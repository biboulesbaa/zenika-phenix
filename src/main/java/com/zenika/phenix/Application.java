package com.zenika.phenix;

import com.zenika.phenix.configuration.Config;
import com.zenika.phenix.serviceScheduler.ServiceScheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class.getName());

    public static void main(String[] args) throws IOException {

        if (args.length != 1){
            String usage = " Wrong usage : \n " +
                    "Use must use : java  -jar zenika-phenix-1.0-SNAPSHOT-jar-with-dependencies.jar 'Path to your configfile.properties'  " ;
            logger.error(usage);
            System.exit(1);
        }
        String configFile = args[0] ;
        Instant start = Instant.now();
        Config config = new Config(configFile) ;
        ServiceScheduler serviceScheduler = new ServiceScheduler(config.properties()) ;
        serviceScheduler.launch();

        Instant finish = Instant.now();
        long l = Duration.between(start, finish).toMillis();
        logger.info("durantion of execution : " + l/60000 + " minutes and " +  l%60000  + "ms" );

        System.exit(0);
    }

}
