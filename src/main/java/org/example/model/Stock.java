package org.example.model;

public class Stock {
    String companyName;
    double price;
    double changePercent;

    public Stock(String companyName, double price, double changePercent) {
        this.companyName = companyName;
        this.price = price;
        this.changePercent = changePercent;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public double getChangePercent() {
        return changePercent;
    }
}