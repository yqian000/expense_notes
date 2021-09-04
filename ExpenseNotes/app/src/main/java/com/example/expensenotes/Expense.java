package com.example.expensenotes;

public class Expense {
    private String date;
    private String time;
    private double amount;
    private String category;

    public Expense(String date, String time, double amount, String category) {
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.category = category;
    }

    public Expense() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
