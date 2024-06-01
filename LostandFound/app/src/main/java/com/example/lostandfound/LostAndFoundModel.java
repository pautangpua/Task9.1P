package com.example.lostandfound;

public class LostAndFoundModel {
    private int id;
    private String postType, itemName, itemDescription, itemFoundDate, itemFoundLocation, finderName, finderPhone;

    // CONSTRUCTOR
    public LostAndFoundModel(String postType, String itemName, String itemDescription, String itemFoundDate, String itemFoundLocation, String finderName, String finderPhone) {
        this.postType = postType;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemFoundDate = itemFoundDate;
        this.itemFoundLocation = itemFoundLocation;
        this.finderName = finderName;
        this.finderPhone = finderPhone;
    }

    // CONSTRCUTOR TO PASS FROM DB TO LOST AND FOUND MODEL
    public LostAndFoundModel(int id, String postType, String itemName, String itemDescription, String itemFoundDate, String itemFoundLocation, String finderName, String finderPhone) {
        this.id = id;
        this.postType = postType;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemFoundDate = itemFoundDate;
        this.itemFoundLocation = itemFoundLocation;
        this.finderName = finderName;
        this.finderPhone = finderPhone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getFinderPhone() {
        return finderPhone;
    }

    public void setFinderPhone(String finderPhone) {
        this.finderPhone = finderPhone;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemFoundDate() {
        return itemFoundDate;
    }

    public void setItemFoundDate(String itemFoundDate) {
        this.itemFoundDate = itemFoundDate;
    }

    public String getItemFoundLocation() {
        return itemFoundLocation;
    }

    public void setItemFoundLocation(String itemFoundLocation) {
        this.itemFoundLocation = itemFoundLocation;
    }

    public String getFinderName() {
        return finderName;
    }

    public void setFinderName(String finderName) {
        this.finderName = finderName;
    }

    public String toString() {
        return postType + ": " + itemName + "\n" + itemFoundDate;
    }
}
