package com.example.student.homemade;

import android.graphics.Bitmap;
import android.widget.Button;

import java.util.ArrayList;

//Class that has all the features of a restaurant
public class RestaurantModel {
    private String restaurantName,description,imageResourceId;
    private ArrayList<String> review;
    private float distance;
    private double rating;
    private String userID;
    Bitmap restaurantImage;

    public RestaurantModel(String restaurantName, String description, ArrayList<String> review,  float distance, String imageResourceId,double rating, String userID) {
        this.restaurantName = restaurantName;
        this.description = description;
        this.review = review;
        this.distance =  distance;
        this.imageResourceId = imageResourceId;
        this.rating = rating;
        this.userID=userID;
    }

    public Bitmap getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(Bitmap restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public String getUserID() {
        return userID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public float getDistance() {
        return distance;
    }

    public String getImageResourceId() {
        return imageResourceId;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getReview() {
        return review;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
    public void setDistance(float distance) {
        this.distance = distance;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReview(ArrayList<String> review) {
        this.review = review;
    }
}
