package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MenuOrder extends AppCompatActivity {

    private int totalInt;
    private LinearLayout itemHolder;
    Context context;
    ArrayList<Button> plusMinusButtons;
    ArrayList<TextView> itemQuantitiesView;
    ArrayList<Integer> itemQuantitiesInt;
    ArrayList<Integer> itemPrices;
    ArrayList<LinearLayout> itemHolderList;
    TextView total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_order);

        //get context
        context = getApplicationContext();

        //set total to 0 initially
        totalInt = 3;

        //int lists
        plusMinusButtons = new ArrayList<>();
        itemQuantitiesView = new ArrayList<>();
        itemQuantitiesInt = new ArrayList<>();
        itemPrices = new ArrayList<>();
        itemHolderList = new ArrayList<>();

        //set up item holder
        itemHolder = (LinearLayout) findViewById(R.id.itemHolder);

        //size for text and green color for text
        float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                18F, this.getApplicationContext().getResources().getDisplayMetrics());
        myTextSize += 10.0;
        int color = Integer.parseInt("32cd32", 16)+0xFF000000;

        //set up food spinner
        //text
        //set up food spinnner holder size
        LinearLayout foodHolder = (LinearLayout) findViewById(R.id.foodScrollerHolder);
        TextView foodText = (TextView) findViewById(R.id.foodText);
        foodText.setText("Food");
        foodText.setTextColor(color);
        foodText.setTextSize(myTextSize);
        //spinner
        Spinner foodSpinner = (Spinner) findViewById(R.id.foodSpinner);
        String[] foodItems = new String[]{"", "Panino (prosciutto crudo e mozzarella): 5€",
                "Panino (prosciutto cotto e formaggio): 5€", "Panino (tonno e pomodoro): 5€",
                "Barretta Energetica: 2€", "Barretta Cioccolato: 2€"};
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, foodItems);
        foodSpinner.setAdapter(foodAdapter);
        //perform action when item selected
        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                        break;
                    case 1:
                        addItem("anino (prosciutto crudo e mozzarella): 5€", 5);
                        break;
                    case 2:
                        addItem("Panino (prosciutto cotto e formaggio): 5€", 5);
                        break;
                    case 3:
                        addItem("Panino (tonno e pomodoro): 5€", 5);
                        break;
                    case 4:
                        addItem("Barretta Energetica: 2€", 2);
                        break;
                    case 5:
                        addItem("Barretta Cioccolato: 2€", 2);
                        break;
//                    case 6:
//                        addItem("Beer: 6€", 2);
//                        break;
                }            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //set up drink spinner
        //text
        TextView drinkText = (TextView) findViewById(R.id.drinkText);
        drinkText.setText("Drinks");
        drinkText.setTextColor(color);
        drinkText.setTextSize(myTextSize);
        //spinner
        Spinner drinkSpinner = (Spinner) findViewById(R.id.drinkSpinner);
        String[] drinkItems = new String[]{"", "Still Water: 2€", "Sparking Water: 2€",
                "Coca-Cola: 4€", "Iced-Tea: 5€", "Powerade: 5€", "Beer: 6€"};
        ArrayAdapter<String> drinkAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, drinkItems);
        drinkSpinner.setAdapter(drinkAdapter);
        //perform action when item selected
        drinkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        addItem("Still Water: 2€", 2);
                        break;
                    case 2:
                        addItem("Sparkling Water: 2€", 2);
                        break;
                    case 3:
                        addItem("Coca-Cola: 4€", 4);
                        break;
                    case 4:
                        addItem("Iced-Tea: 5€", 5);
                        break;
                    case 5:
                        addItem("Powerade: 5€", 5);
                        break;
                    case 6:
                        addItem("Beer: 6€", 6);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //set up delivery fee
        TextView delivery = (TextView) findViewById(R.id.deliveryFeeText);
        delivery.setText("Delivery Fee: 3€\n------------------------------");
        delivery.setGravity(Gravity.RIGHT);
