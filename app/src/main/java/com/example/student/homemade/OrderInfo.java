package com.example.student.homemade;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderInfo{

    String provider;
    String consumer;
    boolean completed;
    boolean delivered;
    String deliveryPerson;
    boolean isMassOrder;
    boolean paid;
    ArrayList<HashMap<String,Object>> itemsOrdered;
    double orderTotal;
    String orderTime;
    String orderDate;

    public OrderInfo() {
    }

    public OrderInfo(String provider, String consumer, boolean completed, boolean delivered, String deliveryPerson, boolean isMassOrder, boolean paid, ArrayList<HashMap<String,Object>> itemsOrdered, double orderTotal, String orderTime, String orderDate) {
        this.provider = provider;
        this.consumer = consumer;

        this.completed = completed;
        this.delivered = delivered;
        this.deliveryPerson = deliveryPerson;
        this.isMassOrder = isMassOrder;
        this.paid = paid;
        this.itemsOrdered = itemsOrdered;
        this.orderTotal = orderTotal;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(String deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public boolean isMassOrder() {
        return isMassOrder;
    }

    public void setMassOrder(boolean massOrder) {
        isMassOrder = massOrder;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public ArrayList<HashMap<String,Object>> getItemsOrdered() {
        return itemsOrdered;
    }

    public void setItemsOrdered(ArrayList<HashMap<String,Object>> itemsOrdered) {
        this.itemsOrdered = itemsOrdered;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public int getNoOrders(){ return itemsOrdered.size();}
}

