package com.example.contactapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.contactapp.R;


public class DialerAllFragement extends Fragment {



    public DialerAllFragement() {
        // Required empty public constructor
    }


    public static DialerAllFragement newInstance(String param1, String param2) {
        DialerAllFragement fragment = new DialerAllFragement();
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
        return inflater.inflate(R.layout.fragment_dialer_all_fragement, container, false);
    }
}