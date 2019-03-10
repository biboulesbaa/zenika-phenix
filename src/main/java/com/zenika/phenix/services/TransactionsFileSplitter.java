package com.zenika.phenix.services;

import com.zenika.phenix.configuration.Config;
import com.zenika.phenix.configuration.InitConfig;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TransactionsFileSplitter {

    private static final String DATA_EXT = ".data";
    private static final String TRANSACTIONS_FILE_PREFIX = "transactions_";

    private InitConfig initConfig ;

    private HashMap<String, BufferedWriter> writerByMagazinId = new HashMap<>(2048) ;

    public TransactionsFileSplitter(InitConfig initConfig) {
        Objects.requireNonNull(initConfig, "initConfig can't be null");
        this.initConfig = initConfig ;
    }

    private BufferedWriter getWriterForMagazin(String store) throws IOException {

        BufferedWriter bw = this.writerByMagazinId.get(store);
        if (bw == null) {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(getTempDirectoryForMagazin(store), true));
            this.writerByMagazinId.put(store, writer);
            return writer ;
        }
        return bw;
    }

    private String getTempDirectoryForMagazin(String magazin) {
        return initConfig.datatmp1Dir + initConfig.date + "/" + magazin + DATA_EXT;
    }

    public void splitByMagazinId() {
        String input = getTransactionInputPath();
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String line;
            while (br.ready() && (line = br.readLine()) != null) {
                String[] split = line.split(Config.DELIMITER);
                String idMagazin = split[2];
                String productQte = split[3] + Config.DELIMITER.substring(1) + split[4];

                BufferedWriter writer = getWriterForMagazin(idMagazin);
                writer.write(productQte);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error while reading file " + input, e);
        } finally {
            close();
        }
    }

    /**
     * Creates the transaction file path (format : /%DATA_DIR%/transactions_%DATE%.data")
     */
    private String getTransactionInputPath() {
        return initConfig.dataSource +
               initConfig.date +
               "/" +
               TRANSACTIONS_FILE_PREFIX +
               initConfig.date + DATA_EXT;
    }

    private void close() {
        Set<Map.Entry<String, BufferedWriter>> entries = writerByMagazinId.entrySet();
        for (Map.Entry<String, BufferedWriter> entry : entries) {
            try {
                entry.getValue().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
