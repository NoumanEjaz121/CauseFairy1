package com.example.causefairy.models;

import com.google.firebase.database.Exclude;

public class UserC extends UserB {

    private String causeId;
    private String description;
    private String category;
    private int postcode;
    private String phone;
    private int acnc;
    private String causeLogo;
    

    public UserC() {
    }

    public UserC(String causeId, String description, String category, int postcode, String phone, int acnc, String causeLogo) {
        this.causeId = causeId;
        this.description = description;
        this.category = category;
        this.postcode = postcode;
        this.phone = phone;
        this.acnc = acnc;
        this.causeLogo = causeLogo;
    }

    @Exclude
    public String getCauseId() {
        return causeId;
    }

    public void setCauseId(String causeId) {
        this.causeId = causeId;
    }

    public UserC(String documentId, String name1, String name2, String email, String password, String conpass, String profilePic, String timestamp, String uid, String businessId, String businessName, int abn, String email1, String password1, String conpass1, String busLogo, String causeId, String description, String catergory, int postcode, String phone, int acnc, String causeLogo) {
        super(documentId, name1, name2, email, password, conpass, profilePic, timestamp, uid, businessId, businessName, abn, email1, password1, conpass1, busLogo);
        this.causeId = causeId;
        this.description = description;
        this.category = category;
        this.postcode = postcode;
        this.phone = phone;
        this.acnc = acnc;
        this.causeLogo = causeLogo;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getPostcode() {
        return postcode;
    }

    public String getPhone() {
        return phone;
    }

    public int getAcnc() {
        return acnc;
    }

    public String getCauseLogo() {
        return causeLogo;
    }
}

