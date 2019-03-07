package com.zenika.phenix.services.top;

import com.zenika.phenix.configuration.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractServiceTop {

    protected void write(Map<Integer, Double> treeMapProQte, Map<Integer, Double> treeMapProCA, String fileName1, String fileName2, int topN) throws IOException {

        // Write top_100_vente_global
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName1));
        writeTreeMap( sortByValues(treeMapProQte) , bufferedWriter , false , topN) ;


        // Write top_100_ca_global
        bufferedWriter = new BufferedWriter(new FileWriter(fileName2));
        writeTreeMap( sortByValues(treeMapProCA) , bufferedWriter , false , topN ) ;

    }

    protected void writeTreeMap(Map<Integer, Double> integerDoubleMap, BufferedWriter bufferedWriter, Boolean writeAll, int topN) throws IOException {
        if (writeAll){
            for(Map.Entry<Integer, Double> e : integerDoubleMap.entrySet()){
                bufferedWriter.write(  e.getKey() + "|" + e.getValue() + "\n");
            }
        }else {
            int i = 0 ;
            for(Map.Entry<Integer, Double> e : integerDoubleMap.entrySet()){
                bufferedWriter.write(  e.getKey() + "|" + e.getValue() + "\n");
                i++ ;
                if (i == topN) break;
            }
        }
        bufferedWriter.close();

    }
    protected <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =
                new Comparator<K>() {
                    public int compare(K k1, K k2) {
                        int compare =
                                map.get(k1).compareTo(map.get(k2));
                        if (compare == 0)
                            return -1;
                        else
                            return -compare;
                    }
                };
        Map<K, V> sortedByValues =
                new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }


    protected void fillTreemapFromBufReader(Map<Integer, Double> myMap, BufferedReader bufferedReaderMagasinVente, int QteOrCA) {

        bufferedReaderMagasinVente.lines().forEach(line -> {
            int idProduit = Integer.parseInt(line.split(Config.DELIMITER)[0] ) ;
            Double value = Double.parseDouble( line.split(Config.DELIMITER)[QteOrCA] )  ;
            if (myMap.containsKey(idProduit) ){
                myMap.put(idProduit, myMap.get(idProduit) + value) ;
            }else myMap.put(idProduit, value) ;
        });
    }

    protected String[] calculateListeDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate mydate = LocalDate.parse(date, formatter)  ;

        return new String[]{date,
                formatter.format(mydate.minusDays(1)),
                formatter.format(mydate.minusDays(2)),
                formatter.format(mydate.minusDays(3)),
                formatter.format(mydate.minusDays(4)),
                formatter.format(mydate.minusDays(5)),
                formatter.format(mydate.minusDays(6))
        };
    }


}
