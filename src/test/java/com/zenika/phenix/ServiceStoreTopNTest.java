package com.zenika.phenix;

import com.zenika.phenix.configuration.Config;
import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.initialisation.InitialisationAndCleanningData;
import com.zenika.phenix.repositories.FileStoreRepository;
import com.zenika.phenix.repositories.StoreRepository;
import com.zenika.phenix.services.top.topn.ServiceStoreTopN;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceStoreTopNTest extends InitialisationAndCleanningData {


    private void initDataTest() throws IOException {
        String fileName = "testDir/workingDir/datatmp2/20170513/";
        File f = new File(fileName) ;
        f.mkdirs();
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName+"id1.data"))){
            bufferedWriter.write("266|8|28.28"+"\n");
            bufferedWriter.write("123|1|18.26"+"\n");
            bufferedWriter.write("243|9|13.70"+"\n");
            bufferedWriter.write("352|5|37.10"+"\n");
            bufferedWriter.write("366|1|42.62"+"\n");
            bufferedWriter.write("417|1|88.17"+"\n");
            bufferedWriter.write("464|1|36.94"+"\n");
            bufferedWriter.write("491|5|45.30"+"\n");
            bufferedWriter.write("530|1|84.67"+"\n");
            bufferedWriter.write("629|6|36.54"+"\n");
        }

        fileName = "testDir/workingDir/datatmp2/20170513/";
        f = new File(fileName) ;
        f.mkdirs();
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName+"id2.data"))){
            bufferedWriter.write("266|8|28.28"+"\n");
            bufferedWriter.write("123|1|18.26"+"\n");
            bufferedWriter.write("243|9|13.70"+"\n");
            bufferedWriter.write("352|5|37.10"+"\n");
            bufferedWriter.write("366|1|42.62"+"\n");
            bufferedWriter.write("417|1|88.17"+"\n");
            bufferedWriter.write("464|1|36.94"+"\n");
            bufferedWriter.write("491|5|45.30"+"\n");
            bufferedWriter.write("530|1|84.67"+"\n");
            bufferedWriter.write("629|6|36.54"+"\n");
        }
    }

    @Test
    public void testcalculatTopNByMagasin() throws IOException {
        // Having
        Config config = new Config("testDir/configuration/config.properties") ;
        InitConfig initConfig = new InitConfig(config.properties()) ;

        initDataTest() ;
        StoreRepository storeRepository = new FileStoreRepository(initConfig.listMagazinFileID) ;

        // Doing
        ServiceStoreTopN serviceStoreTopN = new ServiceStoreTopN(initConfig, storeRepository) ;
        serviceStoreTopN.calculatTopNByMagasin();

        // Get
        String f = "top_100_ca_id1_20170513.data" ;
        String filename = initConfig.top100CaMagasinDir + "20170513/" + f ;
        BufferedReader br = new BufferedReader(new FileReader(filename)) ;
        List<String> collect = br.lines().collect(Collectors.toList());
        Assert.assertTrue(collect.size() ==3 );
        Assert.assertTrue(collect.get(0).equals("417|1|88.17"));


        f = "top_100_ventes_id1_20170513.data" ;
        filename = initConfig.top100VentesMagasinDir + "20170513/" + f ;
        br = new BufferedReader(new FileReader(filename)) ;
        collect = br.lines().collect(Collectors.toList());
        Assert.assertTrue(collect.size() ==3 );
        Assert.assertTrue(collect.get(0).equals("243|9|13.70"));

    }
}
