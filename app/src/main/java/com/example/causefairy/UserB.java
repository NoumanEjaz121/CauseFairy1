package com.example.causefairy;



public class UserB extends User{

    private String businessId;
    private String businessName;
    private int abn;
    private String email;
    private String password;
    private String conpass;
    private String busLogo;

    public UserB(){}

    public UserB(String documentId, String name1, String name2, String email, String password, String conpass, String profilePic, String businessId, String businessName, int abn, String email1, String password1, String conpass1, String busLogo) {
        super(documentId, name1, name2, email, password, conpass, profilePic);
        this.businessId = businessId;
        this.businessName = businessName;
        this.abn = abn;
        this.email = email1;
        this.password = password1;
        this.conpass = conpass1;
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

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getConpass() {
        return conpass;
    }
}
