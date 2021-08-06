package com.example.onlineshopping.model;

import java.io.Serializable;

public class ProductData  {
    private String id ;
   private String name;
    private String color;
    private int price;
    private String imageId;
    private String description ;
    private String category ;
    private String barcode ;

    public ProductData(String id, String name, String color, int price, String imageId, String description, String category, String barcode) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.price = price;
        this.imageId = imageId;
        this.description = description;
        this.category = category;
        this.barcode = barcode;
    }

    public ProductData(String name, String color, int price, String imageId, String description, String category , String barcode ) {
        this.name = name;
        this.color = color;
        this.price = price;
        this.imageId = imageId;
        this.description = description;
        this.category = category;
        this.barcode = barcode ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
