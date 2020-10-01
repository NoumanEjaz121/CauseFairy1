package com.example.causefairy.models;


import com.google.firebase.database.Exclude;

public class UserB {

    private String businessId;
    private String businessName;
    private int abn;
    private String email;
    private String password;
    private String busLogo;

    public UserB(){}

    public void setBusinessId(String causeId) {
        this.businessId= causeId;
    }

    public UserB(String businessId, String businessName, int abn, String email1, String password , String busLogo, String timestamp, String uid) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.abn = abn;
        this.email = email1;
        this.password = password;
        this.busLogo = busLogo;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getBusLogo() {
        return busLogo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public int getAbn() {
        return abn;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
