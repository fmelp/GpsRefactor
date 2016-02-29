package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOError;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class HoleViz extends AppCompatActivity {

    Context context;

    int holeIdx;

    ArrayList<Double> latLongsHole;
    ArrayList<TextView> hazTexts;
    ArrayList<Location> hazLocs;

    TextView fromWhiteText;
    TextView fromYelText;
    TextView toFrontText;
    TextView toBackText;
    ImageView holePic;

    Location fromWhiteLoc;
    Location fromYelLoc;
    Location toFrontLoc;
    Location toBackLoc;

    RelativeLayout frame;

    boolean meters;

    ArrayList<Polygon> polygons;

//    AlarmController alarm;

    Alarm alarm;

    private GestureDetector gestureDetector;

    private LocationManager locationManager;
    private Location currentLocation;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        //check if it's in meters or yards
        meters = Model.METERS;

        //ref to polygons created in Model
        //for geofencing of restricted areas
        polygons = Model.POLYGONS;

        //set up alarm
//        alarm = new AlarmController(getApplicationContext());
        alarm = new Alarm(getApplicationContext(), "android.resource://" + getPackageName() + "/" + "raw/siren");

        //figure out what hole it is
        Bundle b = getIntent().getExtras();
        if (b != null){
            int holeNum = b.getInt("hole_num");
            holeIdx = holeNum - 1;
        }


        //use this instead of setContentView
        //couldn't use xml bc of regeneration for every hole
        //many instances variables set in here
        setupView();

        //set up swipe gestures
        //left == prev hole
        //right == next hole
        setupLeftRightSwipe();


        //init location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //location listener
        locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                currentLocation = location;
                //get doubles for comparing to polygons
                Double y = currentLocation.getLatitude();
                Double x = currentLocation.getLongitude();
                //point to compare
                Point point = new Point(x, y);

//                //hazOne
//                String distHazOne = "1. " + calcDistance(hazOneLoc);
//                hazOneText.setText(distHazOne);

                //handle all hazards
                for (int i = 0; i < hazLocs.size(); i++){
                    int hazNum = i+1;
                    String distHaz = hazNum + ". " + calcDistance(hazLocs.get(i), meters);
                    hazTexts.get(i).setText(distHaz);
                }

                //frontGreen
                String distFront = "front: " + calcDistance(toFrontLoc, meters);
                toFrontText.setText(distFront);
                //backGreen
                String distBack = "back: " + calcDistance(toBackLoc, meters);
                toBackText.setText(distBack);
                //white tee
                String distWhite = "white: " + calcDistance(fromWhiteLoc, meters);
                fromWhiteText.setText(distWhite);
                //yellow tee
                String distYel = "yellow: " + calcDistance(fromYelLoc, meters);
                fromYelText.setText(distYel);

                //loop through polygons and check if point is in there
                for (int i = 0; i < polygons.size(); i++){
                    Polygon polygon = polygons.get(i);
//                    if (polygon.contains(point)){
////                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
////                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 400);
////                        Uri path = Uri.parse("android.resource://com.franmelp.golfgpsrefactor/raw/siren.mp3");
//                        alarm.playSound("android.resource://" + getPackageName() + "/" + "raw/siren");
//                        Toast.makeText(getApplicationContext(), "TORNA IN FAIRWAY STRONZO!",
//                                Toast.LENGTH_SHORT).show();
//                    }else{
////                        alarm.playSound("android.resource://" + getPackageName() + "/" + "raw/empty");
//                        alarm.mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, AudioManager.FLAG_PLAY_SOUND);
//
//
//                    }
                    if (polygon.contains(point)){
                        alarm.play();
                        Toast.makeText(getApplicationContext(), "TORNA IN FAIRWAY STRONZO!",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        alarm.pause();
                    }

                }

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
        //see if meters was updated
        meters = Model.METERS;

        //reload image
        loadBitmap(holePic, context, Model.HOLE_IMAGE_REFS.get(holeIdx));

        //reload alarm
        alarm = new Alarm(getApplicationContext(), "android.resource://" + getPackageName() + "/" + "raw/siren");
    }

    @Override
    protected void onPause(){
        super.onPause();

        try{
            locationManager.removeUpdates(locationListener);

        }catch(SecurityException e){

        }

    }

    @Override
    protected  void onStop(){
        super.onStop();
//        free up image resource space
        Drawable drawable = holePic.getDrawable(); // declare this ImageView Globally
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
            //next line makes app crash every time
//            holePic.setImageBitmap(null);  // edited
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()){
                bitmap.recycle();
            }

//            next line makes app crash every time
            holePic.setImageBitmap(null);  // edited
        }

        //release media player
        alarm.releasePlayer();
    }

    private TextView setupTextView(String s, int color, float textSize){
        TextView text = new TextView(context);
        text.setText(s);
        text.setTextColor(color);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return text;
    }

    private Location setupLocation(Double lat, Double lng){
        Location loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lng);
        return loc;
    }

    private void loadBitmap(ImageView imageView, Context context, int resId){
        LoadHolePic loadPicTask = new LoadHolePic(imageView, context);
        loadPicTask.execute(resId);
    }

    private void setupView(){

        //text size for tablet
        float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                18F, this.getApplicationContext().getResources().getDisplayMetrics());
        myTextSize += 10.0;

        //relative layout to hold everything together
        frame = new RelativeLayout(context);
        frame.setLayoutParams(new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
//        int color = Integer.parseInt("32cd32", 16)+0xFF000000;
        int color = Integer.parseInt("4D4D4D", 16) + 0xFF000000;
//        int color = Integer.parseInt("2E7D32", 16) + 0xFF000000;
        frame.setId(frame.generateViewId());
        frame.setBackgroundColor(color);



        // SHIM to align hole pic to left and rest to right
        View shim = new View(context);
        RelativeLayout.LayoutParams shimParams = new RelativeLayout.LayoutParams(
                0,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        shimParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        shim.setVisibility(View.INVISIBLE);
        shim.setLayoutParams(shimParams);
        shim.setId(shim.generateViewId());
        frame.addView(shim);

        //custom header
        LinearLayout headerHolder = new LinearLayout(context);
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerHolder.setLayoutParams(headerParams);
        headerHolder.setId(headerHolder.generateViewId());
        headerHolder.setGravity(Gravity.CENTER);
        headerHolder.setOrientation(LinearLayout.VERTICAL);
//        headerHolder.setBackgroundColor(Integer.parseInt("4D4D4D", 16) + 0xFF000000);
        headerHolder.setBackgroundColor(Color.GRAY);
        frame.addView(headerHolder);

        //par, hole num, handicap holder
        LinearLayout detailHolder = new LinearLayout(context);
        RelativeLayout.LayoutParams detailParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        detailHolder.setLayoutParams(detailParams);
        detailHolder.setGravity(Gravity.CENTER);
        detailHolder.setOrientation(LinearLayout.HORIZONTAL);
        headerHolder.addView(detailHolder);

        //hole details String[] : hole_num, par, handicap, w, y, b, r
        String[] holeDetails = Model.HEADER_DETAILS.get(holeIdx);
        String holeNum = holeDetails[0];
        String par = holeDetails[1];
        String hcp = holeDetails[2];
        String wt = "";
        String yt = "";
        String bt = "";
        String rt = "";
        if (meters){
            wt = holeDetails[3] + "m";
            yt = holeDetails[4] + "m";
            bt = holeDetails[5] + "m";
            rt = holeDetails[6] + "m";
        }else{
            //convert to yards if meters is false
            int wtM = Integer.parseInt(holeDetails[3].replaceAll("\\s+",""));
            wt = Integer.toString((int) ((wtM / 0.9144) + 0.5)) + "y";
            int ytM = Integer.parseInt(holeDetails[4].replaceAll("\\s+",""));
            yt = Integer.toString((int) ((ytM / 0.9144) + 0.5)) + "y";
            int btM = Integer.parseInt(holeDetails[5].replaceAll("\\s+",""));
            bt = Integer.toString((int) ((btM / 0.9144) + 0.5)) + "y";
            int rtM = Integer.parseInt(holeDetails[6].replaceAll("\\s+",""));
            rt = Integer.toString((int) ((rtM / 0.9144) + 0.5)) + "y";
        }


        //par
        TextView parText = setupTextView("PAR" + par + "           ",
                Color.RED, myTextSize+10);
        detailHolder.addView(parText);

        //hole num
        TextView holeNumText = setupTextView(holeNum,
                Color.RED, myTextSize+40);
        holeNumText.setTypeface(null, Typeface.BOLD);
        detailHolder.addView(holeNumText);

        //handicap
        TextView handicapText = setupTextView("           HCP " + hcp,
                Color.RED, myTextSize+10);
        detailHolder.addView(handicapText);

        //meters from tee holder
        LinearLayout metersHolder = new LinearLayout(context);
        RelativeLayout.LayoutParams metersParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        metersHolder.setLayoutParams(metersParams);
        metersHolder.setGravity(Gravity.CENTER);
        metersHolder.setOrientation(LinearLayout.HORIZONTAL);
        headerHolder.addView(metersHolder);

        //m white
        TextView whiteText = setupTextView(wt + "         ", Color.WHITE, myTextSize);
        metersHolder.addView(whiteText);

        //m white
        TextView yelText = setupTextView(yt + "         ", Color.YELLOW, myTextSize);
        metersHolder.addView(yelText);

        //m white
        TextView blackText = setupTextView(bt + "         ", Color.BLACK, myTextSize);
        metersHolder.addView(blackText);

        //m white
        TextView redText = setupTextView(rt, Color.RED, myTextSize);
        metersHolder.addView(redText);





        //load hole pic
        holePic = new ImageView(context);
//        holePic.setImageResource(Model.HOLE_IMAGE_REFS.get(holeIdx));
        loadBitmap(holePic, context, Model.HOLE_IMAGE_REFS.get(holeIdx));
//        holePic.setImageBitmap(
//                decodeSampledBitmapFromResource(
//                        getResources(),
//                        Model.HOLE_IMAGE_REFS.get(holeIdx),
//                        1000,
//                        2000));
        RelativeLayout.LayoutParams holePicParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        holePicParams.addRule(RelativeLayout.ALIGN_RIGHT, shim.getId());
        holePicParams.addRule(RelativeLayout.BELOW, headerHolder.getId());
        holePic.setLayoutParams(holePicParams);
        holePic.setVisibility(View.VISIBLE);
//        holePic.setScaleType(ImageView.ScaleType.FIT_XY);
        frame.addView(holePic);


        //Relative Layout for right side of screen
        RelativeLayout rightFrame = new RelativeLayout(context);
        RelativeLayout.LayoutParams rightFrameParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rightFrameParams.addRule(RelativeLayout.ALIGN_LEFT, shim.getId());
        rightFrameParams.addRule(RelativeLayout.BELOW, headerHolder.getId());
        rightFrame.setLayoutParams(rightFrameParams);
        frame.addView(rightFrame);


        //Linear Layout to hold text
        LinearLayout textParent = new LinearLayout(context);
        RelativeLayout.LayoutParams textParentParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParentParams.addRule(RelativeLayout.ALIGN_LEFT, shim.getId());
        textParentParams.addRule(RelativeLayout.BELOW, headerHolder.getId());

        textParent.setLayoutParams(textParentParams);
        textParent.setOrientation(LinearLayout.VERTICAL);

        rightFrame.addView(textParent);

//        TextView tv = new TextView(context);
//        tv.setText("from tee");
//        textParent.addView(tv);


        //set distance TextViews
        //and also locations in same loop
        latLongsHole = Model.LAT_LONGS.get(holeIdx);
        int size = latLongsHole.size();
        int numHazards = (size - 8) / 2;
        hazTexts = new ArrayList<>(numHazards);
        hazLocs = new ArrayList<>(numHazards);
        for (int i = 0; i < size - 1; i += 2){
            if (i == 0){
                //header text tee
                TextView teeHeader = setupTextView("FROM TEE: ", Color.BLACK, myTextSize);
                teeHeader.setTypeface(null, Typeface.BOLD);
                teeHeader.setGravity(Gravity.RIGHT);
                textParent.addView(teeHeader);
                //set white text box
                fromWhiteText = setupTextView("white: ", Color.WHITE, myTextSize);
                fromWhiteText.setGravity(Gravity.RIGHT);
                textParent.addView(fromWhiteText);
                //set location
                fromWhiteLoc = setupLocation(latLongsHole.get(i),
                        latLongsHole.get(i + 1));
            }else if (i == 2){
                //set yellow text box
                fromYelText = setupTextView("yellow: ", Color.YELLOW, myTextSize);
                fromYelText.setGravity(Gravity.RIGHT);
                textParent.addView(fromYelText);
                //set location
                fromYelLoc = setupLocation(latLongsHole.get(i),
                        latLongsHole.get(i + 1));
                //set up hazard header
                textParent.addView(setupTextView("", Color.BLACK, myTextSize));
                TextView hazHeader = setupTextView("HAZARDS: ", Color.CYAN, myTextSize);
                hazHeader.setGravity(Gravity.RIGHT);
                hazHeader.setTypeface(null, Typeface.BOLD);
                textParent.addView(hazHeader);

            }else if (i == size - 4){
                //set up to-green header
                textParent.addView(setupTextView("", Color.BLACK, myTextSize));
                TextView greenHeader = setupTextView("TO GREEN: ", Color.WHITE, myTextSize + 10);
                greenHeader.setGravity(Gravity.RIGHT);
                greenHeader.setTypeface(null, Typeface.BOLD);
                textParent.addView(greenHeader);
                //set front text box
                toFrontText = setupTextView("front: ", Color.WHITE, myTextSize+10);
                toFrontText.setGravity(Gravity.RIGHT);
                textParent.addView(toFrontText);
                //set location
                toFrontLoc = setupLocation(latLongsHole.get(i),
                        latLongsHole.get(i + 1));
            }else if (i == size - 2){
                //set back text box
                toBackText = setupTextView("back: ", Color.WHITE, myTextSize+10);
                toBackText.setGravity(Gravity.RIGHT);
                textParent.addView(toBackText);
                textParent.addView(setupTextView("", Color.BLACK, myTextSize));
                //set location
                toBackLoc = setupLocation(latLongsHole.get(i),
                        latLongsHole.get(i + 1));

            }else{
                //set up text view for hazard
                //and add it to list for processing in location listener
                int hazNum = (i / 2) - 1;
                String hazNumString = Integer.toString(hazNum) + ". ";
                TextView hazardText = setupTextView(hazNumString, Color.CYAN, myTextSize);
                hazardText.setGravity(Gravity.RIGHT);
                textParent.addView(hazardText);
                hazTexts.add(hazardText);
                //set up location for hazard
                //add it to list for processing in location listener
                Location hazLoc = setupLocation(latLongsHole.get(i),
                        latLongsHole.get(i + 1));
                hazLocs.add(hazLoc);
            }
        }


        //helper relative layout to keep buttons at bottom right of screen
        //had to use this bc impossible to
        // stretch textParent linear layout to bottom of screen
        RelativeLayout allButtonsBottomHolder = new RelativeLayout(context);
        RelativeLayout.LayoutParams allButtonsBottomHolderParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        allButtonsBottomHolderParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        allButtonsBottomHolder.setLayoutParams(allButtonsBottomHolderParams);
        rightFrame.addView(allButtonsBottomHolder);

        //set up vertical linear layout to hold all buttons
        // [main  menu]
        // [prev][next]
        LinearLayout allButtonsLinear = new LinearLayout(context);
        allButtonsLinear.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        allButtonsLinear.setOrientation(LinearLayout.VERTICAL);
        allButtonsBottomHolder.addView(allButtonsLinear);



        //main menu button
        //to be above next and prev buttons
        Button mainMenuButton = new Button(context);
        mainMenuButton.setBackgroundColor(Integer.parseInt("2E7D32", 16) + 0xFF000000);
        mainMenuButton.setText("MAIN MENU");
        mainMenuButton.setSoundEffectsEnabled(false);
        mainMenuButton.setTextSize(myTextSize + 10);
        RelativeLayout.LayoutParams mainMenuButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        allButtonsLinear.addView(mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainMenu = new Intent(HoleViz.this, HomeScreenActivity.class);
                startActivity(goToMainMenu);
            }
        });

        //empty text view to make space inbetween main and prev/next buttons
        TextView space = setupTextView("", Color.BLACK, 15);
        allButtonsLinear.addView(space);

        //set up horiz linear layout for prev and next buttons
        LinearLayout buttonHolder = new LinearLayout(context);
        buttonHolder.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        buttonHolder.setOrientation(LinearLayout.HORIZONTAL);
        buttonHolder.setWeightSum(2);
        buttonHolder.setId(buttonHolder.generateViewId());
        allButtonsLinear.addView(buttonHolder);


        //button params to make them take up right amount of screen
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.weight = 1.0f;

        //Prev button
        Button prevButton = new Button(context);
        prevButton.setText("PREV");
        prevButton.setSoundEffectsEnabled(false);
        prevButton.setBackgroundColor(Integer.parseInt("2E7D32", 16) + 0xFF000000);
        prevButton.setTextSize(myTextSize+10);
        prevButton.setLayoutParams(buttonParams);
        buttonHolder.addView(prevButton);
        //check it's not the 1st
        if (holeIdx != 0) {
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent goToPrevHole = new Intent(HoleViz.this, HoleViz.class);
//                    goToPrevHole.putExtra("hole_num", holeIdx);
////                    goToPrevHole.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(goToPrevHole);
//                    finish();
                    goToPrevHole();
                }
            });
            //if it is the 1st no going back
        } else prevButton.setEnabled(false);

        //next button
        Button nextButton = new Button(context);
        nextButton.setText("NEXT");
        nextButton.setSoundEffectsEnabled(false);
        nextButton.setBackgroundColor(Integer.parseInt("2E7D32", 16) + 0xFF000000);
        nextButton.setTextSize(myTextSize+10);
        nextButton.setLayoutParams(buttonParams);
        buttonHolder.addView(nextButton);
        //check its not the 18th
        if (holeIdx != 17){
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent goToNextHole = new Intent(HoleViz.this, HoleViz.class);
//                    goToNextHole.putExtra("hole_num", holeIdx + 2);
////                    goToNextHole.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(goToNextHole);
//                    finish();
                    goToNextHole();
                }
            });
            //if it is no going forward
        }else nextButton.setEnabled(false);


        //finally puts all the layouts in main view
        setContentView(frame);
    }

    private void goToPrevHole(){
        Intent goToPrevHole = new Intent(HoleViz.this, HoleViz.class);
        goToPrevHole.putExtra("hole_num", holeIdx);
        startActivity(goToPrevHole);
        finish();
    }

    private void goToNextHole(){
        Intent goToNextHole = new Intent(HoleViz.this, HoleViz.class);
        goToNextHole.putExtra("hole_num", holeIdx + 2);
        startActivity(goToNextHole);
        finish();
    }

    private void setupLeftRightSwipe(){
        frame.setOnTouchListener(new View.OnTouchListener() {

            int downX, upX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getX();
                    Log.i("event.getX()", " downX " + downX);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    upX = (int) event.getX();
                    Log.i("event.getX()", " upX " + downX);
                    if (upX - downX > 100) {
                        //swipe right
                        //check it's not 1st
                        if (holeIdx != 0) {
                            goToPrevHole();
                        }
                    } else if (downX - upX > -100 && downX - upX > 20) {
                        // swipe left
                        //check it's not 18th
                        if (holeIdx != 17) {
                            goToNextHole();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private String calcDistance(Location location){
        int distanceMeters = java.lang.Math.round(currentLocation.distanceTo(location));
        return Integer.toString(distanceMeters);

    }

    private String calcDistance(Location location, boolean meters){
        int distance = 0;
        String ret = "";
        if (!meters){
            distance = (int) ((currentLocation.distanceTo(location) / 0.9144) + 0.5);
            ret = Integer.toString(distance) + "y";

        }else{
            distance = java.lang.Math.round(currentLocation.distanceTo(location));
            ret = Integer.toString(distance) + "m";
        }
        return ret;

    }



}
