package com.zenika.phenix.generations;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class GenerationRandomData {
    private static final Logger logger = LogManager.getLogger(GenerationRandomData.class.getName());
    public static void main(String [] args) throws IOException {
        String date = "20170514";

        String [] listDate = {"20170506","20170507","20170508","20170509","20170510", "20170511","20170512", "20170513"} ;
        Arrays.stream(listDate).forEach(mydate -> {
            generateTransaction(mydate);
            generateMagazinPriceRef(mydate);
        });
        generateMagazinPriceRef(date);
        generateTransaction(date);


    }

    private static void generateTransaction(String date) {
        ArrayList<String> magasinIdList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("dataCom/listMagazinId.data"))) {
            br.lines().forEach(magasinIdList::add);
        } catch (IOException e) {
            logger.error(e.getMessage() ) ;
        }

        String fileName = "dataCom/"+ date+"/transactions_" + date + ".data";
        File f = new File(fileName);
        if (!f.getParentFile().exists()) f.getParentFile().mkdir() ;

        int qte = 3;
        int productID = 926;
        String magazinId = "2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71";


        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true), 163840);
            String transaction1 = "13|20170514T065539+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|926|4";
            String transaction2 = "13|20170514T065539+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|926|4";
            for (int i = 0; i < 1000000; i++) {
                qte = (int) (Math.random() * 10)+1;
                magazinId = magasinIdList.get((int) (Math.random() * 1200));
                productID = (int) (Math.random() * 1000000);
                transaction1 = i + "|20170514T065539+0100|" + magazinId + "|" + productID + "|" + qte;

                qte = (int) (Math.random() * 10) + 1;
                magazinId = magasinIdList.get((int) (Math.random() * 1200));
                transaction2 = i + "|20170514T065539+0100|" + magazinId + "|" + productID + "|" + qte;

                bw.write(transaction1 + "\n");
                bw.write(transaction2 + "\n");
            }
            bw.close();

        } catch (IOException e) {
            logger.error(e.getMessage() ) ;
        }
    }

    private static void generateMagazinPriceRef(String date) {
        DecimalFormat df2 = new DecimalFormat(".##");
        try( BufferedReader br = new BufferedReader(new FileReader("dataCom/listMagazinId.data") )) {
            br.lines().forEach(
                    magasinId -> {
                        try {
                            //String date = "20170514";
                            String fileNameMagasinRef = "dataCom/"+date+"/reference_prod-" + magasinId + "_" + date + ".data" ;
                            BufferedWriter bw = new BufferedWriter(new FileWriter(fileNameMagasinRef, true ) , 163840 ) ;
                            int i =0 ;
                            while (i<1000000){
                                Double v = (Math.random() * 100) + 1;

                                bw.write(i + "|" + String.format(Locale.US, "%.2f", v) + "\n");
                                i++;
                            }
                            bw.close();
                        } catch (IOException e) {
                            logger.error(e.getMessage() ) ;
                        }
                    }
            );


        } catch (IOException e) {
            logger.error(e.getMessage() ) ;
        }
    }

    private static void generateMagasinIds() throws IOException {
        String filename = "reference_prod-10f2f3e6-f728-41f3-b079-43b0aa758100_20170514.data" ;
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("dataCom/listMagazinId.data"))){
            for ( int i= 1000 ; i< 2200 ; i++ ){
                filename = "10f2f3e6-f728-41f3-b079-43b0aa75"+ i ;
                bw.write(filename +"\n");
            }
        }
    }
}
