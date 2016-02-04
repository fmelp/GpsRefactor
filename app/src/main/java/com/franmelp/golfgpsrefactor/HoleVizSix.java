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
import android.widget.LinearLayout;
import android.widget.TextView;

public class HoleVizSix extends AppCompatActivity{

    private TextView hazTwoText;
    private TextView hazThreeText;
    private TextView hazFourText;
    private TextView hazFiveText;
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
    private Location hazFourLoc;
    private Location hazFiveLoc;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hole_viz_six);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        fromWhiteText = (TextView) findViewById(R.id.sixFromWhite);
        fromWhiteText.setText("white: ");
        fromWhiteLoc = new Location("");
        fromWhiteLoc.setLatitude(40.87500524);
        fromWhiteLoc.setLongitude(17.39843293);

        fromYelText = (TextView) findViewById(R.id.sixFromYellow);
        fromYelText.setText("yellow: ");
        fromYelLoc = new Location("");
        fromYelLoc.setLatitude(40.87526427);
        fromYelLoc.setLongitude(17.39849865);

        hazOneText = (TextView) findViewById(R.id.sixHazOne);
        hazOneText.setText("1.");
        hazOneLoc = new Location("");
        hazOneLoc.setLatitude(40.87681947);
        hazOneLoc.setLongitude(17.39774514);

        hazTwoText = (TextView) findViewById(R.id.sixHazTwo);
        hazTwoText.setText("2.");
        hazTwoLoc = new Location("");
        hazTwoLoc.setLatitude(40.87696763);
        hazTwoLoc.setLongitude(17.39770646);

        hazThreeText = (TextView) findViewById(R.id.sixHazThree);
        hazThreeText.setText("3.");
        hazThreeLoc = new Location("");
        hazThreeLoc.setLatitude(40.87710560);
        hazThreeLoc.setLongitude(17.39734808);

        hazFourText = (TextView) findViewById(R.id.sixHazFour);
        hazFourText.setText("4.");
        hazFourLoc = new Location("");
        hazFourLoc.setLatitude(40.87812470);
        hazFourLoc.setLongitude(17.39685346);

        hazFiveText = (TextView) findViewById(R.id.sixHazFive);
        hazFiveText.setText("5.");
        hazFiveLoc = new Location("");
        hazFiveLoc.setLatitude(40.87835765);
        hazFiveLoc.setLongitude(17.39656234);

        frontGreenText = (TextView) findViewById(R.id.sixFront);
        frontGreenText.setText("front:");
        frontGreenLoc = new Location("");
        frontGreenLoc.setLatitude(40.87879126);
        frontGreenLoc.setLongitude(17.39638930);

        backGreenText = (TextView) findViewById(R.id.sixBack);
        backGreenText.setText("back:");
        backGreenLoc = new Location("");
        backGreenLoc.setLatitude(40.87901557);
        backGreenLoc.setLongitude(17.39623589);

        prevButton = (Button) findViewById(R.id.sixPrev);
        nextButton = (Button) findViewById(R.id.sixNext);
        mainMenuButton = (Button) findViewById(R.id.sixMainMenu);


        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNextHole = new Intent(HoleVizSix.this, HoleVizFive.class);
                startActivity(goToNextHole);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNextHole = new Intent(HoleVizSix.this, HoleVizSeven.class);
                startActivity(goToNextHole);
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainMenu = new Intent(HoleVizSix.this, HomeScreenActivity.class);
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
                //hazFour
                String distHazFour = "4. " + calcDistance(hazFourLoc);
                hazFourText.setText(distHazFour);
                //hazFive
                String distHazFive = "5. " + calcDistance(hazFiveLoc);
                hazFiveText.setText(distHazFive);
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
