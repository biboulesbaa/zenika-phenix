package com.zenika.phenix.services.top.topnj7;

import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.services.top.AbstractServiceTop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServiceStoreTopJ7 extends AbstractServiceTop {
    public InitConfig initConfig ;

    private static final Logger logger = LogManager.getLogger(ServiceStoreTopJ7.class.getName());
    public ServiceStoreTopJ7(InitConfig initConfig){
        this.initConfig = initConfig ;
    }

    public void calculateMagsinTopNJM() throws IOException {
        String date = initConfig.date ;
        String[] listDateJN = calculateListeDate(date) ;

        String listMagazinFileID = initConfig.dataSource +"/listMagazinId.data";
        BufferedReader br = new BufferedReader(new FileReader(listMagazinFileID)) ;

        if (validateTmpDataOfJ7ByMagazin(listDateJN, br) ){
            startParalleleCalcul(listDateJN , listMagazinFileID) ;


        }else logger.info("No validate data for ColculateTopNJ-7");


    }

    private void startParalleleCalcul(String[] listDateJN, String listMagazinFileID) throws FileNotFoundException {
        BufferedReader br2 = new BufferedReader(new FileReader(listMagazinFileID)) ;
        List<String> collect = br2.lines().collect(Collectors.toList());
        List<String> list1 = collect.subList(0, collect.size()/2 ) ;
        List<String> list2 = collect.subList( collect.size()/2 , collect.size() ) ;


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
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void calculate(String[] listDateJN, List<String> listMagazinId)  {
        logger.info("Found validate data for calculateMagsinTopNJ-7") ;

        listMagazinId.stream().forEach(magasinid -> {
            TreeMap<Integer, Double> treeMapProQte = new TreeMap<>() ;
            TreeMap<Integer, Double> treeMapProCA = new TreeMap<>() ;

            Arrays.stream(listDateJN).forEach(mydate -> {
                String fileNameMagazinVenteByDate = initConfig.datatmp3Dir +  mydate + "/" + magasinid + ".data" ;
                String fileNameMagazinCAByDate = initConfig.datatmp4Dir + mydate + "/" + magasinid + ".data" ;
                try {
                    BufferedReader bufferedReaderMagasinVente = new BufferedReader(new FileReader(fileNameMagazinVenteByDate)) ;
                    fillTreemapFromBufReader(treeMapProQte, bufferedReaderMagasinVente , 1 );

                    BufferedReader bufferedReaderMagasinCA = new BufferedReader(new FileReader(fileNameMagazinCAByDate)) ;
                    fillTreemapFromBufReader(treeMapProCA, bufferedReaderMagasinCA , 2);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

            try {
                String fileName1 = initConfig.top100VentesMagasinJ7Dir +  "/" + initConfig.date +"/top_100_ventes_" + magasinid + "_" + initConfig.date+ ".data" ;
                String fileName2 = initConfig.top100CaMagasinJ7Dir + "/" + initConfig.date  +"/top_100_ca_" + magasinid + "_" + initConfig.date+ ".data"  ;
                write(treeMapProQte, treeMapProCA, fileName1, fileName2, initConfig.topn);
            } catch (IOException e) {
                e.printStackTrace();
            }

            treeMapProCA.clear();
            treeMapProQte.clear();
        });
    }


    private Boolean validateTmpDataOfJ7ByMagazin(String[] listDate , BufferedReader br ) {
        List<Boolean> collect1 = br.lines().map(magasinid -> {
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
