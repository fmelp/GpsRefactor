package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.renderscript.ScriptGroup;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by francescomelpignano on 01/02/16.
 */
public class Model {

    public static ArrayList<ArrayList<Double>> LAT_LONGS;
    public static ArrayList<Integer> HOLE_IMAGE_REFS;
    public static boolean METERS;
    private static Context context;
    public static  ArrayList<String[]> HEADER_DETAILS;
    public static ArrayList<Polygon> POLYGONS;
    public static String CART_NUMBER;
    public static String CURRENT_LOC;
    public static boolean SIREN_BOOL;
    public static int HOLE_NUMBER;


    private static Model instance = null;
//    private Model("long_lat_sdg.txt", "hole_details.txt", "polys.geojson", HomeScreenActivity().getContext());




    private Model(String fileNameLatLong, String fileNameHeaderDetails,
                 String fileNamePolygons) {
        //add second file name
        //fill in header details by reading from hole_details.txt
        METERS = false;
        context = ContextInfo.context;
        HOLE_IMAGE_REFS = makeImageRefs();
        AssetManager mgr = context.getResources().getAssets();
        LAT_LONGS = readAsset(mgr, fileNameLatLong);
        HEADER_DETAILS = makeHeaderDetails(mgr, fileNameHeaderDetails);
        POLYGONS = readJSONFile(mgr, fileNamePolygons);
        //to be hardcoded for each cart
        CART_NUMBER = "1";
        //updated in HoleVIz every time location is changed
        CURRENT_LOC = "";
        //have a way to turn siren on and off
        SIREN_BOOL = true;
        //make it 0 to start with
        HOLE_NUMBER = 1;
    }

    public static Model getInstance() {
        if (instance == null){
            instance = new Model("long_lat_sdg.txt", "hole_details.txt", "polys.geojson");
        }
        return instance;
    }


    public static void changeSirenStatus(){
        if (SIREN_BOOL == false){
            SIREN_BOOL = true;
        }
        else if (SIREN_BOOL == true){
            SIREN_BOOL = false;
        }
    }

    public static void changeHoleNumber(int hole){
        HOLE_NUMBER = hole;
    }


    private static ArrayList<Integer> makeImageRefs(){
        ArrayList<Integer> imageRefs = new ArrayList<Integer>(18);
        for (int i = 0 ; i < 18; i++){
            int holeNum = i + 1;
            String fileName = "hole" + holeNum + "_rimpicc";
//            String fileName = "hole_" + holeNum;
            int holeId = context.getResources().getIdentifier(
                    fileName, "drawable", context.getPackageName());
            imageRefs.add(holeId);
        }
        return imageRefs;
    }

    private static ArrayList<String[]> makeHeaderDetails(AssetManager mgr, String path){
    //read from hole_details.txt
        // hole_number, par, handicap, white_tee, yel_tee, black_tee, red_tee
        InputStream is = null;
        BufferedReader reader = null;
        ArrayList<String[]> detailsHoles = new ArrayList<>(18);
        int i = 0;
        try{
            is = mgr.open(path);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] detailHole = line.split(",");
                detailsHoles.add(i, detailHole);
                i++;
            }
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
        return detailsHoles;
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

    private ArrayList<Polygon> readJSONFile(AssetManager mgr, String filename){
        ArrayList<Polygon> polygons = new ArrayList<Polygon>();
        try {
            InputStream is = mgr.open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject json = new JSONObject(new String(buffer, "UTF-8"));
            JSONArray features = json.getJSONArray("features");
//            System.out.println(features.toString());
            //iterate through each feature (polygon line set)
            //all the marked areas
            for (int i = 0; i < features.length(); i++){
                //find coordinates in json mess
                JSONArray coordinates = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
//                System.out.println(coordinates.toString());
                //make polygon to add
                Polygon.Builder polygonBuilder = Polygon.Builder();
                //iterate through all lines for the one polygon
                //make the polygon and add it to
                for (int j = 0; j < coordinates.length(); j++){
                    double x = coordinates.getJSONArray(j).getDouble(0);
                    double y = coordinates.getJSONArray(j).getDouble(1);
                    polygonBuilder.addVertex(new Point(x, y));
                }
                Polygon polygon = polygonBuilder.build();
                polygons.add(polygon);
//                Point p = new Point(17.397536, 40.879155);
//                System.out.println(polygon.contains(p));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        return polygons;
    }


}
