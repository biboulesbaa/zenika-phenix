package com.zenika.phenix.domain;

import com.zenika.phenix.configuration.Config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MyMapStruct {

    private final int topN ;//= 100;
    private final int qteOrCA ; //= 1 or 2;
    private Map<Double, String> myMap = new HashMap<>() ;

    public MyMapStruct(int qteOrCA , int topN ){
        this.qteOrCA = qteOrCA;
        this.topN = topN ;
    }

    public void insertToStruct(String l) {
        Double qteOrCaValue = Double.parseDouble(l.split(Config.DELIMITER)[qteOrCA]);

        if (myMap.containsKey(qteOrCaValue)){
            String newValue = myMap.get(qteOrCaValue) + ";" + l;
            myMap.put(qteOrCaValue, newValue) ;
        }else {
            myMap.put(qteOrCaValue, l ) ;
        }
    }

    public void writeTreeMaptoFile(BufferedWriter bufferedWriterMagazin, boolean writeAll) throws IOException {
        TreeMap<Double, String > map  = new TreeMap<>(myMap) ;
        if ( writeAll){
            for(Map.Entry<Double, String> e : map.descendingMap().entrySet()){
                writeEntry(bufferedWriterMagazin, e, Integer.MAX_VALUE);
            }
        }else {
            int i = 0 ;
            for(Map.Entry<Double, String> e : map.descendingMap().entrySet()){
                int length = e.getValue().split(";").length;
                if  ( (i + length) >= topN){
                    writeEntry(bufferedWriterMagazin, e , topN  - i  ) ;
                    break;
                }else writeEntry(bufferedWriterMagazin, e , topN);
                i = i + length ;
            }
        }
        bufferedWriterMagazin.close();
    }

    private void writeEntry(BufferedWriter bufferedWriterMagazin, Map.Entry<Double, String> e, int maxN) throws IOException {
        if (e.getValue().split(";").length >1 ){

            Arrays.stream(e.getValue().split(";")).limit(maxN).forEach(l-> {
                try {
                    bufferedWriterMagazin.write(l + "\n");

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        }
        else bufferedWriterMagazin.write(  e.getValue() + "\n");
    }

}
