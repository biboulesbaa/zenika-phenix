
## Prérequis : 
- java 8 ou plus
- maven 


## Write Configuration : 

Write yours configurations to a properties file. <br>
All configurations are required. 

Configurations :
- date : format yyyyMMdd. Example : date=20170514
- dataSource : Directory containing transaction file and refenciels files of the day "date". <br> 
        Example dataSource=dataCom/ <br>
        In our example you must have :  dataCom/20170514/reference_prod-<MAGASIN-ID>_20170514.data, ..., dataCom/20170514/transactions_20170514.data 
- topN : example : topN=100
- listMagazinFileID: You must have a file containing all your magazinIDs. Example  : listMagazinFileID=dataCom/listMagazinId.data

- working_dir:  A directory used by the application. Don't remove any file dating after "date-J". <br> 
        Example : working_dir=workingDir/

- output_top_100_ca_magasin_dir : Example : output_top_100_ca_magasin_dir=top_100_ca_magasin/
- output_top_100_ventes_magasin_dir : Example : output_top_100_ventes_magasin_dir=top_100_ventes_magasin/
- output_top_100_ca_magasin_J7_dir : Example : output_top_100_ca_magasin_J7_dir=top_100_ca_magasin_j7/
- output_top_100_ventes_magasin_J7_dir : Example : output_top_100_ventes_magasin_J7_dir=top_100_ventes_magasin_j7/

- output_top_100_ca_global_dir : Example : output_top_100_ca_global_dir=top_100_ca_global/
- output_top_100_ventes_global_dir : Example : output_top_100_ventes_global_dir=top_100_ventes_global/
- output_top_100_ventes_global_J7_dir : Example : output_top_100_ventes_global_J7_dir=top_100_ventes_global_j7/
- output_top_100_ca_global_J7_dir : Example : output_top_100_ca_global_J7_dir=top_100_ca_global_j7/
 


## Compile code : 

<code> mvn clean install </code>


## Start Application  : 
java -Xmx<strong><em>heap-size</em></strong>m  -jar target/zenika-phenix-1.0-SNAPSHOT-jar-with-dependencies.jar <strong><em>PATH-TO-CONFIG-FILE</em></strong> 


Example : <br> 
<code> java -Xmx400m  -jar target/zenika-phenix-1.0-SNAPSHOT-jar-with-dependencies.jar configuration/config.properties </code>


## OUTPUT 

Your results are presents in output directories indicated in configuration's file. <br>
Logs will be in directory "logs/"