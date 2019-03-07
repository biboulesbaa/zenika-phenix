package com.zenika.phenix.services;

import com.zenika.phenix.configuration.Config;
import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.repositories.StoreRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProductSalesVolumeCalculator {

    private static final Logger logger = LogManager.getLogger(ProductSalesVolumeCalculator.class.getName());

    private static final String FIELD_SEPARATOR = "|";
    private static final String DATA_EXT = ".data";

    private final InitConfig initConfig ;

    private final StoreRepository repository;

    public ProductSalesVolumeCalculator(InitConfig initConfig,
                                        StoreRepository repository) {
        Objects.requireNonNull(initConfig, "initConfig can't be null");
        Objects.requireNonNull(repository, "repository can't be null");
        this.initConfig = initConfig ;
        this.repository = repository;
    }

    public void computeSalesVolumeForAllProductsPerStores(int parallelism) {
        Collection<List<String>> stores = partitionStoresFor(parallelism);
        ExecutorService executorService = Executors.newFixedThreadPool(parallelism);

        for (List<String> ids : stores) {
            Runnable runnable = () -> computeSalesVolumeForstores(ids);
            executorService.submit(runnable);
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {

        }
    }

    private void computeSalesVolumeForstores(Collection<String> storeIds)  {
        for (String storeId : storeIds){
            logger.info("Starting to sales volume for store {}", storeId);
            Map<String, Long> quantitiesKeyedByProduct = sumVolumePerProduct(storeId);

            try (BufferedWriter bw = getWriterForStoreId(storeId)){
                String line ;
                BufferedReader products = getReferenceReaderProductsForStoreId(storeId);
                while (products.ready() && ( (line = products.readLine() ) != null)){
                    String [] split = line.split(Config.DELIMITER) ;
                    String productId = split[0];
                    Double price = Double.parseDouble(split[1]) ;

                    if (quantitiesKeyedByProduct.containsKey(productId)) {
                        double ca = quantitiesKeyedByProduct.get(productId) * price;
                        String formatedCa = String.format(Locale.US, "%.2f", ca);
                        bw.write(productId + FIELD_SEPARATOR +  quantitiesKeyedByProduct.get(productId) + FIELD_SEPARATOR + formatedCa);
                        bw.newLine();
                    }
                }
            }catch (IOException e){
                throw new RuntimeException(e) ;
            }
        }

        logger.info(Thread.currentThread().getName() + " : End computing Sales Volume  for each store");
    }

    private Map<String, Long> sumVolumePerProduct(String storeId) {
        Map<String, Long> quantitiesKeyedByProduct = new HashMap<>();
        BufferedReader br = getReaderForStoreId(storeId);

        br.lines().forEach(l -> {
            String[] split = l.split(Config.DELIMITER) ;
            String productId = split[0];
            String quantity = split[1];
            long sum = quantitiesKeyedByProduct.getOrDefault(productId , 0L) + Long.parseLong(quantity) ;
            quantitiesKeyedByProduct.put(productId, sum) ;
        });

        return quantitiesKeyedByProduct;
    }

    private Collection<List<String>> partitionStoresFor(int nthreads) {
        Collection<String> ids = repository.getAllStoreIds();
        final AtomicInteger counter = new AtomicInteger(0);
        return ids.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / (ids.size() / nthreads)))
                .values();
    }

    private BufferedWriter getWriterForStoreId(String id) {
        try {
            File f = new File(initConfig.datatmp2Dir + initConfig.date, id + DATA_EXT);
            return new BufferedWriter(new FileWriter(f,true));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private BufferedReader getReaderForStoreId(String id) {
        try {
            File f = new File(initConfig.datatmp1Dir + initConfig.date , id + DATA_EXT);
            return new BufferedReader(new FileReader(f) )  ;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private BufferedReader getReferenceReaderProductsForStoreId(String id) {
        try {
            String referenceFileName = "reference_prod-"+ id + "_" + initConfig.date + ".data" ;
            File f = new File(initConfig.dataSource + "/" + initConfig.date, referenceFileName);
            return new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
