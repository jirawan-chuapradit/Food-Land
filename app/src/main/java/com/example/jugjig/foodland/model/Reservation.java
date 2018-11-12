package com.example.jugjig.foodland.model;

public class Reservation {
    private String customerId;
    private String restaurantId;
    private String date;
    private Integer amont;
    private String comment;
    private String time;

    public Reservation() {
    }

    public Reservation(String customerId, String restaurantId, String date, Integer amont, String comment, String time) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.amont = amont;
        this.comment = comment;
        this.time = time;
    }

    public String getCustomerId() {

        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAmont() {
        return amont;
    }

    public void setAmont(Integer amont) {
        this.amont = amont;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
