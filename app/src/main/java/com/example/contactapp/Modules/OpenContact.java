package com.example.contactapp.Modules;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;

import com.example.contactapp.Data.contactView;

import java.util.ArrayList;

public class OpenContact {


    @SuppressLint("Range")
    public static contactView Open(Context context, String contactId, String Name) {
        contactView contactView= null;

        String selection = ContactsContract.Contacts.DISPLAY_NAME + " =?";
        String[] selectionArgs = new String[]{Name};
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null){
            if (cursor.moveToFirst()){
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String emailAddress = getEmailAddress(context.getContentResolver(), contactId);
                byte[] photoByte = getPhotoUri(context.getContentResolver(), contactId);
                Bitmap photoBimap = getBitmapFromBytes(photoByte);
                ArrayList<String> phoneNumber = getPhoneNumber(context.getContentResolver(), contactId,contactName);
                contactView user = new contactView(contactName,contactId,phoneNumber,emailAddress,photoBimap);
                contactView = user;
            }
            cursor.close();
        }
        return contactView;
    }
    @SuppressLint("Range")
    private static byte[] getPhotoUri(ContentResolver contentResolver, String contactId) {
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO};
        String selection = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        Cursor cursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                new String[]{contactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE},
                null
        );

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    byte[] photo = cursor.getBlob(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
                    return photo;
                }
            } finally {
                cursor.close();
            }
        }

        return null;
    }
//

    @SuppressLint("Range")

    private static String getEmailAddress(ContentResolver contentResolver, String contactId) {
        String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID+" =?";
        String[] selectionArgs = new String[]{contactId};
        Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
                );

        if (emailCursor != null){
            if (emailCursor.moveToFirst()){
                String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                emailCursor.close();
                return  email;
            }
        }
        emailCursor.close();
        return null;
    }
    @SuppressLint("Range")
    private static ArrayList<String> getPhoneNumber(ContentResolver contentResolver, String contactId, String name) {
        ArrayList<String> phoneNumbers = new ArrayList<>();

        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " =?";
        String[] selectionArgs = new String[]{name};

        Cursor phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumbers.add(phoneNumber);
            }
            phoneCursor.close();
        }

        return phoneNumbers;
    }


    private static Bitmap getBitmapFromBytes(byte[] bytes){
        if (bytes != null){
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }
        return null;
    }
}