package com.example.causefairy.models;


import com.google.firebase.database.Exclude;

public class UserB {

    private String businessId;
    private String businessName;
    private int abn;
    private String email;
    private String password;
    private String confirm;
    private String busLogo;

    public UserB(){}

    public void setBusinessId(String businessId) {
        this.businessId= businessId;
    }

    public UserB(String uid, String businessId, String businessName, int abn, String email1, String password , String confirm, String busLogo) {
        User.getUid();
        this.businessId = businessId;
        this.businessName = businessName;
        this.abn = abn;
        this.email = email1;
        this.password = password;
        this.confirm = confirm;
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

    public String getConfirm() {
        return confirm;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setAbn(int abn) {
        this.abn = abn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public void setBusLogo(String busLogo) {
        this.busLogo = busLogo;
    }
}
