package com.example.contactapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.contactapp.R;


public class DialerOutgoingFragment extends Fragment {



    public DialerOutgoingFragment() {
        // Required empty public constructor
    }
    public static DialerOutgoingFragment newInstance(String param1, String param2) {
        DialerOutgoingFragment fragment = new DialerOutgoingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialer_outgoing, container, false);
    }
}