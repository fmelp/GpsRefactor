package com.franmelp.golfgpsrefactor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AAA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(this);
        linearLayout.addView(tv);

        Bundle b = getIntent().getExtras();
        if (b != null){
            int i = b.getInt("key");
            String s = new Integer(i).toString();
            tv.setText(s);
        }else{
            tv.setText("Got fuckall");
        }

        Button test = new Button(this);
        linearLayout.addView(test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(AAA.this, AAA.class);
                start.putExtra("key", 1);
                startActivity(start);
                finish();

            }
        });



    }
}
