package com.franmelp.golfgpsrefactor;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;


///FOR next devices remember to turn off auto-update in google play store
//autoupdate in settings about
//in apps go to google play services and turn off notifications

public class HomeScreenActivity extends AppCompatActivity {

    private static Button selectHoleButton;
    private static Button startOneButton;
    private static Button startTenButton;
    public Model model;


    private LinearLayout frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //get reference to holding frame for touch events
        frame = (LinearLayout) findViewById(R.id.mainLinLayoutHome);





    //size for text and green color for text
        float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                18F, this.getApplicationContext().getResources().getDisplayMetrics());
        myTextSize += 10.0;
        int color = Integer.parseInt("32cd32", 16)+0xFF000000;
//        int color = Integer.parseInt("2E7D32", 16) + 0xFF000000;

//        //initiate all the variable
//        //model that runs in background
//        new SetVariables().execute();
        //set up model
        model = new Model("long_lat_sdg.txt",
                    "hole_details.txt",
                    "polys.geojson",
                    getApplicationContext());

//        System.out.println(Model.POLYGONS.toString());

        //select meters or yards: text
        TextView selectText = (TextView) findViewById(R.id.selectText);
        selectText.setText("\nPlease select distance unit:");
        selectText.setTextSize(myTextSize);
        selectText.setTextColor(Color.BLUE);

        //peg unit variable to model
        //set to true in Model init
        Model.METERS = Model.METERS;

        //meters radio button
        final RadioButton meters = (RadioButton) findViewById(R.id.radio0);
        meters.setText("METERS");
        meters.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize);
        if (Model.METERS){
            meters.setTextColor(Color.BLUE);
            meters.setTypeface(null, Typeface.BOLD);
            meters.setChecked(true);
        }else{
            meters.setTextColor(Color.GRAY);
        }
        meters.setGravity(Gravity.CENTER);


        //yards radio button
        final RadioButton yards = (RadioButton) findViewById(R.id.radio1);
        yards.setText("YARDS");
        yards.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize);
        if (Model.METERS){
            yards.setTextColor(Color.GRAY);
        }else{
            yards.setTextColor(Color.BLUE);
            yards.setTypeface(null, Typeface.BOLD);
            yards.setChecked(true);
        }
        yards.setGravity(Gravity.CENTER);

        //selection showed in text
        final TextView check = (TextView) findViewById(R.id.selection);
        check.setTextSize(myTextSize - 10);
        check.setTextColor(Color.BLUE);
        check.setGravity(Gravity.CENTER);
//        if (Model.METERS){
//            check.setText("---meters selected---");
//        }else{
//            check.setText("---yards selected---");
//        }
//        check.setText("---meters selected---");
        check.setText("---meters selected---");
        if (!Model.METERS){
            check.setText("---yards selected---");
        }


        //handle radiogroup from meters yards
        //creates listener
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioG);
//         This overrides the radiogroup onCheckListener
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
                if (meters.isChecked()) {
                    Model.METERS = true;
                    check.setText("---meters selected---");
                    meters.setTextColor(Color.BLUE);
                    yards.setTypeface(null, Typeface.NORMAL);
                    meters.setTypeface(null, Typeface.BOLD);
                    yards.setTextColor(Color.GRAY);
                }
                if (yards.isChecked()) {
                    Model.METERS = false;
                    check.setText("---yards selected---");
                    yards.setTextColor(Color.BLUE);
                    yards.setTypeface(null, Typeface.BOLD);
                    meters.setTextColor(Color.GRAY);
                    meters.setTypeface(null, Typeface.NORMAL);
                }


            }
        });

        //Playing options text
        //comes before the buttons that start activities
        TextView playingOptionsText = (TextView) findViewById(R.id.playingOptionsText);
        playingOptionsText.setText("\n\nPlease select playing option:");
        playingOptionsText.setTextSize(myTextSize);
        playingOptionsText.setTextColor(color);


        //buttons for each start of activity
        //start from 1st
        startOneButton = (Button) findViewById(R.id.startOneButton);
        startOneButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize + 10);
        startOneButton.setTextColor(color);
        startOneButton.setTypeface(null, Typeface.BOLD);
        startOneButton.setSoundEffectsEnabled(false);
        //start from 10th
        startTenButton = (Button) findViewById(R.id.startTenButton);
        startTenButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize + 10);
        startTenButton.setTextColor(color);
        startTenButton.setTypeface(null, Typeface.BOLD);
        startTenButton.setSoundEffectsEnabled(false);
        //select which hole
        selectHoleButton = (Button) findViewById(R.id.allHoleViewerButton);
        selectHoleButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize + 10);
        selectHoleButton.setTextColor(color);
        selectHoleButton.setTypeface(null, Typeface.BOLD);
        selectHoleButton.setSoundEffectsEnabled(false);

        selectHoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAllHoles = new Intent(HomeScreenActivity.this, AllHoleViewer.class);
                startActivity(goToAllHoles);
                finish();
            }
        });

        startOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startOne = new Intent(HomeScreenActivity.this, HoleViz.class);
                startOne.putExtra("hole_num", 1);
                startActivity(startOne);
                finish();
            }
        });

        startTenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startTen = new Intent(HomeScreenActivity.this, HoleViz.class);
                startTen.putExtra("hole_num", 10);
                startActivity(startTen);
                finish();
            }
        });

        //go order food
        Button adminMenuButton = (Button) findViewById(R.id.foodPage);
        adminMenuButton.setTextSize(myTextSize);
        adminMenuButton.setTextColor(color);
        adminMenuButton.setText("FOOD");
        adminMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAdminMenu = new Intent(HomeScreenActivity.this, MenuOrder.class);
                startActivity(startAdminMenu);
                finish();
            }
        });


        setupAdminSwipe();

    }

//    private class SetVariables extends AsyncTask<String, Void, String>{
//        @Override
//        protected String doInBackground(String... params){
//            Model model = new Model("long_lat_sdg.txt",
//                    "hole_details.txt",
//                    "polys.geojson",
//                    getApplicationContext());
//            return "aa";
//        }
//        @Override
//        public void onPostExecute(String a) {
//
//        }
//    }

    //to access admin settings
    //swipe left 7 times to get to admin settings
    private void setupAdminSwipe(){
        frame.setOnTouchListener(new View.OnTouchListener() {

            int downX, upX;
            int pointCount = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pointCount = pointCount + event.getPointerCount();
                if (pointCount >= 7) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        downX = (int) event.getX();
                        Log.i("event.getX()", " downX " + downX);
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        upX = (int) event.getX();
                        Log.i("event.getX()", " upX " + downX);
                        if (downX - upX > -100 && downX - upX > 20) {
                            // swipe left
                            //check it's not 18th
                            Intent startAdminMenu = new Intent(HomeScreenActivity.this, AdminMenu.class);
                            startActivity(startAdminMenu);
                            finish();
                        }

                    }
                    return true;
                }
                return false;
            }

        });
    }



}
