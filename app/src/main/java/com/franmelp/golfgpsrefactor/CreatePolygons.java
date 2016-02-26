package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by francescomelpignano on 26/02/16.
 */
public class CreatePolygons {

    public static ArrayList<Polygon> POLYGONS;
    private static Context context;

    public CreatePolygons(String filename, Context contextIn){
        context = contextIn;
        AssetManager mgr = context.getResources().getAssets();
//        String s = readPolys(filename, mgr);
//        System.out.println("------------------------------------------------------");
        POLYGONS = readJSONFile(mgr, filename);

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
