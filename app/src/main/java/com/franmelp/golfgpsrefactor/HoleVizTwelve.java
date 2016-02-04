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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HoleVizTwelve extends AppCompatActivity{

    private TextView hazTwoText;
    private TextView hazThreeText;
    private TextView hazOneText;
    private TextView fromWhiteText;
    private TextView fromYelText;
    private TextView frontGreenText;
    private TextView backGreenText;


    private LocationManager locationManager;
    private Location currentLocation;
    private LocationListener locationListener;

    private Location hazTwoLoc;
    private Location hazThreeLoc;
    private Location hazOneLoc;
    private Location frontGreenLoc;
    private Location backGreenLoc;
    private Location fromWhiteLoc;
    private Location fromYelLoc;

    private Button prevButton;
    private Button nextButton;
    private Button mainMenuButton;

    private LinearLayout textHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hole_viz_twelve);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        fromWhiteText = (TextView) findViewById(R.id.twelveFromWhite);
        fromWhiteText.setText("white: ");
        fromWhiteLoc = new Location("");
        fromWhiteLoc.setLatitude(40.87952268);
        fromWhiteLoc.setLongitude(17.39940163);

        fromYelText = (TextView) findViewById(R.id.twelveFromYellow);
        fromYelText.setText("yellow: ");
        fromYelLoc = new Location("");
        fromYelLoc.setLatitude(40.87934828);
        fromYelLoc.setLongitude(17.39946736);

        hazOneText = (TextView) findViewById(R.id.twelveHazOne);
        hazOneText.setText("1.");
        hazOneLoc = new Location("");
        hazOneLoc.setLatitude(40.87824091);
        hazOneLoc.setLongitude(17.40089139);

        hazTwoText = (TextView) findViewById(R.id.twelveHazTwo);
        hazTwoText.setText("2.");
        hazTwoLoc = new Location("");
        hazTwoLoc.setLatitude(40.87766247);
        hazTwoLoc.setLongitude(17.40124963);

        hazThreeText = (TextView) findViewById(R.id.twelveHazThree);
        hazThreeText.setText("3.");
        hazThreeLoc = new Location("");
        hazThreeLoc.setLatitude(40.87780698);
        hazThreeLoc.setLongitude(17.40158571);

        frontGreenText = (TextView) findViewById(R.id.twelveFront);
        frontGreenText.setText("front:");
        frontGreenLoc = new Location("");
        frontGreenLoc.setLatitude(40.87765250);
        frontGreenLoc.setLongitude(17.40174994);

        backGreenText = (TextView) findViewById(R.id.twelveBack);
        backGreenText.setText("back:");
        backGreenLoc = new Location("");
        backGreenLoc.setLatitude(40.87753371);
        backGreenLoc.setLongitude(17.40211660);

        prevButton = (Button) findViewById(R.id.twelvePrev);
        nextButton = (Button) findViewById(R.id.twelveNext);
        mainMenuButton = (Button) findViewById(R.id.twelveMainMenu);


        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNextHole = new Intent(HoleVizTwelve.this, HoleVizEleven.class);
                startActivity(goToNextHole);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNextHole = new Intent(HoleVizTwelve.this, HoleVizThirteen.class);
                startActivity(goToNextHole);
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainMenu = new Intent(HoleVizTwelve.this, HomeScreenActivity.class);
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
                //hazTwo
                String distHazTwo = "2. " + calcDistance(hazTwoLoc);
                hazTwoText.setText(distHazTwo);
                //hazThree
                String distHazThree = "3. " + calcDistance(hazThreeLoc);
                hazThreeText.setText(distHazThree);
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

}
