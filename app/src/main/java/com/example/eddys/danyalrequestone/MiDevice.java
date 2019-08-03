package com.example.eddys.danyalrequestone;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eddys on 6/28/2018.
 */

public class MiDevice implements Parcelable{
    //admin
    private double downPayment[] = new double[12];
    private double baseCost;

    //calculation results
    private double upFrontMiDeviceCost;
    private double totalUpFrontCost;
    private double monthlyDeviceCost;
    private double totalMonthlyDeviceCost;

    //misc
    private String name;

    public MiDevice() {

        this.baseCost = 0;

        for (int i = 0; i < 12; i++) {
            downPayment[i] = 13;
        }

        this.upFrontMiDeviceCost = 0;
        this.totalUpFrontCost = 0;
        this.monthlyDeviceCost = 0;
        this.totalMonthlyDeviceCost = 0;

        this.name = "null";
    }


    public MiDevice(double upFrontMiDeviceCost, double totalUpFrontCost, double monthlyDeviceCost) {

    }




    public void setDownPayment(double[] downPayment) {
        this.downPayment = downPayment;
    }

    public void setDownPayment(double downPayment, int idx) {
        this.downPayment[idx] = downPayment;
    }


    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    public void setUpFrontMiDeviceCost(double upFrontMiDeviceCost) {
        this.upFrontMiDeviceCost = upFrontMiDeviceCost;
    }

    public void setTotalUpFrontCost(double totalUpFrontCost) {
        this.totalUpFrontCost = totalUpFrontCost;
    }

    public void setMonthlyDeviceCost(double monthlyDeviceCost) {
        this.monthlyDeviceCost = monthlyDeviceCost;
    }

    public void setTotalMonthlyDeviceCost(double totalMonthlyDeviceCost) {
        this.totalMonthlyDeviceCost = totalMonthlyDeviceCost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double[] getDownPayment() {
        return downPayment;
    }

    public  double getBaseCost() {
        return baseCost;
    }

    public double getUpFrontMiDeviceCost() {
        return upFrontMiDeviceCost;
    }

    public double getTotalUpFrontCost() {
        return totalUpFrontCost;
    }

    public double getMonthlyDeviceCost() {
        return monthlyDeviceCost;
    }

    public double getTotalMonthlyDeviceCost() {
        return totalMonthlyDeviceCost;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(downPayment);
        dest.writeDouble(baseCost);
        dest.writeDouble(totalUpFrontCost);
        dest.writeDouble(totalMonthlyDeviceCost);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<MiDevice> CREATOR
            = new Parcelable.Creator<MiDevice>() {
        public MiDevice createFromParcel(Parcel in) {
            return new MiDevice(in);
        }

        public MiDevice[] newArray(int size) {
            return new MiDevice[size];
        }
    };

    private MiDevice(Parcel in) {
        in.readDoubleArray(downPayment);
        baseCost = in.readDouble();
        totalUpFrontCost = in.readDouble();
        totalMonthlyDeviceCost = in.readDouble();
        name = in.readString();

    }


}
