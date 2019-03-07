package com.zenika.phenix.services.top.topn;

import com.zenika.phenix.configuration.Config;
import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.repositories.StoreRepository;
import com.zenika.phenix.services.top.AbstractServiceTop;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ServiceGlobalTopN extends AbstractServiceTop {
    private static final String DATA_EXT = ".data";
    private final StoreRepository storeRepository ;
    private InitConfig initConfig ;

    public ServiceGlobalTopN(InitConfig initConfig, StoreRepository storeRepository) throws FileNotFoundException {
        this.initConfig = initConfig ;
        this.storeRepository = storeRepository ;
    }

    public void calculatTopNGlobal() throws IOException {
        HashMap<Integer, Double> MapProQte = new HashMap<>() ;
        HashMap<Integer, Double> MapProCA = new HashMap<>() ;

        for (String storeid : storeRepository.getAllStoreIds()){
            BufferedReader brStore = getBufferedReader(storeid) ;
            brStore.lines().forEach(line -> {
                int idProduit = Integer.parseInt(line.split(Config.DELIMITER)[0] ) ;
                Double qte = Double.parseDouble( line.split(Config.DELIMITER)[1] )  ;
                Double ca = Double.parseDouble(line.split(Config.DELIMITER)[2]) ;
                if (MapProQte.containsKey(idProduit) ){
                    MapProQte.put(idProduit , qte + MapProQte.get(idProduit) ) ;

                }else MapProQte.put(idProduit , qte) ;

                if ( MapProCA.containsKey(idProduit)){
                    MapProCA.put(idProduit , ca + MapProCA.get(idProduit));
                }else MapProCA.put(idProduit, ca) ;
            });
            brStore.close();
        }
        writeGlobalTri(MapProQte, MapProCA);

    }

    private BufferedReader getBufferedReader(String storeid) throws FileNotFoundException {
        File file = new File( initConfig.datatmp2Dir + initConfig.date  , storeid + DATA_EXT ) ;
        return new BufferedReader(new FileReader(file)) ;
    }

    private void writeGlobalTri(Map<Integer, Double> MapProQte, Map<Integer, Double> MapProCA) throws IOException {
        String date = initConfig.date ;

        // Write top_100_vente_global
        String fileName = initConfig.top100VentesGlobalDir +date +"/top_100_ventes_GLOBAL_" + date+ DATA_EXT;
        writeFile(MapProQte, false, fileName);

        //  Write vente_global orderd
        fileName = initConfig.datatmp5Dir +date +"/ventes_GLOBAL_" + date+ DATA_EXT ;
        writeFile(MapProQte , true , fileName);

        // Write top_100_ca_global
        fileName = initConfig.top100CaGlobalDir +date +"/top_100_ca_GLOBAL_" + date + DATA_EXT ;
        writeFile(MapProCA, false, fileName);

        // Write ca_global ordred
        fileName = initConfig.datatmp5Dir +date +"/ca_GLOBAL_" + date + DATA_EXT ;
        writeFile(MapProCA, true , fileName);
    }

    private void writeFile(Map<Integer, Double> MapProQte, Boolean writeAll, String fileName) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        writeTreeMap( sortByValues(MapProQte) , bufferedWriter , writeAll, initConfig.topn) ;
    }

}
