package com.example.causefairy.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {
    private String documentId; //productId
    private String productName;
    private String category;
    private String description;
    private int qty;
    private double unitPrice;
    private String productIcon;
    private String timestamp;
    private String uid;

    public Product(){}

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public Product(String productName, String category, String description, int qty, double unitPrice) {  //temp
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public Product(String documentId, String productName, String category, String description, int qty, double unitPrice, String productIcon, String timestamp, String uid) {
        this.documentId = documentId;
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.productIcon = productIcon;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}


