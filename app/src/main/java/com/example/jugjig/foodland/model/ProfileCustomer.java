package com.example.jugjig.foodland.model;

public class ProfileCustomer {

    /**************************************
     * intent information about restaurant*
     **************************************/
    private String fName,lName,phone,email,role;

    private static ProfileCustomer cusProfileInstance;

    public static ProfileCustomer getCusProfileInstance(){
        if (cusProfileInstance == null){
            cusProfileInstance = new ProfileCustomer();
        }
        return cusProfileInstance;
    }

    private ProfileCustomer() {
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static void setCusProfileInstance(ProfileCustomer cusProfileInstance) {
        ProfileCustomer.cusProfileInstance = cusProfileInstance;
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




}
