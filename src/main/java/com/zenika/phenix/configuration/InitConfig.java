package com.zenika.phenix.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class InitConfig {

    private static final String DATATMP1 = "datatmp1/";
    private static final String DATATMP2 = "datatmp2/";
    private static final String DATATMP3 = "datatmp3/";
    private static final String DATATMP4 = "datatmp4/";
    private static final String DATATMP5 = "datatmp5/";
    private static final String DATE = "date";
    private static final String DATA_SOURCE = "dataSource";
    private static final String TOP_N = "topN";
    private static final String PRODUCT_NUM = "productNum";
    private static final String LIST_MAGAZIN_FILE_ID = "listMagazinFileID";
    private static final String WORKING_DIR = "working_dir";
    private static final String OUTPUT_TOP_100_CA_MAGASIN_DIR = "output_top_100_ca_magasin_dir";
    private static final String OUTPUT_TOP_100_VENTES_MAGASIN_DIR = "output_top_100_ventes_magasin_dir";
    private static final String OUTPUT_TOP_100_CA_MAGASIN_J_7_DIR = "output_top_100_ca_magasin_J7_dir";
    private static final String OUTPUT_TOP_100_VENTES_MAGASIN_J_7_DIR = "output_top_100_ventes_magasin_J7_dir";
    private static final String OUTPUT_TOP_100_CA_GLOBAL_DIR = "output_top_100_ca_global_dir";
    private static final String OUTPUT_TOP_100_VENTES_GLOBAL_DIR = "output_top_100_ventes_global_dir";
    private static final String OUTPUT_TOP_100_CA_GLOBAL_J_7_DIR = "output_top_100_ca_global_J7_dir";
    private static final String OUTPUT_TOP_100_VENTES_GLOBAL_J_7_DIR = "output_top_100_ventes_global_J7_dir";

    private final Logger logger = LogManager.getLogger(InitConfig.class);

    public static final String TOP_100_VENTES = "top_100_ventes_";
    public static final String TOP_100_CA = "top_100_ca_";

    public final String datatmp1Dir; //= "datatmp0/";
    public final String datatmp2Dir; //= "datatmp2/";
    public final String datatmp3Dir; //= "datatmp3/";
    public final String datatmp4Dir; //= "datatmp4/";
    public final String datatmp5Dir; //= "datatmp5/";

    public final String top100VentesMagasinDir;  // = "top_100_ventes_magasin/";
    public final String top100CaMagasinDir; // = "top_100_ca_magasin/";
    public final String top100VentesMagasinJ7Dir; //= "top_100_ventes_magasin_j7/";
    public final String top100CaMagasinJ7Dir; //= "top_100_ca_magasin_j7/";
    public final String top100VentesGlobalJ7Dir; //= "top_100_ventes_global_j7/" ;
    public final String top100CaGlobalJ7Dir; //= "top_100_ca_global_j7/" ;
    public final String top100VentesGlobalDir; //= "top_100_ventes_global/" ;
    public final String top100CaGlobalDir; //= "top_100_ca_global/" ;
    public final int topn; //= 100;
    public final int productNum ; //= 100;
    public final String dataSource ;
    public final String date;
    public final String listMagazinFileID ;

    public InitConfig(Properties props) throws IOException {
        Objects.requireNonNull(props, "properties can't be null") ;
        logger.info("Creating new configuration\n");
        logger.info(props);

        validateConfig(props) ;

        this.date = props.getProperty(DATE);
        this.dataSource = props.getProperty(DATA_SOURCE);
        this.topn = Integer.parseInt( props.getProperty(TOP_N) ) ;
        this.productNum = Integer.parseInt(props.getProperty(PRODUCT_NUM));
        this.listMagazinFileID = props.getProperty(LIST_MAGAZIN_FILE_ID) ;

        String workingDir = props.getProperty(WORKING_DIR);
        this.datatmp1Dir = workingDir + DATATMP1;
        this.datatmp2Dir = workingDir + DATATMP2;
        this.datatmp3Dir = workingDir + DATATMP3;
        this.datatmp4Dir = workingDir + DATATMP4;
        this.datatmp5Dir = workingDir + DATATMP5;

        this.top100CaMagasinDir = props.getProperty(OUTPUT_TOP_100_CA_MAGASIN_DIR) ;
        this.top100VentesMagasinDir = props.getProperty(OUTPUT_TOP_100_VENTES_MAGASIN_DIR) ;

        this.top100CaMagasinJ7Dir = props.getProperty(OUTPUT_TOP_100_CA_MAGASIN_J_7_DIR) ;
        this.top100VentesMagasinJ7Dir = props.getProperty(OUTPUT_TOP_100_VENTES_MAGASIN_J_7_DIR) ;


        this.top100CaGlobalDir = props.getProperty(OUTPUT_TOP_100_CA_GLOBAL_DIR);
        this.top100VentesGlobalDir = props.getProperty(OUTPUT_TOP_100_VENTES_GLOBAL_DIR);

        this.top100CaGlobalJ7Dir = props.getProperty(OUTPUT_TOP_100_CA_GLOBAL_J_7_DIR);
        this.top100VentesGlobalJ7Dir = props.getProperty(OUTPUT_TOP_100_VENTES_GLOBAL_J_7_DIR);

        createAllWorkingDirectories() ;

    }

    private void createAllWorkingDirectories() throws IOException {
        cleanIfExistAndCreate(this.datatmp1Dir);
        cleanIfExistAndCreate(this.datatmp2Dir);
        cleanIfExistAndCreate(this.datatmp3Dir);
        cleanIfExistAndCreate(this.datatmp4Dir);
        cleanIfExistAndCreate(this.datatmp5Dir);
        cleanIfExistAndCreate(this.top100VentesMagasinDir);
        cleanIfExistAndCreate(this.top100VentesMagasinJ7Dir);
        cleanIfExistAndCreate(this.top100VentesGlobalDir);
        cleanIfExistAndCreate(this.top100VentesGlobalJ7Dir);
        cleanIfExistAndCreate(this.top100CaMagasinDir);
        cleanIfExistAndCreate(this.top100CaMagasinJ7Dir);
        cleanIfExistAndCreate(this.top100CaGlobalDir);
        cleanIfExistAndCreate(this.top100CaGlobalJ7Dir);
    }

    private void cleanIfExistAndCreate(final String dirpath) throws IOException {
        File dir = new File(dirpath + date);
        if (dir.exists()) {
            logger.info("Cleaning working directory {}", dir.getAbsolutePath());
            for(String s: dir.list()){
                File currentFile = new File(dir.getPath(),s);
                currentFile.delete()  ;
            }
        }

        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to create " + dir.getAbsolutePath() ) ;
        }
    }

    private void validateConfig(Properties props){
        File dir = new File(props.getProperty("dataSource")) ;
        if (! dir.exists()) throw new RuntimeException("dataSource not found") ;

        dir = new File (props.getProperty("listMagazinFileID")) ;
        if (! dir.exists()) throw new RuntimeException("listMagazinFileID not found") ;
    }


}
