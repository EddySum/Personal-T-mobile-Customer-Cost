package com.example.eddys.danyalrequestone;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;



import java.text.DecimalFormat;
import java.util.Arrays;

import static com.example.eddys.danyalrequestone.CreditClass.WELL_QUALIFIED;


/**
 * Created by eddys on 6/21/2018.
 */

enum CreditClass {
    WELL_QUALIFIED,
    DOWN_PAYMENT_TWO,
    DOWN_PAYMENT_THREE,
    DOWN_PAYMENT_FOUR,
    DOWN_PAYMENT_FIVE,
    DOWN_PAYMENT_SIX,
    DOWN_PAYMENT_SEVEN,
    DOWN_PAYMENT_EIGHT,
    DOWN_PAYMENT_NINE,
    DOWN_PAYMENT_TEN,
    DOWN_PAYMENT_ELEVEN,
    DOWN_PAYMENT_TWELVE,
    NULL,
}

public class NumberCruncher implements Parcelable {
    /*
        40 inputs
        5 rows 8 columns
        each row is its own thing

     */

    private static final int cycle = 24;
    private int baseAccessoryCost = 100;

    private int basePhoneCost;
    private int phoneDownPayment;
    private CreditClass creditClass;
    private int extraFee;
    private boolean autoPay;



    private double tax;

    private double upFrontPhoneCost;
    private double upFrontMiLineCost;
    private double upFrontAccessoryCost;
    private double totalUpFrontCost;
    private double phoneMonthlyCost;
    private int miLineMonthlyCost;//mobileInternet
    private double accessoryMonthlyCost;
    private double totalMonthlyCost;

    private MiDevice miDevices[] = new MiDevice[5];
    private int creditClassIdx;


    NumberCruncher() {
        basePhoneCost = 0;
        phoneDownPayment = 0;
        creditClass = CreditClass.NULL;
        extraFee = 0;
        autoPay = false;

        tax = 6.35;

        upFrontPhoneCost = 0;
        upFrontMiLineCost = 0;
        totalUpFrontCost = 0;
        phoneMonthlyCost = 0;
        miLineMonthlyCost = 0;
        accessoryMonthlyCost = 0;
        totalMonthlyCost = 0;

        for (int i = 0; i < 5; i++) {
            miDevices[i] = new MiDevice();

        }

        creditClassIdx = 0;

        miDevices[0].setBaseCost(5.0);

    }



    public void calcUpFrontPhoneCost() {
         //upFrontPhoneCost = accessoryTax + miLineActivationFee + extraFee + phoneDownPayment + (basePhoneCost * (tax/100));
        upFrontPhoneCost = extraFee + phoneDownPayment + (basePhoneCost * (tax/100));

    }

    public void calcUpFrontMiLineCost() {
        upFrontMiLineCost = 25 + (25 * (tax/100));
    }

    public void calcUpFrontAccessoryCost() {
        if (creditClass == WELL_QUALIFIED) {
            upFrontAccessoryCost = 0 + (baseAccessoryCost * (tax/100));
        }
        else {
            upFrontAccessoryCost = (baseAccessoryCost * (50/100)) + (baseAccessoryCost * tax/100);
        }
    }

    public void calcTotalUpFrontCost() {
        DecimalFormat df = new DecimalFormat("0.##");

        calcUpFrontPhoneCost();
        calcUpFrontMiLineCost();
        calcUpFrontAccessoryCost();
        totalUpFrontCost = upFrontPhoneCost + upFrontMiLineCost + upFrontAccessoryCost;
        totalUpFrontCost = Double.parseDouble(df.format(totalUpFrontCost));


    }

    public void calcPhoneMonthlyCost() {
        phoneMonthlyCost = (basePhoneCost - phoneDownPayment) / cycle;
        Log.e("this", Double.toString(phoneMonthlyCost));
    }

    public void calcAccessoryMonthlyCost() {
        accessoryMonthlyCost =  (100 / cycle);
    }


    public void calcMiLineMonthlyCost() {
        if (autoPay == true) {
            miLineMonthlyCost = 10;
        }
        else {
            miLineMonthlyCost = 15;
        }
    }


    public void calcTotalMonthlyCost() {
        DecimalFormat df = new DecimalFormat("0.##");

        calcPhoneMonthlyCost();
        calcAccessoryMonthlyCost();
        calcMiLineMonthlyCost();

      totalMonthlyCost = phoneMonthlyCost + accessoryMonthlyCost + miLineMonthlyCost;

        totalMonthlyCost = Double.parseDouble(df.format(totalMonthlyCost));
    }








    private void calcUpfrontMiDeviceCosts() {
        double upFrontMiDeviceCost;

        for (int i = 0; i < 5; i++) {



             upFrontMiDeviceCost = miDevices[i].getDownPayment()[creditClassIdx] +
                    (miDevices[i].getBaseCost() * (tax/100));

            miDevices[i].setUpFrontMiDeviceCost(upFrontMiDeviceCost);


            Log.e("BaseCost", Double.toString(miDevices[i].getBaseCost()));
            Log.e("DP", Double.toString(miDevices[i].getDownPayment()[creditClassIdx]));


        }

    }

