package com.example.contactapp.Data;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class contactView {
    public String name;
    public String id;
    public ArrayList<String> phone;
    public Bitmap image;

    public String email;



    public contactView(String name, String id, ArrayList<String> phone, String email, Bitmap image) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.image = image;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public Bitmap getImage() {
        return image;
    }
}
