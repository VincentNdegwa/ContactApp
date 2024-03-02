package com.example.contactapp.Modules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.contactapp.Data.contactUser;
import com.example.contactapp.Data.contacts;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchContacts {
    @SuppressLint("Range")
    public static ArrayList<contactUser> Search(Context context, String search){

        String selection = ContactsContract.Contacts.DISPLAY_NAME+" LIKE?";
        String[] selectionArgs = new String[]{"%"+search+"%"};
        String[] projection =  new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        };

        ArrayList<contactUser> contacts = new ArrayList<>();
        Cursor cs = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,selection,selectionArgs,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

        Log.d("contactCount", String.valueOf(cs.getCount()));
        if (cs != null && cs.getCount()>0){
            HashMap<String, ArrayList<String>> contactsMap = new HashMap<>();

            while (cs.moveToNext()) {
                String phone = cs.getString(cs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = cs.getString(cs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String id = cs.getString(cs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                // Check if the name already exists in the map
                if (contactsMap.containsKey(name)) {
                    // If yes, add the phone number to the existing list
                    contactsMap.get(name).add(phone);
                } else {
                    // If no, create a new list with the phone number
                    ArrayList<String> phoneNumbers = new ArrayList<>();
                    phoneNumbers.add(phone);
                    contactsMap.put(name, phoneNumbers);

                    // Create and add the contact user
                    contactUser user = new contactUser(name, id, phoneNumbers);
                    contacts.add(user);
                }
            }

            cs.close();
            contacts myContacts = new contacts(contacts);
            return myContacts.getContactsArray();
        }

        return null;
    }
}
