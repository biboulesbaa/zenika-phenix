package com.zenika.phenix;

import com.zenika.phenix.configuration.Config;
import com.zenika.phenix.initialisation.InitialisationAndCleanningData;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class ConfigTest extends InitialisationAndCleanningData {


    @Test
    public void testConfig(){
        Properties properties = new Properties();

        properties.put("date","20170513" );
        properties.put("dataSource", "testDir/dataCom/");
        properties.put("topN","3");
        properties.put("listMagazinFileID","testDir/dataCom/listMagazinId.data");
        properties.put("working_dir", "testDir/workingDir/");


        properties.put("output_top_100_ca_magasin_dir", "testDir/top_100_ca_magasin/");
        properties.put("output_top_100_ventes_magasin_dir","testDir/top_100_ventes_magasin/");
        properties.put("output_top_100_ventes_magasin_J7_dir","testDir/top_100_ventes_magasin_j7/");
        properties.put("output_top_100_ca_magasin_J7_dir","testDir/top_100_ca_magasin_j7/");


        properties.put("output_top_100_ventes_global_dir","testDir/top_100_ventes_global/");
        properties.put("output_top_100_ventes_global_J7_dir","testDir/top_100_ventes_global_j7/");
        properties.put("output_top_100_ca_global_dir","testDir/top_100_ca_global/");
        properties.put("output_top_100_ca_global_J7_dir","testDir/top_100_ca_global_j7/");


        Config config = new Config("testDir/configuration/config.properties") ;

        Assert.assertTrue("Config loaded not correct" , config.properties().equals(properties)   ) ;
    }
}
