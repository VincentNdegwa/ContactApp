package com.example.contactapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.contactapp.Adapters.ContactsAdapter;
import com.example.contactapp.Data.contactUser;
import com.example.contactapp.Data.contacts;
import com.example.contactapp.Modules.SearchContacts;
import com.example.contactapp.databinding.FragmentContactBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class ContactFragment extends Fragment {
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;
    private Context context;


    FragmentContactBinding binding;
    public ContactFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        binding = FragmentContactBinding.inflate(getLayoutInflater());
        eventListener();
        getManyContacts();
        return binding.getRoot();
    }

    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void eventListener() {
        binding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String search = charSequence.toString();
                ArrayList<contactUser> contactsResults  =  SearchContacts.Search((Context) context, search);
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




    @SuppressLint("Range")
    public void getManyContacts() {
        ArrayList<contactUser> contacts = new ArrayList<>();
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        };

        Cursor cs = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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
            com.example.contactapp.Data.contacts myContacts = new contacts(contacts);


            if (myContacts.getContactsArray() != null && !myContacts.getContactsArray().isEmpty()) {
                removeVisibility();
                renderContacts(myContacts.getContactsArray());
            } else {
                renderNoContacts();
            }
        }
    }


    private void renderContacts(ArrayList<contactUser> contactsArray) {

        ContactsAdapter adapter = new ContactsAdapter(context,contactsArray);
        RecyclerView recView = binding.renderRecyclerview;

        LinearLayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
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