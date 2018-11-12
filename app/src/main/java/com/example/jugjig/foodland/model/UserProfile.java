package com.example.jugjig.foodland.model;

public class UserProfile {

    /**************************************
     * intent information about restaurant*
     **************************************/
    private String fName,lName,phone,email,desc,role;

    private static UserProfile userProfileInstance;

   public static UserProfile getRestProfileInstance(){
       if (userProfileInstance == null){
           userProfileInstance = new UserProfile();
       }
       return userProfileInstance;
   }

    private UserProfile() {
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static void setRestProfileInstance(UserProfile userProfileInstance) {
        UserProfile.userProfileInstance = userProfileInstance;
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
