package com.zenika.phenix;

import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.initialisation.InitialisationAndCleanningData;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class InitConfigTest extends InitialisationAndCleanningData {

    @Test(expected = NullPointerException.class)
    public void testMustFail() {
        Properties props = null;
        try {
            new InitConfig(props);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInavlideDataSource(){
        Properties properties = new Properties() ;
        properties.put("dataSource", "testDir/dataCom/");
        properties.put("listMagazinFileID", "testDir/dataCom/nofile.data");

        try{
            new InitConfig(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
