package com.example.onlineshopping.model;

public class Users {
    private String userName ;
    private  String email ;
    private String phone ;
    private String address ;
    private String job ;
    private String birthDate ;
    private String gender ;
    private String type;


    public Users() {
    }

    public Users(String userName, String email, String phone, String address, String job, String birthDate, String gender, String type) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.job = job;
        this.birthDate = birthDate;
        this.gender = gender;
        this.type = type;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
