package com.example.eddys.danyalrequestone;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.eddys.danyalrequestone.R.color.postClick;
import static com.example.eddys.danyalrequestone.R.color.preClick;


public class MainActivity extends AppCompatActivity {
    private EditText inputOne;
    private EditText inputTwo;
    private Button activateButton;
    private Button upgradeButton;
    private Button noneButton;
    private int extraFeeCode; // 0 = none, 1 = activation 2 = upgrade
    private RadioGroup creditClassGroup;
    private RadioGroup creditClassGroupTwo;
    private boolean creditGroupClicked[] = new boolean[2]; //true = groupOneChecked
    private RadioGroup autoPayGroup;
    private Button calcButton;

    private NumberCruncher numCrunch = new NumberCruncher();




    private void colorSwitcher(int switchTest) {
        switch (switchTest){
            case 0:
                noneButton.setBackgroundResource(postClick);
                activateButton.setBackgroundResource(preClick);
                upgradeButton.setBackgroundResource(preClick);

                break;
            case 1:
                activateButton.setBackgroundResource(postClick);
                upgradeButton.setBackgroundResource(preClick);
                noneButton.setBackgroundResource(preClick);
                break;
            case 2:
                upgradeButton.setBackgroundResource(postClick);
                activateButton.setBackgroundResource(preClick);
                noneButton.setBackgroundResource(preClick);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        numCrunch.getMiDevices()[0].setBaseCost(1);




        activateButton = (Button) findViewById(R.id.activate);
        upgradeButton = (Button) findViewById(R.id.upgrade);
        noneButton = (Button) findViewById(R.id.none);


        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraFeeCode = 1;
                colorSwitcher(extraFeeCode);
            }
        });

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraFeeCode = 2;
                colorSwitcher(extraFeeCode);
            }
        });

        noneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraFeeCode = 0;
                colorSwitcher(extraFeeCode);
            }
        });


        inputOne = (EditText) findViewById(R.id.inputOne);
        inputTwo = (EditText) findViewById(R.id.inputTwo);

        creditClassGroup = (RadioGroup) findViewById(R.id.creditClassGroup);
        creditClassGroupTwo = (RadioGroup) findViewById(R.id.creditClassGroup2);



        creditClassGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (creditGroupClicked[1] == true) {
                    creditClassGroupTwo.clearCheck();
                    creditGroupClicked[1] = false;
                }
                creditGroupClicked[0] = true;

            }
        });

        creditClassGroupTwo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (creditGroupClicked[0] == true) {
                    creditClassGroup.clearCheck();
                    creditGroupClicked[0] = false;
                }
                creditGroupClicked[1] = true;
            }
        });





        autoPayGroup = (RadioGroup) findViewById(R.id.autopaygroup);


        calcButton = (Button) findViewById(R.id.calc);
        calcButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numCrunch.setBasePhoneCost(Integer.parseInt(inputOne.getText().toString()));
                numCrunch.setPhoneDownPayment(Integer.parseInt(inputTwo.getText().toString()));


                switch (creditClassGroup.getCheckedRadioButtonId()) {
                    case R.id.creditClass1:
                        numCrunch.setCreditClass(CreditClass.WELL_QUALIFIED);
                        break;
                    case R.id.creditClass2:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_TWO);
                        break;
                    case R.id.creditClass3:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_THREE);
                        break;
                    case R.id.creditClass4:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_FOUR);
                        break;
                    case R.id.creditClass5:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_FIVE);
                        break;
                    case R.id.creditClass6:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_SIX);
                        break;

                }

                switch (creditClassGroupTwo.getCheckedRadioButtonId()) {
                    case R.id.creditClass7:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_SEVEN);
                        break;
                    case R.id.creditClass8:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_EIGHT);
                        break;
                    case R.id.creditClass9:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_NINE);
                        break;
                    case R.id.creditClass10:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_TEN);
                        break;
                    case R.id.creditClass11:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_ELEVEN);
                        break;
                    case R.id.creditClass12:
                        numCrunch.setCreditClass(CreditClass.DOWN_PAYMENT_TWELVE);
                        break;


                }

                numCrunch.setExtraFee(extraFeeCode);


                switch (autoPayGroup.getCheckedRadioButtonId()) {
                    case R.id.autopayoff:
                        numCrunch.setAutoPay(false);
                        break;
                    case R.id.autopayon:
                        numCrunch.setAutoPay(true);
                        break;
                }

                numCrunch.calcTotalUpFrontCost();
                numCrunch.calcTotalMonthlyCost();
                numCrunch.calcTotalUpfrontMiDeviceCost();
                numCrunch.calcTotalMonthlyMiDeviceCost();


                Log.e("Current Tax: 5", Double.toString(numCrunch.getTax()));
                Log.e("Device 2, DownPayment", Double.toString(numCrunch.getMiDevices()[1].getDownPayment()[11]));


                Intent i = new Intent(MainActivity.this, OutputActivity.class);
                i.putExtra("numCrunch", numCrunch);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();





        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MiDevice tempDevice = new MiDevice();
                String deviceKey;





                for (int i = 0; i < 5; i++) {
                    deviceKey = "MiDevice" + Integer.toString(i);


                    numCrunch.getMiDevices()[i].setBaseCost(dataSnapshot.child(deviceKey).child(
                            "baseCost").getValue(Double.class));


                    numCrunch.getMiDevices()[i].setName(
                            dataSnapshot.child(deviceKey).child("name").getValue(String.class));

                    numCrunch.getMiDevices()[i].setName(
                            dataSnapshot.child(deviceKey).child("name").getValue(String.class));

                    for (int k = 0; k < 12; k++) {
                        numCrunch.getMiDevices()[i].setDownPayment(dataSnapshot.child(deviceKey).child("downPayment").
                                child(Integer.toString(k)).getValue(Double.class), k);


                    }

                }

                numCrunch.setTax(dataSnapshot.child("Other").child("tax").getValue(Double.class));
                dataSnapshot.child("Other").child("tax").getValue();




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



        /*ArrayList<Integer> arrayTest = new ArrayList<Integer>(8);

        for (int i = 0; i < 8; i++) {
            arrayTest.add(i);
        }

        for (int i = 0; i < 5; i++) {
            String child = "MiDevice" + Integer.toString(i);



            myRef.child(child).child("baseCost").setValue(5);
            myRef.child(child).child("downPayment").setValue(arrayTest);

        }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_admin:
                Intent i = new Intent(MainActivity.this, AdminActivity.class);
                i.putExtra("numCrunch", numCrunch);
                startActivity(i);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }




}
