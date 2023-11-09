package com.example.contactapp.Data;

import java.util.ArrayList;

public class contacts {
    public ArrayList<contactUser> contactsArray;

    public contacts(ArrayList<contactUser> contactsArray) {
        this.contactsArray = contactsArray;
    }

    public void setContactsArray(ArrayList<contactUser> contactsArray) {
        this.contactsArray = contactsArray;
    }

    public ArrayList<contactUser> getContactsArray() {
        return contactsArray;
    }

}
