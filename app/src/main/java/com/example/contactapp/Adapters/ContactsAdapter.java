package com.example.contactapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.ContactPreview;
import com.example.contactapp.Data.contactUser;
import com.example.contactapp.Data.contactView;
import com.example.contactapp.Modules.OpenActivity;
import com.example.contactapp.Modules.OpenContact;
import com.example.contactapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    Context context;
    ArrayList<contactUser> contactsArray;
    public ContactsAdapter(Context context, ArrayList<contactUser> contactsArray) {
        this.contactsArray = contactsArray;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView contactName;
        public TextView contactPhone;
        public View rootView;
        public ImageView contactIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             contactName = itemView.findViewById(R.id.contact_name);
             contactPhone = itemView.findViewById(R.id.contact_phone);
             contactIcon = itemView.findViewById(R.id.icon_contact);
             rootView =itemView;
        }


    }
    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        contactUser contact = contactsArray.get(position);
        holder.contactPhone.setText(contact.phone);
        holder.contactName.setText(contact.name);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                view.setBackgroundColor(ContextCompat.getColor(context,R.color.variantdark));
                contactUser selectedContact = contactsArray.get(holder.getAdapterPosition());
              contactView user =  OpenContact.Open(context,selectedContact.id);
              if (user != null){
                  Gson gson = new Gson();
                  String userData = gson.toJson(user);
                  OpenActivity.Open(context, ContactPreview.class, userData);
              }
            }
        });


    }

    @Override
    public int getItemCount() {
        return contactsArray.size();
    }
}