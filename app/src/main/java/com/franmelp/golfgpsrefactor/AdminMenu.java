package com.franmelp.golfgpsrefactor;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AdminMenu extends AppCompatActivity {

    //needs to display cart number
    //  let you change this number (will be temporary until app is reset)
    //turn siren off/on

//    private String cartNumberString;

    private final Model model = Model.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);


        float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                18F, this.getApplicationContext().getResources().getDisplayMetrics());
        myTextSize += 10.0;
        int color = Integer.parseInt("32cd32", 16)+0xFF000000;

//show cart number
        TextView showNumber = (TextView) findViewById(R.id.cartNumberFinal);
        showNumber.setText("CART #: " + model.CART_NUMBER);
        showNumber.setTextSize(myTextSize + 30);
        showNumber.setTextColor(color);

        //siren on radio button
        final RadioButton sirenOn = (RadioButton) findViewById(R.id.sirenRadio0);
        sirenOn.setText("ROMPERE");
        sirenOn.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize);
        if (model.SIREN_BOOL){
            sirenOn.setTextColor(Color.BLUE);
            sirenOn.setTypeface(null, Typeface.BOLD);
            sirenOn.setChecked(true);
        }else{
            sirenOn.setTextColor(Color.GRAY);
        }
        sirenOn.setGravity(Gravity.CENTER);


        //siren off radio button
        final RadioButton sirenOff = (RadioButton) findViewById(R.id.sirenRadio1);
        sirenOff.setText("NON ROMPERE");
        sirenOff.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize);
        if (model.SIREN_BOOL){
            sirenOff.setTextColor(Color.GRAY);
        }else{
            sirenOff.setTextColor(Color.BLUE);
            sirenOff.setTypeface(null, Typeface.BOLD);
            sirenOff.setChecked(true);
        }
        sirenOff.setGravity(Gravity.CENTER);

        //handle radiogroup from sirenOn sirenOff
        //creates listener
        RadioGroup rg = (RadioGroup) findViewById(R.id.sirenRadioG);
//         This overrides the radiogroup onCheckListener
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
                if (sirenOn.isChecked()) {
//                    Model.SIREN_BOOL = true;
                    //change siren boolean to true
                    model.changeSirenStatus();
                    sirenOn.setTextColor(Color.BLUE);
                    sirenOff.setTypeface(null, Typeface.NORMAL);
                    sirenOn.setTypeface(null, Typeface.BOLD);
                    sirenOff.setTextColor(Color.GRAY);
                    System.out.println("--------"+model.SIREN_BOOL+"---------");
                }
                if (sirenOff.isChecked()) {
//                    Model.SIREN_BOOL = false;
                    model.changeSirenStatus();
                    sirenOff.setTextColor(Color.BLUE);
                    sirenOff.setTypeface(null, Typeface.BOLD);
                    sirenOn.setTextColor(Color.GRAY);
                    sirenOn.setTypeface(null, Typeface.NORMAL);
                    System.out.println("--------" + model.SIREN_BOOL + "---------");
                }


            }
        });

        //go back to home screen
        Button adminMenuButton = (Button) findViewById(R.id.adminMenuButton);
        adminMenuButton.setTextSize(myTextSize);
        adminMenuButton.setTextColor(color);
        adminMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAdminMenu = new Intent(AdminMenu.this, HomeScreenActivity.class);
                startActivity(startAdminMenu);
                finish();
            }
        });




    }
}
