package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import com.example.contactapp.Adapters.ViewCallLogAdapter;
import com.example.contactapp.Data.CallDetails;
import com.example.contactapp.Fragments.FullLogViewFragment;
import com.example.contactapp.MyViewModels.CallLogViewModel;
import com.example.contactapp.databinding.ActivityCallLogViewBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CallLogView extends AppCompatActivity {
    ActivityCallLogViewBinding binding;
    ViewCallLogAdapter adapter;
    public String phoneNumber;
    public String phoneName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        phoneName = intent.getStringExtra("phoneName");

        binding = ActivityCallLogViewBinding.inflate(getLayoutInflater());
        getData(phoneNumber);
        setContentView(binding.getRoot());
        setEventListeners();
        setDataOnUi(phoneNumber,phoneName);
    }

    private void getData(String phoneNumber) {
        CallLogViewModel callLogViewModel = new CallLogViewModel();
        callLogViewModel.getLimitedContactLogs(this,phoneNumber).observe(this, callDetails -> {
            System.out.println(callDetails);
                renderData(callDetails);
        });

    }

    private void renderData(List<CallDetails> callDetails) {
        adapter = new ViewCallLogAdapter(callDetails,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = binding.callLogRecyclerview;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setEventListeners() {
        binding.viewAllButton.setOnClickListener(view -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.log_view_fragment, FullLogViewFragment.newInstance(phoneNumber));
            transaction.addToBackStack(null);
            transaction.commit();
        });
        binding.logToolBar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void setDataOnUi(String phoneNumber, String phoneName) {
        binding.userName.setText(phoneName);
        binding.callLogNumber.setText(phoneNumber);
    }
}