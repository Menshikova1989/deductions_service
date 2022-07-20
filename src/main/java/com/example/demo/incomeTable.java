package com.example.demo;

import java.io.Serializable;

public class incomeTable implements Serializable {

    private String month;
    private double income;
    private double sumIncome;


    public incomeTable(String month, double income) {
        this.month = month;
        this.income = income;
    }

    public incomeTable() {
    }

    public incomeTable(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getSumIncome() {
        return sumIncome;
    }

    public void setSumIncome(double sumIncome) {
        this.sumIncome = sumIncome;
    }
}
