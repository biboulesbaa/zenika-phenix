package com.zenika.phenix;

import com.zenika.phenix.configuration.Config;
import com.zenika.phenix.configuration.InitConfig;
import com.zenika.phenix.initialisation.InitialisationAndCleanningData;
import com.zenika.phenix.services.TransactionsFileSplitter;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionsFileSplitterTest extends InitialisationAndCleanningData {

    @Test
    public void testSplit() throws IOException {
        Config config = new Config("testDir/configuration/config.properties") ;
        InitConfig initConfig = new InitConfig(config.properties());
        TransactionsFileSplitter stageSpliteTransactionToTransactionByMagazin = new TransactionsFileSplitter(initConfig);
        stageSpliteTransactionToTransactionByMagazin.splitByMagazinId();

        try(BufferedReader br = new BufferedReader(new FileReader("testDir/workingDir/datatmp1/20170513/id1.data"))){
            List<String> collect = br.lines().collect(Collectors.toList());
            Assert.assertTrue("size != 5" , collect.size() == 5);
            Assert.assertTrue("id1 file doesn't contain 1|2" , collect.contains("1|2"));
        }
    }

}
