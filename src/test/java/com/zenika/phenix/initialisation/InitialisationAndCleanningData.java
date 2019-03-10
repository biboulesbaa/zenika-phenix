package com.zenika.phenix.initialisation;

import org.junit.After;
import org.junit.Before;

import java.io.*;

public class InitialisationAndCleanningData {


    @Before
    public void  init() throws IOException {
        generateConfigFile();
        generateMagazinIDs();
        generateTransaction("20170513") ;
        generateMagazinPriceRef("20170513") ;

    }

    @After
    public void clean() throws IOException {
        File dir = new File("testDir/"   ) ;
        delete(dir);
    }
    void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

    private void generateMagazinIDs() throws IOException {
        File dir = new File("testDir"   + "/" + "dataCom/") ;
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to create " + dir.getAbsolutePath() ) ;
        }

        try( BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("testDir/dataCom/listMagazinId.data"))){
            bufferedWriter.write("id1\n");
            bufferedWriter.write("id2\n");
        }
    }

    private void generateConfigFile() throws IOException {
        File dir = new File("testDir"   + "/" + "configuration/") ;
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to create " + dir.getAbsolutePath() ) ;
        }

        try( BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("testDir/configuration/config.properties"))){
            bufferedWriter.write("date=20170513\n" );
            bufferedWriter.write("dataSource=testDir/dataCom/\n");
            bufferedWriter.write("topN=3\n");
            bufferedWriter.write("productNum=10\n");
            bufferedWriter.write("listMagazinFileID=testDir/dataCom/listMagazinId.data\n");
            bufferedWriter.write("working_dir=testDir/workingDir/\n");


            bufferedWriter.write("output_top_100_ca_magasin_dir=testDir/top_100_ca_magasin/\n");
            bufferedWriter.write("output_top_100_ventes_magasin_dir=testDir/top_100_ventes_magasin/\n");
            bufferedWriter.write("output_top_100_ventes_magasin_J7_dir=testDir/top_100_ventes_magasin_j7/\n");
            bufferedWriter.write("output_top_100_ca_magasin_J7_dir=testDir/top_100_ca_magasin_j7/\n");


            bufferedWriter.write("output_top_100_ventes_global_dir=testDir/top_100_ventes_global/\n");
            bufferedWriter.write("output_top_100_ventes_global_J7_dir=testDir/top_100_ventes_global_j7/\n");
            bufferedWriter.write("output_top_100_ca_global_dir=testDir/top_100_ca_global/\n");
            bufferedWriter.write("output_top_100_ca_global_J7_dir=testDir/top_100_ca_global_j7/\n");

        }
    }

    private void generateTransaction(String date) {


        String fileName = "testDir/dataCom/"+ date+"/transactions_" + date + ".data";
        File f = new File(fileName);
        if (!f.getParentFile().exists()) f.getParentFile().mkdir() ;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))){

            bw.write("1|20170513T065539+0100|id1|1|2" + "\n");
            bw.write("1|20170513T065539+0100|id1|2|3" + "\n");
            bw.write("2|20170513T065539+0100|id2|3|5" + "\n");
            bw.write("2|20170513T065539+0100|id2|1|2" + "\n");
            bw.write("3|20170513T065539+0100|id1|2|1" + "\n");
            bw.write("3|20170513T065539+0100|id1|3|3" + "\n");
            bw.write("4|20170513T065539+0100|id2|1|4" + "\n");
            bw.write("5|20170513T065539+0100|id2|2|5" + "\n");
            bw.write("6|20170513T065539+0100|id1|3|1" + "\n");
            bw.write("7|20170513T065539+0100|id2|1|2" + "\n");

        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }

    private static void generateMagazinPriceRef(String date) {
        String magasinId = "id1" ;
        String fileNameMagasinRef = "testDir/dataCom/"+date+"/reference_prod-" + magasinId + "_" + date + ".data" ;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileNameMagasinRef, true )  ) ){
            bw.write("1|2.0" + "\n");
            bw.write("2|3.5" + "\n");
            bw.write("3|1.5" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        magasinId = "id2" ;
        fileNameMagasinRef = "testDir/dataCom/"+date+"/reference_prod-" + magasinId + "_" + date + ".data" ;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileNameMagasinRef, true )  ) ){
            bw.write("1|2.5" + "\n");
            bw.write("2|3.0" + "\n");
            bw.write("3|2.0" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
