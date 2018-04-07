package com.haofan.monthly_budget_buddy.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "item")
public class Item {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "iid")
    @NonNull
    public int iid;

    @ColumnInfo(name = "idate")
    public Date idatetime;

    @ColumnInfo(name = "iyear_month")
    public String iyear_month;

    @ColumnInfo(name = "inote")
    public String inote;

    @ColumnInfo(name = "iclass")
    public String iclass;

    @ColumnInfo(name = "itype")
    public String itype;

    @ColumnInfo(name = "isource")
    public String isource;

    @ColumnInfo(name = "iamount")
    public double iamount;

    @NonNull
    public int getIid() {
        return iid;
    }

    public void setIid(@NonNull int iid) {
        this.iid = iid;
    }

    public Date getIdatetime() {
        return idatetime;
    }

    public void setIdatetime(Date idatetime) {
        this.idatetime = idatetime;
    }

    public String getIyear_month() {
        return iyear_month;
    }

    public void setIyear_month(String iyear_month) {
        this.iyear_month = iyear_month;
    }

    public String getInote() {
        return inote;
    }

    public void setInote(String inote) {
        this.inote = inote;
    }

    public String getIclass() {
        return iclass;
    }

    public void setIclass(String iclass) {
        this.iclass = iclass;
    }

    public String getItype() {
        return itype;
    }

    public void setItype(String itype) {
        this.itype = itype;
    }

    public String getIsource() {
        return isource;
    }

    public void setIsource(String isource) {
        this.isource = isource;
    }

    public double getIamount() {
        return iamount;
    }

    public void setIamount(double iamount) {
        this.iamount = iamount;
    }

}
