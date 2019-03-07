package com.zenika.phenix.services.top.topnj7;

import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.services.top.AbstractServiceTop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ServiceGlobalTopNJ7 extends AbstractServiceTop {
    private static final Logger logger = LogManager.getLogger(ServiceGlobalTopNJ7.class.getName());
    private InitConfig initConfig ;
    private final String FILE_INTERMIDIATE_DATA_CA = "ca_GLOBAL" ;
    private final String FILE_INTERMIDIATE_DATA_VENTES = "ventes_GLOBAL" ;

    public ServiceGlobalTopNJ7(InitConfig initConfig){
        this.initConfig = initConfig ;
    }

    public void ColculateTopNJM(){
        String date = initConfig.date ;
        String[] listDate = calculateListeDate(date) ;

        if (validateDataOfJ7(listDate) ) {
            calculate(date, listDate);

        } else {
            logger.info("No validate data for ColculateTopNJM");
        }


    }

    private void calculate(String date, String[] listDate) {
        logger.info("Found validate data for GlobalTopNJ-7") ;
        TreeMap<Integer, Double> treeMapVentes = new TreeMap<>() ;
        TreeMap<Integer, Double> treeMapCA = new TreeMap<>() ;

        Arrays.stream(listDate).forEach(mydate -> {
            String filenameVente = initConfig.datatmp5Dir + mydate + "/" + FILE_INTERMIDIATE_DATA_VENTES + "_" + mydate + ".data" ;
            String filenameCA = initConfig.datatmp5Dir + mydate + "/" + FILE_INTERMIDIATE_DATA_CA + "_" + mydate + ".data" ;
            try {
                BufferedReader brVenteGlobal = new BufferedReader(new FileReader(filenameVente)) ;
                fillTreemapFromBufReader(treeMapVentes, brVenteGlobal, 1);

                BufferedReader brCAGlobal = new BufferedReader(new FileReader(filenameCA)) ;
                fillTreemapFromBufReader(treeMapCA, brCAGlobal, 1);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });


        try {
            String fileName1 = initConfig.top100VentesGlobalJ7Dir +  "/" + date +"/top_100_ventes_GLOBAL_" + date+ ".data" ;
            String fileName2 = initConfig.top100CaGlobalJ7Dir + "/" + date  +"/top_100_ca_GLOBAL_"   + date+ ".data"  ;
            write(treeMapVentes, treeMapCA, fileName1, fileName2, initConfig.topn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        treeMapVentes.clear();
        treeMapCA.clear();
    }

    private Boolean validateDataOfJ7(String[] listDate) {
        List<Boolean> collect = Arrays.stream(listDate).map(d -> {
            String filenameVente = initConfig.datatmp5Dir + d + "/" + FILE_INTERMIDIATE_DATA_VENTES + "_" + d + ".data";
            String filenameCA = initConfig.datatmp5Dir + d + "/" + FILE_INTERMIDIATE_DATA_CA + "_" + d + ".data";

            File dir1 = new File(filenameVente);
            File dir2 = new File(filenameCA);
            return dir1.exists() && dir2.exists();
        }).collect(Collectors.toList());

        return  !collect.contains(false) ;

    }

}
