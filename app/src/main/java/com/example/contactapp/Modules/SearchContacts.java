package com.example.contactapp.Modules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.contactapp.Data.contactUser;
import com.example.contactapp.Data.contacts;

import java.util.ArrayList;

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
            while (cs.moveToNext()){
                 String phone = cs.getString(cs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = cs.getString(cs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String id = cs.getString(cs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                contactUser user = new contactUser(name,id,phone);
                contacts.add(user);
            }
            cs.close();
            contacts myContacts = new contacts(contacts);
            return myContacts.getContactsArray();
        }

        return null;
    }
}
