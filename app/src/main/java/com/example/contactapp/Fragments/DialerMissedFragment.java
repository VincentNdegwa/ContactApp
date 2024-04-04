package com.example.contactapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.contactapp.R;


public class DialerMissedFragment extends Fragment {


    public DialerMissedFragment() {
        // Required empty public constructor
    }

    public static DialerMissedFragment newInstance(String param1, String param2) {
        DialerMissedFragment fragment = new DialerMissedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialer_missed, container, false);
    }
}