package com.example.jugjig.foodland.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Restaurant implements Parcelable {

    private String name;
    private String location;
    private String type;
    private String openTime;
    private String closeTime;
    private String telephone;
    private String profileImageURL;
    private ArrayList<String> imageDes;
    private Double latitude;
    private Double longitude;
    private String restaurantId;


    public Restaurant() {

    }

    public Restaurant(String name, String location, String type, String openTime, String closeTime, String telephone, String profileImageURL, ArrayList<String> imageDes, Double latitude, Double longitude, String restaurantId) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.telephone = telephone;
        this.profileImageURL = profileImageURL;
        this.imageDes = imageDes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.restaurantId = restaurantId;
    }

    protected Restaurant(Parcel in) {
        name = in.readString();
        location = in.readString();
        type = in.readString();
        openTime = in.readString();
        closeTime = in.readString();
        profileImageURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(type);
        dest.writeString(openTime);
        dest.writeString(closeTime);
        dest.writeString(profileImageURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {

            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }


    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ArrayList<String> getImageDes() {
        return imageDes;
    }

    public void setImageDes(ArrayList<String> imageDes) {
        this.imageDes = imageDes;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        restaurantId = restaurantId;
    }
}
