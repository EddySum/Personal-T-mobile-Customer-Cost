package com.example.eddys.danyalrequestone;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class OutputActivity extends AppCompatActivity {
    TextView totalUpFrontCost;
    TextView totalMonthlyCost;
    TextView totalUpFrontCostDevice[] = new TextView[5];
    TextView totalMonthlyCostDevice[] = new TextView[5];
    TextView deviceName[] = new TextView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final int[] deviceNameIds = {R.id.device1, R.id.device2,
                R.id.device3, R.id.device4, R.id.device5};

        final int[] upFrontCostIds = {R.id.totalupfrontcost1,R.id.totalupfrontcost2,
                R.id.totalupfrontcost3,R.id.totalupfrontcost4,R.id.totalupfrontcost5};

        final int[] totalMonthlyIds = {R.id.totalMonthlyCost1,R.id.totalMonthlyCost2,
                R.id.totalMonthlyCost3,R.id.totalMonthlyCost4,R.id.totalMonthlyCost5};


        NumberCruncher numCrunch = (NumberCruncher) getIntent().getParcelableExtra("numCrunch");


        totalUpFrontCost = (TextView) findViewById(R.id.totalupfrontcost) ;
        totalUpFrontCost.setText("Total Up Front Cost: " + numCrunch.getTotalUpFrontCost());

        totalMonthlyCost = (TextView) findViewById(R.id.totalMonthlyCost);
        totalMonthlyCost.setText("Total Monthly Cost: " + numCrunch.getTotalMonthlyCost());






        for (int i = 0; i < 5; i++) {
            deviceName[i] = (TextView) findViewById(deviceNameIds[i]);
            deviceName[i].setText(numCrunch.getMiDevices()[i].getName());

            totalUpFrontCostDevice[i] = (TextView) findViewById(upFrontCostIds[i]);
            totalUpFrontCostDevice[i].setText("Total Up Front Cost: " +
                    Double.toString(numCrunch.getMiDevices()[i].getTotalUpFrontCost()));
        }

        for (int i = 0; i < 5; i++) {
            totalMonthlyCostDevice[i] = (TextView) findViewById(totalMonthlyIds[i]);
            totalMonthlyCostDevice[i].setText("Total Monthly Cost " +
                    Double.toString(numCrunch.getMiDevices()[i].getTotalMonthlyDeviceCost()));
        }



    }

}
