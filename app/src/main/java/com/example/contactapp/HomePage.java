package com.example.contactapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.example.contactapp.Adapters.HomePageAdapter;
import com.example.contactapp.databinding.ActivityHomePageBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomePage extends AppCompatActivity {
    ActivityHomePageBinding bind;
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        setupInterface();
        getUserPermission();
    }

    private void setupInterface() {
        ViewPager2 viewPager2 = bind.mainViewpager;
        TabLayout tabLayout = bind.mainTab;
        viewPager2.setAdapter(new HomePageAdapter(this));
//        new TabLayoutMediator(tabLayout,viewPager2,true,true,null);
        new TabLayoutMediator(tabLayout,viewPager2, (tab,position)->{
            tab.setText(createText(position));
            tab.setIcon(createIcon(position));
        }).attach();
    }

    private String createText(int position){
        String tagName = null;
        switch (position){
            case 0:
                tagName = "Call";
                break;
            case 1:
                tagName = "Contacts";
                break;
        }
        return tagName;
    }

    private int createIcon(int position){
        int icon = R.drawable.answer_call;
        switch (position){
            case 0:
                icon = R.drawable.answer_call;
                break;
            case 1:
                icon = R.drawable.contact;
                break;
        }

        return icon;
    }

    private void getUserPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
        }else {
            requestMultiplePermissionsLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    isGranted -> {
                        if (isGranted.containsValue(true)) {
                            getUserPermission();
                        }
                    }
            );

            String[] permissions = {android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS, android.Manifest.permission.CALL_PHONE, android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.POST_NOTIFICATIONS};
            requestMultiplePermissionsLauncher.launch(permissions);
        }
    }
}