package com.example.contactapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.contactapp.Data.contactUser;
import com.example.contactapp.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchContactAdapter extends RecyclerView.Adapter<SearchContactAdapter.ViewHolder>{
    public ArrayList<contactUser> contactUsers;
    public Context context;
    public SearchContactAdapter(ArrayList<contactUser> contactUsers, Context context){
        this.contactUsers = contactUsers;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public SearchContactAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(card);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public TextView userPhone;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.contact_name);
            userPhone = itemView.findViewById(R.id.contact_phone);
        }

    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchContactAdapter.ViewHolder holder, int position) {
        contactUser contact = contactUsers.get(position);
        holder.username.setText(contact.getName());
        holder.userPhone.setText(contact.getPhoneNumbers().get(0));
    }

    @Override
    public int getItemCount() {
        return contactUsers.size();
    }
}
