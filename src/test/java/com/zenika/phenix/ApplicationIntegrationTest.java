package com.zenika.phenix;

import com.zenika.phenix.configuration.Config;
import com.zenika.phenix.initialisation.InitialisationAndCleanningData;
import com.zenika.phenix.serviceScheduler.ServiceScheduler;
import org.junit.*;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationIntegrationTest extends InitialisationAndCleanningData {



    @Test
    public void shouldProduceCorrectResults() throws IOException {
        Config config = new Config("testDir/configuration/config.properties") ;
        ServiceScheduler serviceScheduler = new ServiceScheduler(config.properties()) ;
        serviceScheduler.launch();

        try(BufferedReader br = new BufferedReader(new FileReader("testDir/top_100_ventes_magasin/20170513/top_100_ventes_id1_20170513.data"))){
            List<String> collect = br.lines().collect(Collectors.toList());
            Assert.assertTrue("size != 3" , collect.size() == 3);
            Assert.assertTrue("top_100_ca_magasin de id1 doesn't contain 2|4|14.00" , collect.contains("2|4|14.00"));
        }


        try(BufferedReader br = new BufferedReader(new FileReader("testDir/top_100_ventes_global/20170513/top_100_ventes_GLOBAL_20170513.data"))){
            List<String> collect = br.lines().collect(Collectors.toList());
            Assert.assertTrue("size != 3" , collect.size() == 3);
            Assert.assertTrue("top_100_ca_magasin de id1 doesn't contain 2|4|14.00" , collect.contains("1|10.0"));
        }

    }


}
