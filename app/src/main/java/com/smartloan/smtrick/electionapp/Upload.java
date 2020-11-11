package com.smartloan.smtrick.electionapp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Belal on 2/23/2017.
 */
@IgnoreExtraProperties
public class Upload {

    public String mainproduct;
    public String subproduct;
    public String desc;
    public String name;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String mainproduct, String subproduct, String desc, String name, String url) {
        this.mainproduct = mainproduct;
        this.subproduct = subproduct;
        this.desc = desc;
        this.name = name;
        this.url = url;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}