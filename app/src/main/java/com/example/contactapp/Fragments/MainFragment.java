package com.example.contactapp.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.contactapp.R;
import com.example.contactapp.databinding.FragmentMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import org.jetbrains.annotations.NotNull;


public class MainFragment extends Fragment {

    FragmentMainBinding bind;
    private Context context;
    private String TAG = "MainFragment";
    private static FragmentManager fragmentManager;
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(FragmentManager fr) {
        MainFragment fragment = new MainFragment();
        fragmentManager = fr;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        bind = FragmentMainBinding.inflate(getLayoutInflater());
        loadFragment(new DialerFragment());
        setupInterface();
        getUserPermission();
        return bind.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupInterface() {
        bind.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment;
            if (item.getItemId() == R.id.menu_call) {
                fragment = new DialerFragment();
            } else if (item.getItemId() == R.id.menu_contact) {
                fragment = new ContactFragment();
            } else {
                return false;
            }
            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getUserPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestMultiplePermissionsLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    isGranted -> {
                        if (isGranted.containsValue(true)) {
                            getUserPermission(); // Request permissions again if not granted
                        }
                    }
            );

            String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_CALL_LOG};
            requestMultiplePermissionsLauncher.launch(permissions);
        }
    }
}