package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HoleViz extends AppCompatActivity {

    Context context;
    int holeIdx;

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
        ImageView holePic = new ImageView(context);
        holePic.setImageResource(Model.HOLE_IMAGE_REFS.get(holeIdx));
        RelativeLayout.LayoutParams holePicParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        holePicParams.addRule(RelativeLayout.ALIGN_RIGHT, shim.getId());
        System.out.println(shim.getId());
        holePic.setLayoutParams(holePicParams);
        holePic.setVisibility(View.VISIBLE);
        frame.addView(holePic);

        setContentView(frame);


        LinearLayout textParent = new LinearLayout(context);



    }
}