//        total.setTextSize(myTextSize);
//        total.setTextColor(color);


        //set up total
        total = (TextView) findViewById(R.id.totalText);
        total.setText("Total: 0€");
        total.setGravity(Gravity.RIGHT);
        total.setTextSize(myTextSize);
        total.setTextColor(color);

        //set up order button
        Button orderButton = (Button) findViewById(R.id.orderButton);
        orderButton.setText("ORDER");
        orderButton.setTextSize(myTextSize);
        orderButton.setTextColor(color);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendEmail().execute();

            }
        });

    }

    //async task to send email
    final class SendEmail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            String message = createMessage();
            sendEmail(message, "info@sandomenicogolf.com");
            return "aa";
        }

    }

    private String createMessage(){
        String message = "";
        for (LinearLayout item : itemHolderList){
            TextView name = (TextView) item.getChildAt(0);
            TextView quantity = (TextView) item.getChildAt(1);
            message += name.getText() + "||";
            message += " " + quantity.getText() + "\n";
        }
        message += ("\n totale: " + Integer.toString(totalInt));
        return message;
    }

    private void sendEmail1(){
        //SENDING EMAILS
        //first part is hack to avoid doing it in async task

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //actual mail sending code
        Mail m = new Mail("golfsandomenico@gmail.com", "chemaestro");
        String[] toAttr = {"info@sandomenicogolf.com"};
        m.setTo(toAttr);
        m.setFrom("golfsandomenico@gmail.com");
        m.setSubject("buoooongioooorno");
        m.setBody("CHE MAESTRO!");
        try {
            if(m.send()) {
                Toast.makeText(MenuOrder.this, "Email was sent succesfully.", Toast.LENGTH_LONG).show();
//                    here redirect the user back to the screen they were at. indicating success
            } else {
                Toast.makeText(MenuOrder.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            //in here i need to handle if the email was not sent
            //need to warn user and tell him to send form again
            //or not clear form until this exception doesnt run
            Log.e("MailApp", "Could not send email", e);
            Toast.makeText(MenuOrder.this, "Email was not sent exception", Toast.LENGTH_LONG).show();
        }
    }

    private void sendEmail(String message, String recepient){
        //SENDING EMAILS

        //actual mail sending code
        Mail m = new Mail("golfsandomenico@gmail.com", "chemaestro");
        String[] toAttr = {recepient};
        m.setTo(toAttr);
        m.setFrom("golfsandomenico@gmail.com");
        m.setSubject("Ordine per cart #" + Model.CART_NUMBER);
        m.setBody(message);
        try {
            if(m.send()) {
//                Toast.makeText(MenuOrder.this, "Email was sent succesfully.", Toast.LENGTH_LONG).show();
//                    here redirect the user back to the screen they were at. indicating success
            } else {
//                Toast.makeText(MenuOrder.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            //in here i need to handle if the email was not sent
            //need to warn user and tell him to send form again
            //or not clear form until this exception doesnt run
            Log.e("MailApp", "Could not send email", e);
            Toast.makeText(MenuOrder.this, "Email was not sent exception", Toast.LENGTH_LONG).show();
        }
    }


    private void addItem(String itemName, final int itemPrice){
        LinearLayout item = new LinearLayout(context);
        item.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        //textView for item name
        TextView itemNameText = new TextView(context);
        itemNameText.setText(itemName);
        itemNameText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 3f));
        item.addView(itemNameText);
        //set up quantity textView
        TextView itemQuantityText = new TextView(context);
        itemQuantityText.setText("Quantity: 1");
        itemQuantityText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 3f));
        itemQuantityText.setGravity(Gravity.CENTER);
        item.addView(itemQuantityText);
        itemQuantitiesInt.add(1);
        itemQuantitiesView.add(itemQuantityText);
        itemPrices.add(itemPrice);
        //set up add button
        final Button add = new Button(context);
        add.setText("+");
//        add.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        item.addView(add);
        //set up minus button
        final Button minus = new Button(context);
        minus.setText("-");
//        add.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        item.addView(minus);
        //add buttons to array
        //+ in evens
        //- in odds
        plusMinusButtons.add(add);
        plusMinusButtons.add(minus);

        //reset the text for total
        totalInt += itemPrice;
        //set the text
        total.setText("Total:" + Integer.toString(totalInt) + "€");

        //finally add to layout
        itemHolder.addView(item);
        itemHolderList.add(item);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = plusMinusButtons.indexOf(add) / 2;
                int quantity = itemQuantitiesInt.get(position);
                itemQuantitiesInt.set(position, quantity + 1);
                TextView quantityVIew = itemQuantitiesView.get(position);
                quantityVIew.setText("Quantity: " + (quantity + 1));
                //reset the text for total
                totalInt += itemPrice;
                //set the text
                total.setText("Total: " + Integer.toString(totalInt) + "€");

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = plusMinusButtons.indexOf(minus) / 2;
                int quantity = itemQuantitiesInt.get(position);
                itemQuantitiesInt.set(position, quantity - 1);
                if (quantity - 1 == 0) {
                    //need to implement removing it
                    //remove from everywhere
//                    for (int i = position; i < plusMinusButtons.size() - 1; i++) {
//                        plusMinusButtons.
//
//                    }
                    itemQuantitiesInt.remove(position);
                    itemQuantitiesView.remove(position);
                    itemHolderList.remove(position);
//                    itemPrices.remove(position);
                    //remove from view
                    itemHolder.removeViewAt(position);
                    int index = plusMinusButtons.indexOf(minus);
                    plusMinusButtons.remove(index);
                    plusMinusButtons.remove(index - 1);
                    //reset total
                    totalInt -= itemPrice;
                    //set the text
                    total.setText("Total: " + Integer.toString(totalInt) + "€");

                } else {
                    TextView quantityVIew = itemQuantitiesView.get(position);
                    quantityVIew.setText("Quantity: " + (quantity - 1));
                    //reset the text for total
                    totalInt -= itemPrice;
                    //set the text
                    total.setText("Total: " + Integer.toString(totalInt) + "€");
                }


            }
        });



    }
}
