package com.example.student.homemade;

import java.sql.Timestamp;
import java.util.ArrayList;

public class OrderInfo {

    int Provider;
    int client;
    boolean completed;
    boolean delivered;
    int delivery_person;
    int orderID;
    boolean paid;
    ArrayList<String> things_ordered;
    String time_and_date;
    int timeBeforeCancel;
    float total_cost;

    public OrderInfo(int Provider, int client, boolean completed, boolean delivered, int delivery_person, int orderID, boolean paid, ArrayList<String> things_ordered, String time_and_date, int timeBeforeCancel, float total_cost){
        this.Provider = Provider;
        this.client =  client;
        this.completed = completed;
        this.delivered = delivered;
        this.delivery_person = delivery_person;
        this.orderID = orderID;
        this.paid = paid;
        this.things_ordered = things_ordered;
        this.time_and_date = time_and_date;
        this.timeBeforeCancel = timeBeforeCancel;
        this.total_cost = total_cost;
    }

    public int getNoOrders(){ return things_ordered.size();}

    public int getClient() {
        return client;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public int getDelivery_person() {
        return delivery_person;
    }

    public int getOrderID() {
        return orderID;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getThings_ordered() {
        String result = "";
        for(int i=0; i<things_ordered.size() - 1; i++){
            result = result + things_ordered.get(i) + ", ";
        }
        result = result + things_ordered.get(things_ordered.size() - 1);

        return result;
    }

    public String getTime_and_date() {
        return time_and_date;
    }

    public float getTotal_cost() {
        return total_cost;
    }


    public void setProvider(int provider) {
        Provider = provider;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public void setDelivery_person(int delivery_person) {
        this.delivery_person = delivery_person;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setThings_ordered(ArrayList<String> things_ordered) {
        this.things_ordered = things_ordered;
    }

    public void setTime_and_date(String time_and_date) {
        this.time_and_date = time_and_date;
    }

    public void setTotal_cost(float total_cost) {
        this.total_cost = total_cost;
    }

    public float gettotal_cost(){ return total_cost;}

}