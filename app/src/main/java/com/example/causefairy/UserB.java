package com.example.causefairy;



public class UserB extends User{

    private String businessName;
    private int abn;
    private String email;
    private String password;
    private String conpass;

    public UserB(){}

    public UserB(String name1, String name2, String email, String password, String conpass, String businessName, int abn, String email1, String password1, String conpass1) {
        super(name1, name2, email, password, conpass);
        this.businessName = businessName;
        this.abn = abn;
        this.email = email1;
        this.password = password1;
        this.conpass = conpass1;
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
