package com.example.jugjig.foodland.model;

public class Reservation {
    private String customerId;
    private String restaurantId;
    private String date;
    private Integer amount;
    private String comment;
    private String time;
    private String status;

    public Reservation() {
    }

    public Reservation(String customerId, String restaurantId, String date, Integer amount, String comment, String time, String status) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.amount = amount;
        this.comment = comment;
        this.time = time;
        this.status = status;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
