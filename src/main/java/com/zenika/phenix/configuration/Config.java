package com.zenika.phenix.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Logger logger = LogManager.getLogger(Config.class.getName());

    private String CONFIGURATION_FILENAMES_PROPERTIES ; //= "configuration/config.properties";
    private Properties filesList ;

    public static String DELIMITER = "\\|" ;
    public Config (String configFile){
        this.CONFIGURATION_FILENAMES_PROPERTIES = configFile ;

        filesList = new Properties();
        try(InputStream is = new FileInputStream(CONFIGURATION_FILENAMES_PROPERTIES);) {
            filesList.load(is);
            logger.info("Config loaded");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    public Properties properties (){
        return this.filesList ;
    }



}
