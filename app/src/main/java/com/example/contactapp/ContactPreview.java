package com.example.contactapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.contactapp.databinding.ActivityContactPreviewBinding;
import com.google.gson.Gson;
import com.example.contactapp.Data.contactView;

public class ContactPreview extends AppCompatActivity {

    contactView user;

    ActivityContactPreviewBinding binding;
    ActivityResultLauncher<String> resultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactPreviewBinding.inflate(getLayoutInflater());
        String data = getIntent().getStringExtra("data");
        if (data != null){
            user = new Gson().fromJson(data, contactView.class);
            renderData(user);
            eventLister();
        }
        resultLauncher= registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted->{
                    if (isGranted){
                        dialPhone();
                    }
                }
        );
        setContentView(binding.getRoot());
    }

    private void eventLister() {
        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactPreview.super.onBackPressed();
            }
        });

        binding.optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pop = new PopupMenu(ContactPreview.this,binding.optionButton);
                pop.getMenuInflater().inflate(R.menu.options_menu, pop.getMenu());
                pop.show();
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.edit){
                            Toast.makeText(ContactPreview.this, "edited", Toast.LENGTH_SHORT).show();
                            return true;
                        }else if (menuItem.getItemId() == R.id.delete){
                            Toast.makeText(ContactPreview.this, "deleted", Toast.LENGTH_SHORT).show();
                            return true;
                        }else {
                            return false;
                        }
                    }
                });
            }
        });

        binding.callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ContactPreview.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    dialPhone();
                }else {
                    resultLauncher.launch(Manifest.permission.CALL_PHONE);
                }
            }
        });
    }

    private void dialPhone() {
        String phone = "tel:"+ Uri.parse(user.phone);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(phone));
        startActivity(callIntent);
    }

    @SuppressLint("SetTextI18n")
    private void renderData(contactView user) {
        binding.contactName.setText(user.name);
        if (user.email != null){
            binding.contactEmail.setText(user.email);
        }else {
            binding.contactEmail.setText("No email");
        }

        if (user.phone != null){
            binding.contactNumber.setText(user.phone);
        }else {
            binding.contactNumber.setText("Number does not exist");
        }
    }
}