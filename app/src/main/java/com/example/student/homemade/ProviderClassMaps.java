package com.example.student.homemade;

import com.google.firebase.firestore.GeoPoint;

public class ProviderClassMaps {
    private boolean active;
    private GeoPoint address;
    private String description, email, password, phone, restaurantname, typeOfUser, userid, username;
    private int wallet;

    public ProviderClassMaps(boolean active, GeoPoint address, String description, String email, String password, String phone, String restaurantname, String typeOfUser, String userid, String username, int wallet) {
        this.active = active;
        this.address = address;
        this.description = description;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.restaurantname = restaurantname;
        this.typeOfUser = typeOfUser;
        this.userid = userid;
        this.username = username;
        this.wallet = wallet;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRestaurantname() {
        return restaurantname;
    }

    public void setRestaurantname(String restaurantname) {
        this.restaurantname = restaurantname;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}
