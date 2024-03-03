package com.example.contactapp;

import android.util.TypedValue;
import android.view.Gravity;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.contactapp.databinding.ActivityContactPreviewBinding;
import com.google.gson.Gson;
import com.example.contactapp.Data.contactView;

import java.util.ArrayList;

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
                        dialPhone(null);
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

//        binding.callIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(ContactPreview.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
//                    dialPhone();
//                }else {
//                    resultLauncher.launch(Manifest.permission.CALL_PHONE);
//                }
//            }
//        });
    }

    private void dialPhone(String phoneNumber) {
        String phone = "tel:"+ Uri.parse(phoneNumber);
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

        if (!user.phone.isEmpty()){
//            binding.contactNumber.setText(user.phone);
            renderPhones(user.getPhone());
        }
    }

    private void renderPhones(ArrayList<String> phone) {
        // Assuming 'binding' is your ViewBinding instance
        LinearLayout parentLayout = binding.phone;


            for (String phoneNumber : phone) {
                // Create LinearLayout
                LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 50, 0, 0);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.dark_lining));
                linearLayout.setPadding(30, 30, 30, 30);
                linearLayout.setGravity(Gravity.CENTER);

                // Create TextView
                TextView textView = new TextView(this);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );
                textView.setLayoutParams(textParams);
                textView.setText(phoneNumber);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                textView.setTextColor(ContextCompat.getColor(this, R.color.white));

                // Create CardView
                CardView cardView = new CardView(this);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                cardView.setLayoutParams(cardParams);
//                cardView.setCardCornerRadius(getResources().getDimension(R.dimen.card_cornerRadius));
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(ContactPreview.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    dialPhone(phoneNumber);
                        }else {
                    resultLauncher.launch(Manifest.permission.CALL_PHONE);
                        }
                    }
                });

                // Create ImageView
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                35,
                                getResources().getDisplayMetrics()
                        ),
                        (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                35,
                                getResources().getDisplayMetrics()
                        )
                );

                imageView.setLayoutParams(imageParams);
                imageView.setImageResource(R.drawable.phone_icon);

                // Add TextView and CardView to LinearLayout
                linearLayout.addView(textView);
                cardView.addView(imageView);
                linearLayout.addView(cardView);

                // Add LinearLayout to parent layout
                parentLayout.addView(linearLayout);
        }

    }
}