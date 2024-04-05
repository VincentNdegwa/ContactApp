package com.example.contactapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.contactapp.Fragments.ContactFragment;
import com.example.contactapp.Fragments.DialerFragment;
import com.example.contactapp.Fragments.MainFragment;
import com.example.contactapp.databinding.ActivityHomePageBinding;


public class HomePage extends AppCompatActivity {
    private ActivityHomePageBinding bind;
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        setupInterface();
    }

    private void setupInterface() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,MainFragment.newInstance(getSupportFragmentManager()));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("trying to resume");
    }
    //    private void setupInterface() {
//        bind.bottomNavigation.setOnItemSelectedListener(item -> {
//            Fragment fragment;
//            if (item.getItemId() == R.id.menu_call) {
//                fragment = new DialerFragment();
//            } else if (item.getItemId() == R.id.menu_contact) {
//                fragment = new ContactFragment();
//            } else {
//                return false;
//            }
//            loadFragment(fragment);
//            return true;
//        });
//    }
//
//    private void loadFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    private void getUserPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            // Permissions already granted
//        } else {
//            requestMultiplePermissionsLauncher = registerForActivityResult(
//                    new ActivityResultContracts.RequestMultiplePermissions(),
//                    isGranted -> {
//                        if (isGranted.containsValue(true)) {
//                            getUserPermission(); // Request permissions again if not granted
//                        }
//                    }
//            );
//
//            String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.POST_NOTIFICATIONS};
//            requestMultiplePermissionsLauncher.launch(permissions);
//        }
//    }
}
