package com.example.jugjig.foodland.model;

import android.content.ContentValues;

public class UserProfileRegis {

    /**************************************
     * intent information about restaurant*
     **************************************/
    private String fName,lName,phone,email,desc,role;

    private static UserProfileRegis userProfileInstance;

    public static UserProfileRegis getRestProfileInstance() {
        if (userProfileInstance == null) {
            userProfileInstance = new UserProfileRegis();
        }
        return userProfileInstance;
    }

    private UserProfileRegis() {
    }

    public UserProfileRegis(String fName, String lName, String phone, String email, String desc, String role) {
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.email = email;
        this.desc = desc;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static void setRestProfileInstance(UserProfileRegis userProfileInstance) {
        UserProfileRegis.userProfileInstance = userProfileInstance;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



}
