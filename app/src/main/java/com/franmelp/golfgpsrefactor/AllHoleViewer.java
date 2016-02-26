package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.ConnectException;


public class AllHoleViewer extends AppCompatActivity {


    private static LinearLayout buttonHolderFront;
    private static LinearLayout buttonHolderBack;
    private static RelativeLayout frame;
    private int holeNum;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        buttonHolderFront = new LinearLayout(context);

        //text size and color
        float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                18F, context.getResources().getDisplayMetrics());
//        myTextSize += 10.0;
        int color = Integer.parseInt("32cd32", 16)+0xFF000000;

//        for (int i = 0; i < buttonHolder.getChildCount(); i++){
//            Button b = (Button) buttonHolder.getChildAt(i);
//            b.setTextSize(TypedValue.COMPLEX_UNIT_SP,myTextSize+10);
//            b.setTextColor(color);
//
//        }

        //relative layout to hold everything together
        frame = new RelativeLayout(context);
        frame.setLayoutParams(new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
//        int color = Integer.parseInt("32cd32", 16)+0xFF000000;
//        int color = Integer.parseInt("1B5E20", 16) + 0xFF000000;
        int colorBack = Integer.parseInt("2E7D32", 16) + 0xFF000000;
        frame.setId(frame.generateViewId());
        frame.setBackgroundColor(colorBack);

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

        //Linear Layout for right side (back nine)
        buttonHolderBack = new LinearLayout(context);
        RelativeLayout.LayoutParams buttonHolderBackParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonHolderBackParams.addRule(RelativeLayout.ALIGN_LEFT, shim.getId());
        buttonHolderBack.setOrientation(LinearLayout.VERTICAL);
        buttonHolderBack.setLayoutParams(buttonHolderBackParams);
        frame.addView(buttonHolderBack);

        //Linear Layout for left side (front nine)
        buttonHolderFront = new LinearLayout(context);
        RelativeLayout.LayoutParams buttonHolderFrontParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonHolderFrontParams.addRule(RelativeLayout.ALIGN_RIGHT, shim.getId());
        buttonHolderFront.setOrientation(LinearLayout.VERTICAL);
        buttonHolderFront.setLayoutParams(buttonHolderFrontParams);
        frame.addView(buttonHolderFront);

////        space to make it seems more even
////        added to both front and back view
//        TextView space = new TextView(context);
//        space.setText("");
//        space.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize);
//        buttonHolderBack.addView(space);
//        buttonHolderFront.addView(space);

        for (int i = 1; i < 19; i++){
            Button holeButton = new Button(context);
            final int hole = i;
            String holeNumS = Integer.toString(i);
            holeButton.setText(holeNumS);
            holeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize + 54);
            holeButton.setTextColor(color);
            holeButton.setVisibility(Button.VISIBLE);
            if (i < 10){
                buttonHolderFront.addView(holeButton);
            }else{
                buttonHolderBack.addView(holeButton);
            }

            holeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToHole(hole);
                }
            });
        }

        setContentView(frame);


    }
    private void goToHole(int holeNum){
        Intent goToHole = new Intent(AllHoleViewer.this, HoleViz.class);
        goToHole.putExtra("hole_num", holeNum);
        startActivity(goToHole);
        finish();
    }
}
