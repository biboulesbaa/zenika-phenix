package com.zenika.phenix.services;

import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.repositories.StoreRepository;
import com.zenika.phenix.services.top.topn.ServiceGlobalTopN;
import com.zenika.phenix.services.top.topn.ServiceStoreTopN;
import com.zenika.phenix.services.top.topnj7.ServiceGlobalTopNJ7;
import com.zenika.phenix.services.top.topnj7.ServiceStoreTopJ7;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ComputeMetrics {
    private static final Logger logger = LogManager.getLogger(ComputeMetrics.class.getName());

    private InitConfig initConfig ;
    private final StoreRepository storeRepository ;

    public ComputeMetrics(InitConfig initConfig, StoreRepository storeRepository) throws IOException {
        this.initConfig = initConfig ;
        this.storeRepository = storeRepository;
    }

    public void launch() throws IOException {
        logger.info("Start calculating top " + initConfig.topn + " By magazin " );
        ServiceStoreTopN serviceStoreTopN = new ServiceStoreTopN(initConfig, storeRepository);
        serviceStoreTopN.calculatTopNByMagasin();
        logger.info("End calculating top " +  initConfig.topn + " By magazin " );
        logger.info("Start calculating top " +  initConfig.topn + " By magazin of J-7 " );
        ServiceStoreTopJ7 magasinTopNJM = new ServiceStoreTopJ7(initConfig) ;
        magasinTopNJM.calculateMagsinTopNJM();
        logger.info("End calculating top " + initConfig.topn + " By magazin of J-7 " );

        logger.info("Start calculating top " + initConfig.topn + " Global " );
        ServiceGlobalTopN globalTopN = new ServiceGlobalTopN(initConfig, storeRepository);
        globalTopN.calculatTopNGlobal();
        logger.info("End calculating top " + initConfig.topn + " Global " );
        logger.info("Start calculating top " + initConfig.topn + " Global of J-7" );
        ServiceGlobalTopNJ7 globalTopNJM = new ServiceGlobalTopNJ7(initConfig) ;
        globalTopNJM.ColculateTopNJM();
        logger.info("End calculating top " + initConfig.topn + " Global of J-7" );
    }


}
