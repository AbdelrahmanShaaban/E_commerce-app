package com.example.onlineshopping.model;

import java.util.ArrayList;

public class Order {
    private String userEmail;
    private String userAddress;
    private String username;
    private String userPhone;
    private String orderDate;
    private int totalPrice;
    private ArrayList<CartData> cartDataArrayList ;


    public Order(String userEmail, String userAddress, String username, String userPhone, String orderDate,int totalPrice, ArrayList<CartData> cartDataArrayList) {
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.username = username;
        this.userPhone = userPhone;
        this.orderDate = orderDate;
        this.totalPrice=totalPrice;
        this.cartDataArrayList = cartDataArrayList;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<CartData> getCartDataArrayList() {
        return cartDataArrayList;
    }

    public void setCartDataArrayList(ArrayList<CartData> cartDataArrayList) {
        this.cartDataArrayList = cartDataArrayList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}

