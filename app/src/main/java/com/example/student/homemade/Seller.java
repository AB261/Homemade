package com.example.student.homemade;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Seller implements Serializable {
    boolean active;
    GeoPoint geoPoint;
    boolean availability;
    ArrayList<String> customItems;
    String description;
    String email;
    HashMap<String,String> itemPictures;
    double longTermSubscriptionDiscount;
    double massOrderDiscount;
    double noOfMassOrders;
    String phone;
    String restaurantName;
    int timeBeforeCancel;
    String username;
    Long wallet;


    @Override
    public String toString() {
        return "Seller{" +
                "active=" + active +
                ", geoPoint=" + geoPoint +
                ", availability=" + availability +
                ", customItems=" + customItems +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", itemPictures=" + itemPictures +
                ", longTermSubscriptionDiscount=" + longTermSubscriptionDiscount +
                ", massOrderDiscount=" + massOrderDiscount +
                ", phone='" + phone + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", timeBeforeCancel=" + timeBeforeCancel +
                ", username='" + username + '\'' +
                ", wallet=" + wallet +
                '}';
    }

    public double getNoOfMassOrders() {
        return noOfMassOrders;
    }

    public void setNoOfMassOrders(double noOfMassOrders) {
        this.noOfMassOrders = noOfMassOrders;
    }

    public void setWallet(Long wallet) {
        this.wallet = wallet;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public ArrayList<String> getCustomItems() {
        return customItems;
    }

    public void setCustomItems(ArrayList<String> customItems) {
        this.customItems = customItems;
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

    public HashMap<String, String> getItemPictures() {
        return itemPictures;
    }

    public void setItemPictures(HashMap<String, String> itemPictures) {
        this.itemPictures = itemPictures;
    }

    public double getLongTermSubscriptionDiscount() {
        return longTermSubscriptionDiscount;
    }

    public void setLongTermSubscriptionDiscount(double longTermSubscriptionDiscount) {
        this.longTermSubscriptionDiscount = longTermSubscriptionDiscount;
    }

    public double getMassOrderDiscount() {
        return massOrderDiscount;
    }

    public void setMassOrderDiscount(double massOrderDiscount) {
        this.massOrderDiscount = massOrderDiscount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getTimeBeforeCancel() {
        return timeBeforeCancel;
    }

    public void setTimeBeforeCancel(int timeBeforeCancel) {
        this.timeBeforeCancel = timeBeforeCancel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getWallet() {
        return wallet;
    }
}
