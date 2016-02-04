package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HoleVizOne extends AppCompatActivity{

    private TextView hazOneText;
    private TextView fromWhiteText;
    private TextView fromYelText;
    private TextView frontGreenText;
    private TextView backGreenText;


    private LocationManager locationManager;
    private Location currentLocation;
    private LocationListener locationListener;

    private Location hazOneLoc;
    private Location frontGreenLoc;
    private Location backGreenLoc;
    private Location fromWhiteLoc;
    private Location fromYelLoc;

    private Button prevButton;
    private Button nextButton;
    private Button mainMenuButton;

    private LinearLayout textHolder;

//    private ImageView holePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hole_viz_one);
//        header = (ImageView) findViewById(R.id.headerTwo);
//        holePic = (ImageView) findViewById(R.id.oneHolePic);
//        loadBitmap(holePic, this.getApplicationContext(), R.drawable.hole1_rimpicc);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        fromWhiteText = (TextView) findViewById(R.id.oneFromWhite);
        fromWhiteText.setText("white: ");
        fromWhiteLoc = new Location("");
        fromWhiteLoc.setLatitude(40.87489667);
        fromWhiteLoc.setLongitude(17.39830026);

        fromYelText = (TextView) findViewById(R.id.oneFromYellow);
        fromYelText.setText("yellow: ");
        fromYelLoc = new Location("");
        fromYelLoc.setLatitude(40.87475550);
        fromYelLoc.setLongitude(17.39863454);

        hazOneText = (TextView) findViewById(R.id.oneHazOne);
        hazOneText.setText("1.");
        hazOneLoc = new Location("");
        hazOneLoc.setLatitude(40.87252846);
        hazOneLoc.setLongitude(17.40025690);


        frontGreenText = (TextView) findViewById(R.id.oneFront);
        frontGreenText.setText("front:");
        frontGreenLoc = new Location("");
        frontGreenLoc.setLatitude(40.87208871);
        frontGreenLoc.setLongitude(17.40004996);

        backGreenText = (TextView) findViewById(R.id.oneBack);
        backGreenText.setText("back:");
        backGreenLoc = new Location("");
        backGreenLoc.setLatitude(40.87181609);
        backGreenLoc.setLongitude(17.40003537);

        prevButton = (Button) findViewById(R.id.onePrev);
        nextButton = (Button) findViewById(R.id.oneNext);
        mainMenuButton = (Button) findViewById(R.id.oneMainMenu);

        prevButton.setEnabled(false);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNextHole = new Intent(HoleVizOne.this, HoleVizTwo.class);
                startActivity(goToNextHole);
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainMenu = new Intent(HoleVizOne.this, HomeScreenActivity.class);
                startActivity(goToMainMenu);
            }
        });


        textHolder = (LinearLayout) frontGreenText.getParent();

        float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                18F, this.getApplicationContext().getResources().getDisplayMetrics());

        int color = Integer.parseInt("32cd32", 16)+0xFF000000;

        for (int i = 0; i < textHolder.getChildCount(); i++){
            if (textHolder.getChildAt(i) instanceof TextView){
                TextView t = (TextView) textHolder.getChildAt(i);
                t.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize);
            }
//            Button b = (Button) textHolder.getChildAt(i);
//            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize+10);
//            b.setTextColor(color);
        }
        prevButton.setTextSize(myTextSize);
        nextButton.setTextSize(myTextSize);
        fromWhiteText.setTextColor(Color.WHITE);
        fromYelText.setTextColor(Color.YELLOW);




        locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                currentLocation = location;
                //hazOne
                String distHazOne = "1. " + calcDistance(hazOneLoc);
                hazOneText.setText(distHazOne);
                //frontGreen
                String distFront = "front: " + calcDistance(frontGreenLoc);
                frontGreenText.setText(distFront);
                //backGreen
                String distBack = "back: " + calcDistance(backGreenLoc);
                backGreenText.setText(distBack);
                //white tee
                String distWhite = "white: " + calcDistance(fromWhiteLoc);
                fromWhiteText.setText(distWhite);
                //yellow tee
                String distYel = "yellow: " + calcDistance(fromYelLoc);
                fromYelText.setText(distYel);

            }
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // NA
            }
            public void onProviderEnabled(String provider) {
                // NA
            }
            public void onProviderDisabled(String provider) {
                // NA
            }
        };

    }

    @Override
    protected void onResume(){
        super.onResume();
        locationManager.getProvider(LocationManager.GPS_PROVIDER);
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    500, 1, locationListener);

        }catch(SecurityException e){

        }

    }

    @Override
    protected void onPause(){
        super.onPause();

        try{
            locationManager.removeUpdates(locationListener);

        }catch(SecurityException e){

        }
    }

    private String calcDistance(Location location){
        int distanceMeters = java.lang.Math.round(currentLocation.distanceTo(location));
        return Integer.toString(distanceMeters);

    }

    private void loadBitmap(ImageView imageView, Context context, int resId){
        LoadHolePic loadPicTask = new LoadHolePic(imageView, context);
        loadPicTask.execute(resId);
    }
}




