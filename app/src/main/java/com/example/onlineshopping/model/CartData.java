package com.example.onlineshopping.model;


import java.io.Serializable;
public class CartData  {
    private String id;
    private String cartKey;
    private String name;
    private String color;
    private int price;
    private String imageId;
    private int quantity ;
    private String userEmail ;
    private String userName ;
    private  String description ;

    public CartData(String id , String name, String color, int price, String imageId, int quantity, String userEmail , String cartKey , String description ) {
        this.id = id;
        this.cartKey = cartKey ;
        this.name = name;
        this.color = color;
        this.price = price;
        this.imageId = imageId;
        this.quantity = quantity;
        this.userEmail = userEmail;
        this.description = description ;
        this.userName = userName ;
    }
    public CartData(String id, String name, String color, int price, String imageId, int quantity, String cartKey, String description) {
        this.id = id;
        this.cartKey = cartKey ;
        this.name = name;
        this.color = color;
        this.price = price;
        this.imageId = imageId;
        this.quantity = quantity;
        this.description = description ;
    }
    public CartData(String id, String name, String color, int price, String imageId, int quantity) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.price = price;
        this.imageId = imageId;
        this.quantity = quantity;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getCartKey() {
        return cartKey;
    }
    public void setCartKey(String cartKey) {
        this.cartKey = cartKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
