package com.example.student.homemade;

import java.util.HashMap;

public class MassOrderInfo   {
String Consumer;
String address;
boolean delivered;
String orderDate;
String orderTime;
HashMap<String, Object> orderItems;
boolean paid;
String provider;
String restaurantName;

    public MassOrderInfo(String consumer, String address, boolean delivered, String orderDate, String orderTime, HashMap<String, Object> orderItems, boolean paid, String provider, String restaurantName) {
        Consumer = consumer;
        this.address = address;
        this.delivered = delivered;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderItems = orderItems;
        this.paid = paid;
        this.provider = provider;
        this.restaurantName = restaurantName;
    }

    public String getConsumer() {
        return Consumer;
    }

    public void setConsumer(String consumer) {
        Consumer = consumer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public HashMap<String, Object> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(HashMap<String, Object> orderItems) {
        this.orderItems = orderItems;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
    public int getNoOrders(){ return orderItems.size();}
}
