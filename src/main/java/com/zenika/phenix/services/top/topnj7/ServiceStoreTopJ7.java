package com.zenika.phenix.services.top.topnj7;

import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.repositories.StoreRepository;
import com.zenika.phenix.services.top.AbstractServiceTop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServiceStoreTopJ7 extends AbstractServiceTop {
    private static final Logger logger = LogManager.getLogger(ServiceStoreTopJ7.class.getName());
    private final StoreRepository storeRepository ;
    private InitConfig initConfig ;

    public ServiceStoreTopJ7(InitConfig initConfig, StoreRepository storeRepository){
        this.initConfig = initConfig ;
        this.storeRepository = storeRepository ;
    }

    public void calculateMagsinTopNJM() throws IOException {
        String date = initConfig.date ;
        String[] listDateJN = calculateListeDate(date) ;

        if (validateTmpDataOfJ7ByMagazin(listDateJN) ){
            startParalleleCalcul(listDateJN ) ;


        }else logger.info("No validate data for ColculateTopNJ-7");


    }

    private void startParalleleCalcul(String[] listDateJN)  {
        logger.info("In parallele calcul ");
        List<String> collect = new ArrayList<>(storeRepository.getAllStoreIds());
        List<String> list1 = collect.subList(0, collect.size()/2 ) ;
        List<String> list2 = collect.subList( collect.size()/2 , collect.size() ) ;

        logger.info("In parallele calcul : splited list of magazin to two sublist ");

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Runnable runnable1 = () -> {
            calculate(listDateJN , list1) ;
        };

        Runnable runnable2 = () -> {
            calculate(listDateJN, list2);
        };

        executorService.submit(runnable1);
        executorService.submit(runnable2);
        executorService.shutdown();

        try {
            executorService.awaitTermination(240, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void calculate(String[] listDateJN, Collection<String> listStoresIds)  {
        logger.info("Found validate data for calculateMagsinTopNJ-7") ;

        for (String storeId : listStoresIds){
            TreeMap<Integer, Double> treeMapProQte = new TreeMap<>() ;
            TreeMap<Integer, Double> treeMapProCA = new TreeMap<>() ;

            for (String mydate : listDateJN){
                String fileNameMagazinVenteByDate = initConfig.datatmp3Dir +  mydate + "/" + storeId + ".data" ;
                String fileNameMagazinCAByDate = initConfig.datatmp4Dir + mydate + "/" + storeId + ".data" ;
                try {
                    BufferedReader bufferedReaderMagasinVente = new BufferedReader(new FileReader(fileNameMagazinVenteByDate)) ;
                    fillTreemapFromBufReader(treeMapProQte, bufferedReaderMagasinVente , 1 );

                    BufferedReader bufferedReaderMagasinCA = new BufferedReader(new FileReader(fileNameMagazinCAByDate)) ;
                    fillTreemapFromBufReader(treeMapProCA, bufferedReaderMagasinCA , 2);

                } catch (FileNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }

            String fileName1 = initConfig.top100VentesMagasinJ7Dir +  "/" + initConfig.date +"/top_100_ventes_" + storeId + "_" + initConfig.date+ ".data" ;
            String fileName2 = initConfig.top100CaMagasinJ7Dir + "/" + initConfig.date  +"/top_100_ca_" + storeId + "_" + initConfig.date+ ".data"  ;

            try {
                write(treeMapProQte, treeMapProCA, fileName1, fileName2, initConfig.topn);
            } catch (IOException e) {
                logger.error(e.getMessage() ) ;
            }

            treeMapProCA.clear();
            treeMapProQte.clear();
        }
    }


    private Boolean validateTmpDataOfJ7ByMagazin(String[] listDate  ) {

        List<Boolean> collect1 = storeRepository.getAllStoreIds()
                .stream().map(magasinid -> {
                    List<Boolean> collect = Arrays.stream(listDate).map(d -> {
                        String fileNameMagazinVenteByDate = initConfig.datatmp3Dir + d + "/" + magasinid + ".data";
                        String fileNameMagazinCAByDate = initConfig.datatmp4Dir + d + "/" + magasinid + ".data";

                        File dir1 = new File(fileNameMagazinVenteByDate);
                        File dir2 = new File(fileNameMagazinCAByDate);
                        return dir1.exists() && dir2.exists();
                    }).collect(Collectors.toList());
                    return !collect.contains(false);
                }).collect(Collectors.toList());

        return !collect1.contains(false);

    }


}
