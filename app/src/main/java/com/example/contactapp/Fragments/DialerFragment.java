package com.example.contactapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.ArrayMap;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.example.contactapp.Adapters.DialerPageAdapter;
import com.example.contactapp.Data.CallDetails;
import com.example.contactapp.MyViewModels.CallLogViewModel;
import com.example.contactapp.R;
import com.example.contactapp.databinding.FragmentDialerBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.util.*;


public class DialerFragment extends Fragment {

    FragmentDialerBinding bind;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    private Boolean openDialer =false;
    private ActivityResultLauncher<String> requestPermission;


    public DialerFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentDialerBinding.inflate(getLayoutInflater());
        viewPager2 = bind.dialerViewPager;
        tabLayout = bind.dialerTab;
        setupUI(viewPager2,tabLayout);
        setEventListeners();
        intiatePermission();
        return bind.getRoot();
    }

    private void setEventListeners() {
        bind.fabDialer.setOnClickListener(view -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new DailerPad())
                    .addToBackStack(null)
                    .commit();
        });
    }


    private void setupUI(ViewPager2 viewPager2, TabLayout tabLayout) {
        viewPager2.setAdapter(new DialerPageAdapter(getActivity()));
        new TabLayoutMediator(tabLayout,viewPager2, (tab, position) ->{
            View customTabView = LayoutInflater.from(requireContext()).inflate(R.layout.dialer_tab_layout, null);
            ImageView tabIcon = customTabView.findViewById(R.id.tab_icon);
            tabIcon.setImageResource(createIcon(position));
            tab.setCustomView(customTabView);
        }).attach();
    }
    private int createIcon(int position){
        int icon = R.drawable.answer_call;
        switch (position){
            case 0:
                icon = R.drawable.all_call;
                break;
            case 1:
                icon = R.drawable.incomming_call;
                break;
            case 2:
                icon = R.drawable.outgoing_call;
                break;
            case 3:
                icon = R.drawable.missed_call;
                break;
        }

        return icon;
    }
    private ArrayList<ArrayMap> allCallLog = new ArrayList<>();
    @SuppressLint("Range")

    private void fetchData() {

        CallLogViewModel callLogViewModel = new CallLogViewModel();
        callLogViewModel.getAllContactLogs(getContext(), null).observe(getViewLifecycleOwner(), callDetails -> {
            ArrayList<CallDetails> uniqueLatestCallLogs = getUniqueLatestCallLogs(callDetails);
            saveCallLogsToSharedPref(uniqueLatestCallLogs);

        });



    }


    private ArrayList<CallDetails> getUniqueLatestCallLogs(List<CallDetails> allCallLogs) {
        ArrayList<CallDetails> uniqueLatestCallLogs = new ArrayList<>();
        Set<String> encounteredKeys = new HashSet<>();

        int index = 0;
        int size = allCallLogs.size();

        // Loop until all call logs are processed
        while (index < size) {
            CallDetails callLog = allCallLogs.get(index);
            String key = callLog.getKey();
            if (!encounteredKeys.contains(key)){
                uniqueLatestCallLogs.add(callLog);
                encounteredKeys.add(key);
            }
            index++;
        }

        return uniqueLatestCallLogs;
    }

    private void saveCallLogsToSharedPref(ArrayList<CallDetails> callDetails) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CallLogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(callDetails);
        editor.putString("logs", json);
        editor.apply();
    }


    private boolean intiatePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_DENIED){
            fetchData();
            return true;
        }else {
            requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if (isGranted){
                    fetchData();
                }else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });
            requestPermission.launch(Manifest.permission.READ_CALL_LOG);
            return false;
        }
    }


    public static DialerFragment newInstance(String param1, String param2) {
        DialerFragment fragment = new DialerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}