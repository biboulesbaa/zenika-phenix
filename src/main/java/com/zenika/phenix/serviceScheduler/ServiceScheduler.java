package com.zenika.phenix.serviceScheduler;

import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.repositories.FileStoreRepository;
import com.zenika.phenix.repositories.StoreRepository;
import com.zenika.phenix.services.ProductSalesVolumeCalculator;
import com.zenika.phenix.services.ComputeMetrics;
import com.zenika.phenix.services.TransactionsFileSplitter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class ServiceScheduler {

    private static final Logger logger = LogManager.getLogger(ServiceScheduler.class.getName());

    private final InitConfig initConfig ;

    public ServiceScheduler(Properties properties) throws IOException {
        initConfig = new InitConfig(properties) ;
    }

    public void launch () throws IOException {
        logger.info("Starting to splitByMagazinId transcation file to transactions By magazin");

        TransactionsFileSplitter splitter = new TransactionsFileSplitter(initConfig);
        splitter.splitByMagazinId();

        logger.info("End spliting transactions file ");

        logger.info("Start calulating CA and Ventes of each product By magazin");

        StoreRepository repository = new FileStoreRepository(initConfig.listMagazinFileID);
        ProductSalesVolumeCalculator calculator = new ProductSalesVolumeCalculator(initConfig, repository);
        calculator.computeSalesVolumeForAllProductsPerStores(2);
        logger.info("End calulating CA and Ventes of each product By magazin");

        ComputeMetrics computeMetrics = new ComputeMetrics(initConfig,repository);
        computeMetrics.launch();
    }
}
