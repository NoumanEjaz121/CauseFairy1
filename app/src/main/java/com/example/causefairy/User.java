package com.example.causefairy;


import com.google.firebase.database.Exclude;

public class User {

    private String documentId;
    private String name1;
    private String name2;
    private String email;
    private String password;
    private String conpass;
    private String profilePic;
    private String timestamp;
    private String uid;

    public User(){}

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public User(String documentId, String name1, String name2, String email, String password, String conpass, String profilePic, String timestamp, String uid) {
        this.documentId = documentId;
        this.name1 = name1;
        this.name2 = name2;
        this.email = email;
        this.password = password;
        this.conpass = conpass;
        this.profilePic = profilePic;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getProfilePic() {
        return profilePic;
    }


    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConpass() {
        return conpass;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        timestamp = ""+ System.currentTimeMillis();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
