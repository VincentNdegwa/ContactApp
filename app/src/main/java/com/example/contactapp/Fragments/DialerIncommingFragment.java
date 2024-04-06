package com.example.contactapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.contactapp.Data.CallDetails;
import com.example.contactapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class DialerIncommingFragment extends Fragment {



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
        getDataFromSharedPref();
        return inflater.inflate(R.layout.fragment_dialer_incomming, container, false);
    }
    private void getDataFromSharedPref() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CallLogs", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("logs","");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CallDetails>>(){}.getType();
        ArrayList<CallDetails> callDetails = gson.fromJson(json,type);
        ArrayList<CallDetails> Calls = new ArrayList<>();
        for(CallDetails call: callDetails ){
            if ("Incoming".equals(call.getType())){
                Calls.add(call);
            }
        }
    }
}