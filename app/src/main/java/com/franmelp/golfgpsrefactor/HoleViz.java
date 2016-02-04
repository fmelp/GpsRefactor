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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private LocationManager locationManager;
    private Location currentLocation;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
//        setContentView(R.layout.activity_hole_viz);

        //figure out what hole it is
        Bundle b = getIntent().getExtras();
        if (b != null){
            int holeNum = b.getInt("hole_num");
            holeIdx = holeNum - 1;
        }

        //relative layout to hold everything together
        RelativeLayout frame = new RelativeLayout(context);
        frame.setLayoutParams(new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        int color = Integer.parseInt("32cd32", 16)+0xFF000000;
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
        holePic.setLayoutParams(holePicParams);
        holePic.setVisibility(View.VISIBLE);
        frame.addView(holePic);

        //Linear Layout to hold text
        LinearLayout textParent = new LinearLayout(context);
        RelativeLayout.LayoutParams textParentParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParentParams.addRule(RelativeLayout.ALIGN_LEFT, shim.getId());
        textParent.setLayoutParams(textParentParams);
        textParent.setOrientation(LinearLayout.VERTICAL);
        frame.addView(textParent);

//        TextView tv = new TextView(context);
//        tv.setText("from tee");
//        textParent.addView(tv);


        //set distance TextViews
        //and also locations in same loop
        latLongsHole = Model.LAT_LONGS.get(holeIdx);
        float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                18F, this.getApplicationContext().getResources().getDisplayMetrics());
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
                TextView hazHeader = setupTextView("HAZARDS: ", Color.RED, myTextSize);
                hazHeader.setGravity(Gravity.RIGHT);
                hazHeader.setTypeface(null, Typeface.BOLD);
                textParent.addView(hazHeader);

            }else if (i == size - 4){
                //set up to-green header
                textParent.addView(setupTextView("", Color.BLACK, myTextSize));
                TextView greenHeader = setupTextView("TO GREEN: ", Color.BLACK, myTextSize + 10);
                greenHeader.setGravity(Gravity.RIGHT);
                greenHeader.setTypeface(null, Typeface.BOLD);
                textParent.addView(greenHeader);
                //set front text box
                toFrontText = setupTextView("front: ", Color.BLACK, myTextSize+10);
                toFrontText.setGravity(Gravity.RIGHT);
                textParent.addView(toFrontText);
                //set location
                toFrontLoc = setupLocation(latLongsHole.get(i),
                        latLongsHole.get(i + 1));
            }else if (i == size - 2){
                //set back text box
                toBackText = setupTextView("back: ", Color.BLACK, myTextSize+10);
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
                TextView hazardText = setupTextView(hazNumString, Color.RED, myTextSize);
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

        //set up horiz linear layout for prev and next buttons
        LinearLayout buttonHolder = new LinearLayout(context);
        buttonHolder.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        buttonHolder.setOrientation(LinearLayout.HORIZONTAL);
        buttonHolder.setWeightSum(2);
        textParent.addView(buttonHolder);

        //button params to make them take up right amount of screen
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.weight = 1.0f;

        //Prev button
        Button prevButton = new Button(context);
        prevButton.setText("PREV");
        prevButton.setTextSize(myTextSize);
        prevButton.setLayoutParams(buttonParams);
        buttonHolder.addView(prevButton);
        //check it's not the 1st
        if (holeIdx != 0) {
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToPrevHole = new Intent(HoleViz.this, HoleViz.class);
                    goToPrevHole.putExtra("hole_num", holeIdx);
                    startActivity(goToPrevHole);
                    finish();
                }
            });
            //if it is the 1st no going back
        } else prevButton.setEnabled(false);

        //next button
        Button nextButton = new Button(context);
        nextButton.setText("NEXT");
        nextButton.setTextSize(myTextSize);
        nextButton.setLayoutParams(buttonParams);
        buttonHolder.addView(nextButton);
        //check its not the 18th
        if (holeIdx != 17){
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToNextHole = new Intent(HoleViz.this, HoleViz.class);
                    goToNextHole.putExtra("hole_num", holeIdx + 2);
                    startActivity(goToNextHole);
                    finish();
                }
            });
            //if it is no going forward
        }else prevButton.setEnabled(false);

        //main menu button
        Button mainMenuButton = new Button(context);
        mainMenuButton.setText("MAIN MENU");
        mainMenuButton.setTextSize(myTextSize);
        textParent.addView(mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainMenu = new Intent(HoleViz.this, HomeScreenActivity.class);
                startActivity(goToMainMenu);
            }
        });

        //finally puts all the layouts in main view
        setContentView(frame);
    }

    @Override
    protected void onResume(){
        super.onResume();
//        locationManager.getProvider(LocationManager.GPS_PROVIDER);
//        try{
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    500, 1, locationListener);
//
//        }catch(SecurityException e){
//
//        }
    }

    @Override
    protected void onPause(){
        super.onPause();

//        try{
//            locationManager.removeUpdates(locationListener);
//
//        }catch(SecurityException e){
//
//        }
        Drawable drawable = holePic.getDrawable(); // declare this ImageView Globally
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
            holePic.setImageBitmap(null);  // edited
        }
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



}
