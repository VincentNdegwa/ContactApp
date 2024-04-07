package com.example.contactapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.contactapp.Adapters.CallLogsAdapter;
import com.example.contactapp.Data.CallDetails;
import com.example.contactapp.R;
import com.example.contactapp.databinding.FragmentDialerAllFragementBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class DialerIncommingFragment extends Fragment {

    public ArrayList<CallDetails> callDetails;
    public FragmentDialerAllFragementBinding binding;

    public DialerIncommingFragment() {
    }

    public static DialerIncommingFragment newInstance(String param1, String param2) {
        DialerIncommingFragment fragment = new DialerIncommingFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDialerAllFragementBinding.inflate(getLayoutInflater());
        getDataFromSharedPref();
        renderData();
        return binding.getRoot();
    }
    private void getDataFromSharedPref() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CallLogs", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("logs","");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CallDetails>>(){}.getType();
        ArrayList<CallDetails> callsData = gson.fromJson(json,type);
        callDetails = new ArrayList<>();
        for(CallDetails call: callsData ){
            if ("Incoming".equals(call.getType())){
                callDetails.add(call);
            }
        }

    }
    private void renderData() {
        CallLogsAdapter adapter = new CallLogsAdapter(callDetails, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = binding.callLogRecyclerview;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}