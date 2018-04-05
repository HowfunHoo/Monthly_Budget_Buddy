package com.haofan.monthly_budget_buddy.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "monthlyStatus")
public class MonthlyStatus {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "mid")
    public String mid;

    @ColumnInfo(name = "myear_month")
    public String myear_month;

    @ColumnInfo(name = "mexpense")
    public double mexpense;

    @ColumnInfo(name = "mrevenue")
    public double mrevenue;

    @ColumnInfo(name = "mbudget")
    public double mbudget;

    @ColumnInfo(name = "mused")
    public double mused;

    @NonNull
    public String getMid() {
        return mid;
    }

    public void setMid(@NonNull String mid) {
        this.mid = mid;
    }

    public String getMyear_month() {
        return myear_month;
    }

    public void setMyear_month(String myear_month) {
        this.myear_month = myear_month;
    }

    public double getMexpense() {
        return mexpense;
    }

    public void setMexpense(double mexpense) {
        this.mexpense = mexpense;
    }

    public double getMrevenue() {
        return mrevenue;
    }

    public void setMrevenue(double mrevenue) {
        this.mrevenue = mrevenue;
    }

    public double getMbudget() {
        return mbudget;
    }

    public void setMbudget(double mbudget) {
        this.mbudget = mbudget;
    }

    public double getMused() {
        return (this.mexpense - this.mrevenue)/this.mbudget;
    }

    public void setMused(double mexpense, double mrevenue, double mbudget) {
        this.mused = (mexpense - mrevenue)/mbudget;
    }
}
