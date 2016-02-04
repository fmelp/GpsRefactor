package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by francescomelpignano on 01/02/16.
 */
public class Model {

    public static ArrayList<ArrayList<Double>> LAT_LONGS;
    public static ArrayList<Integer> HOLE_IMAGE_REFS;
    public static boolean METERS;
    private static Context context;


    public Model(String fileName, Context contextIn) {
        METERS = true;
        context = contextIn;
        HOLE_IMAGE_REFS = makeImageRefs();
        AssetManager mgr = context.getResources().getAssets();
        LAT_LONGS = readAsset(mgr, fileName);

    }

    private static ArrayList<Integer> makeImageRefs(){
        ArrayList<Integer> imageRefs = new ArrayList<Integer>(18);
        for (int i = 0 ; i < 18; i++){
            int holeNum = i + 1;
            String fileName = "hole" + holeNum + "_rimpicc";
            int holeId = context.getResources().getIdentifier(
                    fileName, "drawable", context.getPackageName());
            imageRefs.add(holeId);
        }
        return imageRefs;
    }



    /*
    format of data:
        white tee
        yellow tee
        haz_1
         .
         .
         .
        haz_n
        front of green
        back of green
        -----blank line----
     */
    private static ArrayList<ArrayList<Double>> readAsset(AssetManager mgr, String path) {
        InputStream is = null;
        BufferedReader reader = null;
        ArrayList<ArrayList<Double>> longLats = new ArrayList<ArrayList<Double>>(18);
        try {
            is = mgr.open(path);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            int i = 0;
            ArrayList<Double> longLatHole = new ArrayList<Double>(30);
            while ((line = reader.readLine()) != null) {
                if (!"".equals(line)){
                    String[] latLong = line.split(",");
                    String lat = latLong[0];
                    String lng = latLong[1];
                    longLatHole.add(Double.parseDouble(lat.substring(0, lat.length() - 2)));
                    longLatHole.add(Double.parseDouble(lng.substring(0, lng.length() - 2)));
                } else {
                    longLats.add(i, longLatHole);
                    i++;
                    longLatHole = new ArrayList<Double>(30);
                }


            }
//            System.out.println(longLats.toString());
//            System.out.println(longLats.size());
//            System.out.println("-----------------------------------------");
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
//        for (ArrayList<Double> l : longLats){
//            System.out.println(l.size());
//        }
        return longLats;
    }


}
