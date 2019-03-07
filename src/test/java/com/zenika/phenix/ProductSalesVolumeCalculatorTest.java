package com.zenika.phenix;

import com.zenika.phenix.configuration.Config;
import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.initialisation.InitialisationAndCleanningData;
import com.zenika.phenix.repositories.FileStoreRepository;
import com.zenika.phenix.repositories.StoreRepository;
import com.zenika.phenix.services.ProductSalesVolumeCalculator;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSalesVolumeCalculatorTest extends InitialisationAndCleanningData {
    public void initDataTest() throws IOException {
        String fileName = "testDir/workingDir/datatmp1/20170513/";
        File f = new File(fileName) ;
        f.mkdirs();
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName+"id1.data"))){
            bufferedWriter.write("1|8|"+"\n");
            bufferedWriter.write("2|2|"+"\n");
            bufferedWriter.write("3|1|"+"\n");
            bufferedWriter.write("2|4|"+"\n");
            bufferedWriter.write("1|5|"+"\n");
            bufferedWriter.write("3|1|"+"\n");
            bufferedWriter.write("2|2|"+"\n");
            bufferedWriter.write("1|3|"+"\n");
            bufferedWriter.write("3|4|"+"\n");
        }

        fileName = "testDir/workingDir/datatmp1/20170513/";
        f = new File(fileName) ;
        f.mkdirs();
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName+"id2.data"))){
            bufferedWriter.write("1|8|"+"\n");
            bufferedWriter.write("2|2|"+"\n");
            bufferedWriter.write("3|1|"+"\n");
            bufferedWriter.write("2|4|"+"\n");
            bufferedWriter.write("1|5|"+"\n");
            bufferedWriter.write("3|1|"+"\n");
            bufferedWriter.write("2|2|"+"\n");
            bufferedWriter.write("1|3|"+"\n");
            bufferedWriter.write("3|4|"+"\n");
        }
    }

    @Test
    public void testIntermediateSteps() throws IOException {

        Config config = new Config("testDir/configuration/config.properties") ;
        InitConfig initConfig = new InitConfig(config.properties()) ;
        initDataTest() ;

        StoreRepository repository = new FileStoreRepository(initConfig.listMagazinFileID);
        ProductSalesVolumeCalculator s = new ProductSalesVolumeCalculator(initConfig, repository);
        s.computeSalesVolumeForAllProductsPerStores(1);

        String filename = initConfig.datatmp2Dir + "20170513/id1.data"   ;
        BufferedReader br = new BufferedReader(new FileReader(filename)) ;
        List<String> line1 = br.lines().collect(Collectors.toList());
        Assert.assertTrue("No line 1|16|32.00", line1.contains("1|16|32.00"));
        Assert.assertTrue("No line 3|6|9.00" , line1.contains("3|6|9.00"));


    }

}
