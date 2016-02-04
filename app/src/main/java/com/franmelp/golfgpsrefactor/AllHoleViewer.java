package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class AllHoleViewer extends AppCompatActivity {

    private static Button buttonOne;
    private static Button buttonTwo;
    private static Button buttonThree;
    private static Button buttonFour;
    private static Button buttonFive;
    private static Button buttonSix;
    private static Button buttonSeven;
    private static Button buttonEight;
    private static Button buttonNine;
    private static Button buttonTen;
    private static Button buttonEleven;
    private static Button buttonTwelve;
    private static Button buttonThirteen;
    private static Button buttonFourteen;
    private static Button buttonFifteen;
    private static Button buttonSixteen;
    private static Button buttonSeventeen;
    private static Button buttonEighteen;

    private static LinearLayout buttonHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_hole_viewer);

        buttonOne = (Button) findViewById(R.id.buttonOne);
        buttonTwo = (Button) findViewById(R.id.buttonTwo);
        buttonThree = (Button) findViewById(R.id.buttonThree);
        buttonFour = (Button) findViewById(R.id.buttonFour);
        buttonFive = (Button) findViewById(R.id.buttonFive);
        buttonSix = (Button) findViewById(R.id.buttonSix);
        buttonSeven = (Button) findViewById(R.id.buttonSeven);
        buttonEight = (Button) findViewById(R.id.buttonEight);
        buttonNine = (Button) findViewById(R.id.buttonNine);
        buttonTen = (Button) findViewById(R.id.buttonTen);
        buttonEleven = (Button) findViewById(R.id.buttonEleven);
        buttonTwelve = (Button) findViewById(R.id.buttonTwelve);
        buttonThirteen = (Button) findViewById(R.id.buttonThirteen);
        buttonFourteen = (Button) findViewById(R.id.buttonFourteen);
        buttonFifteen = (Button) findViewById(R.id.buttonFifteen);
        buttonSixteen = (Button) findViewById(R.id.buttonSixteen);
        buttonSeventeen = (Button) findViewById(R.id.buttonSeventeen);
        buttonEighteen = (Button) findViewById(R.id.buttonEigtheen);

        buttonHolder = (LinearLayout) findViewById(R.id.allHoleHolder);
        Context context = this.getApplicationContext();

        float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18F, context.getResources().getDisplayMetrics());

        int color = Integer.parseInt("32cd32", 16)+0xFF000000;

        for (int i = 0; i < buttonHolder.getChildCount(); i++){
            Button b = (Button) buttonHolder.getChildAt(i);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP,myTextSize+10);
            b.setTextColor(color);

        }

        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleOne = new Intent(AllHoleViewer.this, HoleVizOne.class);
                startActivity(goToHoleOne);

            }
        });

        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleTwo = new Intent(AllHoleViewer.this, HoleVizTwo.class);
                startActivity(goToHoleTwo);

            }
        });

        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleThree = new Intent(AllHoleViewer.this, HoleVizThree.class);
                startActivity(goToHoleThree);

            }
        });

        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleFour = new Intent(AllHoleViewer.this, HoleVizFour.class);
                startActivity(goToHoleFour);

            }
        });

        buttonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleFive = new Intent(AllHoleViewer.this, HoleVizFive.class);
                startActivity(goToHoleFive);

            }
        });

        buttonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleSix = new Intent(AllHoleViewer.this, HoleVizSix.class);
                startActivity(goToHoleSix);

            }
        });

        buttonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleSeven = new Intent(AllHoleViewer.this, HoleVizSeven.class);
                startActivity(goToHoleSeven);

            }
        });


        buttonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleEight = new Intent(AllHoleViewer.this, HoleVizEight.class);
                startActivity(goToHoleEight);

            }
        });


        buttonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleNine = new Intent(AllHoleViewer.this, HoleVizNine.class);
                startActivity(goToHoleNine);

            }
        });


        buttonTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleTen = new Intent(AllHoleViewer.this, HoleVizTen.class);
                startActivity(goToHoleTen);

            }
        });


        buttonEleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleEleven = new Intent(AllHoleViewer.this, HoleVizEleven.class);
                startActivity(goToHoleEleven);

            }
        });


        buttonTwelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleTwelve = new Intent(AllHoleViewer.this, HoleVizTwelve.class);
                startActivity(goToHoleTwelve);

            }
        });


        buttonThirteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleThirteen = new Intent(AllHoleViewer.this, HoleVizThirteen.class);
                startActivity(goToHoleThirteen);

            }
        });


        buttonFourteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleFourteen = new Intent(AllHoleViewer.this, HoleVizFourteen.class);
                startActivity(goToHoleFourteen);

            }
        });


        buttonFifteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleFifteen = new Intent(AllHoleViewer.this, HoleVizFifteen.class);
                startActivity(goToHoleFifteen);

            }
        });


        buttonSixteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleSixteen = new Intent(AllHoleViewer.this, HoleVizSixteen.class);
                startActivity(goToHoleSixteen);

            }
        });


        buttonSeventeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleSeventeen = new Intent(AllHoleViewer.this, HoleVizSeventeen.class);
                startActivity(goToHoleSeventeen);

            }
        });


        buttonEighteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHoleEighteen = new Intent(AllHoleViewer.this, HoleVizEighteen.class);
                startActivity(goToHoleEighteen);

            }
        });


    }
}
