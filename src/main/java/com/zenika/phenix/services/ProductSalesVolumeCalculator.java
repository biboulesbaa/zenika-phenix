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

    public int computeSalesVolumeForAllProductsPerStores(int parallelism) {
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
            logger.error(ignore.getMessage());
        }
        return 1 ;
    }

    private void computeSalesVolumeForstores(Collection<String> storeIds)  {

        logger.info("Starting to sales volume for store {}", storeIds);
        for (String storeId : storeIds){
            Integer[] quantitiesByProduct  = sumVolumePerProduct(storeId);
            int index  ;
            try (BufferedWriter bw = getWriterForStoreId(storeId)){
                index =1 ;
                BufferedReader productsPrices = getReferenceReaderProductsForStoreId(storeId);
                String productPrice  ;
                while (productsPrices.ready() && ( (productPrice = productsPrices.readLine() ) != null) ){
                    if (quantitiesByProduct[index] != null){
                        double ca = Double.parseDouble(productPrice.split(Config.DELIMITER)[1]) * quantitiesByProduct[index];
                        String formatedCa = String.format(Locale.US, "%.2f", ca);
                        bw.write(index + FIELD_SEPARATOR +  quantitiesByProduct[index] + FIELD_SEPARATOR + formatedCa);
                        bw.newLine();
                        index ++ ;
                    }else index ++ ;
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
                throw new RuntimeException(e) ;
            }
        }

        logger.info(Thread.currentThread().getName() + " : End computing Sales Volume  for each store");
    }

    private Integer[] sumVolumePerProduct( String storeId){
        Integer [] list = new Integer[initConfig.productNum + 10] ;
        BufferedReader br = getReaderForStoreId(storeId);
        br.lines().forEach(line -> {
            int productId = Integer.parseInt(line.split(Config.DELIMITER)[0]);
            int qte = Integer.parseInt(line.split(Config.DELIMITER)[1]);
            if (list [productId] != null ) list[productId] =  list[productId] + qte;
            else list [productId] = qte ;
        });
        return list;

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
