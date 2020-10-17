package com.example.causefairy.models;

public class ItemTrending {

// here i am taking only image url. and this is as integer because i am using it from drawable file.

    Integer itemId;
    Integer imageUrl;

    public ItemTrending(Integer itemId, Integer imageUrl) {
        this.itemId = itemId;
        this.imageUrl = imageUrl;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }
}