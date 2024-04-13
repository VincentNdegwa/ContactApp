package com.example.contactapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contactapp.Adapters.ViewCallLogAdapter;
import com.example.contactapp.Data.CallDetails;
import com.example.contactapp.MyViewModels.CallLogViewModel;
import com.example.contactapp.R;
import com.example.contactapp.databinding.FragmentFullLogViewBinding;

import java.util.List;

public class FullLogViewFragment extends Fragment {
    public FragmentFullLogViewBinding binding;
    public String phoneNumber;
    public FullLogViewFragment() {
        // Required empty public constructor
    }

    public static FullLogViewFragment newInstance(String phoneNumber) {
        FullLogViewFragment fragment = new FullLogViewFragment();
        Bundle args = new Bundle();
        args.putString("phone", phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            phoneNumber = getArguments().getString("phone");
        }

        CallLogViewModel callLogViewModel = new CallLogViewModel();
        callLogViewModel.getAllContactLogs(getContext(),phoneNumber).observe(this, callDetails -> {
            renderData(callDetails);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFullLogViewBinding.inflate(getLayoutInflater());
        setEvents();
        return  binding.getRoot();
    }

    private void renderData(List<CallDetails> callDetails) {
        ViewCallLogAdapter adapter = new ViewCallLogAdapter(callDetails,getContext());
        RecyclerView recyclerView = binding.fullLogRecyclerview;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setEvents() {
    }
}