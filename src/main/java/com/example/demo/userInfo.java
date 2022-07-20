package com.example.demo;

import java.io.Serializable;

public class userInfo implements Serializable {
    String name;
    boolean incomeInfo;
    int childCount;
    int childAge1;
    int childAge2;
    int childAge3;
    int index = 2;

    public userInfo(String name, boolean incomeInfo, int childCount) {
        this.name = name;
        this.incomeInfo = incomeInfo;
        this.childCount = childCount;
    }

    public userInfo(String name, boolean incomeInfo, int childCount, int childAge1, int childAge2, int childAge3) {
        this.name = name;
        this.incomeInfo = incomeInfo;
        this.childCount = childCount;
        this.childAge1 = childAge1;
        this.childAge2 = childAge2;
        this.childAge3 = childAge3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIncomeInfo() {
        return incomeInfo;
    }

    public void setIncomeInfo(boolean incomeInfo) {
        this.incomeInfo = incomeInfo;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

   public int getChildAge1() {
        return childAge1;
    }

    public void setChildAge1(int childAge1) {
        this.childAge1 = childAge1;
    }

    public int getChildAge2() {
        return childAge2;
    }

    public void setChildAge2(int childAge2) {
        this.childAge2 = childAge2;
    }

    public int getChildAge3() {
        return childAge3;
    }

    public void setChildAge3(int childAge3) {
        this.childAge3 = childAge3;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
