package com.smartloan.smtrick.electionapp;

public class MainSubProducts {

    public String mainproduct;
    public String subproduct;

    public MainSubProducts() {
    }

    public MainSubProducts(String mainproduct, String subproduct) {
        this.mainproduct = mainproduct;
        this.subproduct = subproduct;
    }

    public String getMainproduct() {
        return mainproduct;
    }

    public void setMainproduct(String mainproduct) {
        this.mainproduct = mainproduct;
    }

    public String getSubproduct() {
        return subproduct;
    }

    public void setSubproduct(String subproduct) {
        this.subproduct = subproduct;
    }
}
