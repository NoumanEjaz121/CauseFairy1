package com.example.causefairy;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {
    private String documentId;
    private String productName;
    private String category;
    private String description;
    private int qty;
    private double unitPrice;
    private char shippingFee;
    private String sellerId;


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


    public Product(String productName, String category, String description, int qty, double unitPrice, char shippingFee, String sellerId) {
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.shippingFee = shippingFee;
        this.sellerId = sellerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public int getQty() {
        return qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public char getShippingFee() {
        return shippingFee;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}


