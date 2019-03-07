package com.zenika.phenix.services.top.topn;

import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.domain.MyMapStruct;
import com.zenika.phenix.repositories.StoreRepository;

import java.io.*;

import static com.zenika.phenix.configuration.InitConfig.TOP_100_CA;
import static com.zenika.phenix.configuration.InitConfig.TOP_100_VENTES;

public class ServiceStoreTopN {

    public static final String DATA_EXT = ".data";
    private InitConfig initConfig ;
    private final StoreRepository storeRepository ;

    public ServiceStoreTopN(InitConfig initConfig, StoreRepository storeRepository)  {
        this.initConfig = initConfig ;
        this.storeRepository = storeRepository ;
    }


    public void calculatTopNByMagasin() throws IOException {

        // Top 100 Vente et CA Par magasin

        for (String storeId : storeRepository.getAllStoreIds()){
            MyMapStruct myMapStructVentesByMagazin = new MyMapStruct(1 , initConfig.topn) ;
            MyMapStruct myMapStructCAByMagazin = new MyMapStruct(2 , initConfig.topn) ;

            BufferedReader storeVolumeReader = createBrStoreVolume(storeId)  ;
            storeVolumeReader.lines().forEach(l-> {
                myMapStructVentesByMagazin.insertToStruct(l);
                myMapStructCAByMagazin.insertToStruct(l);
            }) ;

            myMapStructVentesByMagazin.writeTreeMaptoFile(createHMWriterMagasinTop100(storeId), false);
            myMapStructVentesByMagazin.writeTreeMaptoFile(createHMWriterMagasinAllVentesOrdred(storeId), true);

            myMapStructCAByMagazin.writeTreeMaptoFile(createHMWriterMagasinTop100CA(storeId) , false);
            myMapStructCAByMagazin.writeTreeMaptoFile(createHMWriterMagasinAllCAOrdred(storeId) , true);
            storeVolumeReader.close();

        }

    }

    private BufferedWriter createHMWriterMagasinAllCAOrdred(String storeId) throws IOException {
        File f = new File (initConfig.datatmp4Dir + initConfig.date  ,  storeId + DATA_EXT) ;
        return new BufferedWriter(new FileWriter(f,true) );
    }

    private BufferedWriter createHMWriterMagasinTop100CA(String storeId) throws IOException {
        File f = new File(initConfig.top100CaMagasinDir + initConfig.date , TOP_100_CA + storeId + "_" + initConfig.date + DATA_EXT) ;
        return new BufferedWriter(new FileWriter(f,true) );
    }

    private BufferedWriter createHMWriterMagasinAllVentesOrdred(String storeId) throws IOException {
        File f = new File(initConfig.datatmp3Dir + initConfig.date ,  storeId + DATA_EXT) ;
        return new BufferedWriter(new FileWriter(f,true) );
    }

    private BufferedWriter createHMWriterMagasinTop100(String storeId) throws IOException {
        File f = new File (initConfig.top100VentesMagasinDir + initConfig.date , TOP_100_VENTES + storeId + "_" + initConfig.date+ DATA_EXT) ;
        return new BufferedWriter( new FileWriter(f, true));
    }

    private BufferedReader createBrStoreVolume(String storeId) throws FileNotFoundException {
        File f = new File(initConfig.datatmp2Dir + initConfig.date  , storeId+ DATA_EXT) ;
        return new BufferedReader(new FileReader(f));
    }


}
