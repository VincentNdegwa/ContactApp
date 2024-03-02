package com.example.contactapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.contactapp.Adapters.ContactsAdapter;
import com.example.contactapp.Data.contactUser;
import com.example.contactapp.Data.contacts;
import com.example.contactapp.Modules.SearchContacts;
import com.example.contactapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchData();
        eventListener();

    }

    private void eventListener() {
        binding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String search = charSequence.toString();
                ArrayList<contactUser> contactsResults  =  SearchContacts.Search((Context) MainActivity.this, search);
                if (contactsResults != null && contactsResults.size()>0){
                    removeVisibility();
                    renderContacts(contactsResults);
                }else {
                    renderNoContacts();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    private void fetchData() {
        System.out.println("fetch data is running");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            getManyContacts();
        }else {
            requestMultiplePermissionsLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    isGranted -> {
                        if (isGranted.containsValue(true)) {
                            fetchData();
                        }
                    }
            );

            String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
            requestMultiplePermissionsLauncher.launch(permissions);
        }
    }


    @SuppressLint("Range")
    public void getManyContacts() {
        ArrayList<contactUser> contacts = new ArrayList<>();
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        };

        Cursor cs = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        Log.d("contactCount", String.valueOf(cs.getCount()));

        if (cs != null && cs.getCount() > 0) {
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

            // Now, 'contacts' array contains contacts with multiple phone numbers
            contacts myContacts = new contacts(contacts);


            if (myContacts.getContactsArray() != null && myContacts.getContactsArray().size() > 0) {
                removeVisibility();
                renderContacts(myContacts.getContactsArray());
            } else {
                renderNoContacts();
            }
        }
    }


    private void renderContacts(ArrayList<contactUser> contactsArray) {

        ContactsAdapter adapter = new ContactsAdapter(this,contactsArray);
        RecyclerView recView = binding.renderRecyclerview;

        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layout);
        recView.setAdapter(adapter);

    }

    private void renderNoContacts() {
        System.out.println("no contacts");
        binding.renderRecyclerview.setVisibility(View.INVISIBLE);
        binding.noContactsText.setVisibility(View.VISIBLE);
    }
    private void removeVisibility(){
        binding.renderRecyclerview.setVisibility(View.VISIBLE);
        binding.noContactsText.setVisibility(View.INVISIBLE);
    }
}
