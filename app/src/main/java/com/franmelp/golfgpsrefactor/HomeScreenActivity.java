package com.franmelp.golfgpsrefactor;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class HomeScreenActivity extends AppCompatActivity {

    private static Button selectHoleButton;
    private static Button startOneButton;
    private static Button startTenButton;

    private static LinearLayout buttonHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        new SetVariables().execute();

        selectHoleButton = (Button) findViewById(R.id.allHoleViewerButton);
        startTenButton = (Button) findViewById(R.id.startTenButton);
        startOneButton = (Button) findViewById(R.id.startOneButton);

        buttonHolder = (LinearLayout) findViewById(R.id.mainLinLayout);

        float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                18F, this.getApplicationContext().getResources().getDisplayMetrics());

        int color = Integer.parseInt("32cd32", 16)+0xFF000000;

        for (int i = 0; i < buttonHolder.getChildCount(); i++){
            Button b = (Button) buttonHolder.getChildAt(i);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize+10);
            b.setTextColor(color);
        }

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
                Intent startOne = new Intent(HomeScreenActivity.this, HoleVizOne.class);
                startActivity(startOne);
                finish();

            }
        });

        startTenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startTen = new Intent(HomeScreenActivity.this, HoleVizTen.class);
                startActivity(startTen);
                finish();

            }
        });

        Button test = new Button(this);
        buttonHolder.addView(test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(HomeScreenActivity.this, HoleViz.class);
                start.putExtra("hole_num", 1);
                startActivity(start);
                finish();

            }
        });



    }

    private class SetVariables extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            Model model = new Model("long_lat_sdg.txt", getApplicationContext());
            return "aa";
        }
    }
}
