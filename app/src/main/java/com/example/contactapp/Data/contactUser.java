package com.example.contactapp.Data;

import java.util.ArrayList;

public class contactUser {

    private String name;
    private String id;
    private ArrayList<String> phoneNumbers;

    public contactUser(String name, String id, ArrayList<String> phoneNumbers) {
        this.name = name;
        this.id = id;
        this.phoneNumbers = phoneNumbers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters
    public String getName() {
        return name;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public String getId() {
        return id;
    }
}