    public void calcTotalUpfrontMiDeviceCost() {
        calcUpfrontMiDeviceCosts();
        DecimalFormat df = new DecimalFormat("0.##");


        for (int i = 0; i < 5; i++) {

            double totalUpFrontMiDeviceCost = totalUpFrontCost + miDevices[i].getUpFrontMiDeviceCost();


            totalUpFrontMiDeviceCost = Double.parseDouble(df.format(totalUpFrontMiDeviceCost));

            miDevices[i].setTotalUpFrontCost(totalUpFrontMiDeviceCost);


        }
    }

    private void calcMonthlyMiDeviceCost() {
        for (int i = 0; i < 5; i++) {

            double monthlyDeviceCost = (miDevices[i].getBaseCost()
                    - miDevices[i].getDownPayment()[creditClassIdx]) / cycle;


            miDevices[i].setMonthlyDeviceCost(monthlyDeviceCost);

            Log.e("Monthly Device Cost", Double.toString(miDevices[i].getMonthlyDeviceCost()));
        }
    }

    public void calcTotalMonthlyMiDeviceCost() {
        calcMonthlyMiDeviceCost();
        DecimalFormat df = new DecimalFormat("0.##");

        for (int i = 0; i < 5; i++) {
            double totalMonthlyDeviceCost = totalMonthlyCost
                    + miDevices[i].getMonthlyDeviceCost();

            totalMonthlyDeviceCost = Double.parseDouble(df.format(totalMonthlyDeviceCost));

            miDevices[i].setTotalMonthlyDeviceCost(totalMonthlyDeviceCost);
        }
    }







    public void setBasePhoneCost(int basePhoneCost) {
        this.basePhoneCost = basePhoneCost;
    }

    public void setPhoneDownPayment(int phoneDownPayment) {
        this.phoneDownPayment = phoneDownPayment;
    }

    public void setCreditClass(CreditClass creditClass) {
        this.creditClass = creditClass;

        switch (creditClass) {
            case WELL_QUALIFIED:
                creditClassIdx = 0;
                break;
            case DOWN_PAYMENT_TWO:
                creditClassIdx = 1;
                break;
            case DOWN_PAYMENT_THREE:
                creditClassIdx = 2;
                break;
            case DOWN_PAYMENT_FOUR:
                creditClassIdx = 3;
                break;
            case DOWN_PAYMENT_FIVE:
                creditClassIdx = 4;
                break;
            case DOWN_PAYMENT_SIX:
                creditClassIdx = 5;
                break;
            case DOWN_PAYMENT_SEVEN:
                creditClassIdx = 6;
                break;
            case DOWN_PAYMENT_EIGHT:
                creditClassIdx = 7;
                break;
            case DOWN_PAYMENT_NINE:
                creditClassIdx = 8;
                break;
            case DOWN_PAYMENT_TEN:
                creditClassIdx = 9;
                break;
            case DOWN_PAYMENT_ELEVEN:
                creditClassIdx = 10;
                break;
            case DOWN_PAYMENT_TWELVE:
                creditClassIdx = 11;
                break;
        }

    }

    public void setExtraFee(int extraFeeCode) {
        switch (extraFeeCode) {
            case 0:
                this.extraFee = 0;
                break;
            case 1:
                this.extraFee = 25; //activation  fee
                break;
            case 2:
                this.extraFee = 20; //upgrade fee
                break;
        }
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setAutoPay(boolean autoPay) {
        this.autoPay = autoPay;
    }

    public int getBasePhoneCost() {
        return basePhoneCost;
    }

    public double getTotalUpFrontCost() {
        return totalUpFrontCost;
    }

    public double getTotalMonthlyCost() {
        return totalMonthlyCost;
    }

    public  MiDevice[] getMiDevices() {
        return miDevices;
    }

    public double getTax() {
        return tax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(totalUpFrontCost);
        dest.writeDouble(totalMonthlyCost);
        dest.writeDouble(tax);
        dest.writeParcelableArray(miDevices,flags);

    }

    public static final Parcelable.Creator<NumberCruncher> CREATOR
            = new Parcelable.Creator<NumberCruncher>() {
        public NumberCruncher createFromParcel(Parcel in) {
            return new NumberCruncher(in);
        }

        public NumberCruncher[] newArray(int size) {
            return new NumberCruncher[size];
        }
    };

    private NumberCruncher(Parcel in) {
        this.totalUpFrontCost = in.readDouble();
        this.totalMonthlyCost = in.readDouble();
        this.tax = in.readDouble();
        Parcelable parcArray[] = in.readParcelableArray(MiDevice.class.getClassLoader());

        if (parcArray != null) {
            miDevices = Arrays.copyOf(parcArray, parcArray.length, MiDevice[].class);
        }
    }

}
