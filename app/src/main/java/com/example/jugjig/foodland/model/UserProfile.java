package com.example.jugjig.foodland.model;

public class UserProfile {

    /**************************************
     * intent information about restaurant*
     **************************************/
    private String fName,lName,phone,email,desc,role;

    private static UserProfile restProfileInstance;

   public static UserProfile getRestProfileInstance(){
       if (restProfileInstance == null){
           restProfileInstance = new UserProfile();
       }
       return restProfileInstance;
   }

    private UserProfile() {
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static void setRestProfileInstance(UserProfile restProfileInstance) {
        UserProfile.restProfileInstance = restProfileInstance;
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
