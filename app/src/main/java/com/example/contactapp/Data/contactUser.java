package com.example.contactapp.Data;

import java.util.ArrayList;

public class contactUser {

    public String name;
    public String id;
    public String phone;

    public contactUser(String name, String id, String phone) {
        this.name = name;
        this.id = id;
        this.phone = phone;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String name){
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }
//    getters


    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
    public String getId() {
        return id;
    }
}
