package com.example.contactapp.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.example.contactapp.Adapters.DialerPageAdapter;
import com.example.contactapp.R;
import com.example.contactapp.databinding.FragmentDialerBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class DialerFragment extends Fragment {

    FragmentDialerBinding bind;
    ViewPager2 viewPager2;
    TabLayout tabLayout;


    public DialerFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentDialerBinding.inflate(getLayoutInflater());
        viewPager2 = bind.dialerViewPager;
        tabLayout = bind.dialerTab;
        setupUI(viewPager2,tabLayout);
        return bind.getRoot();
    }

    private void setupUI(ViewPager2 viewPager2, TabLayout tabLayout) {
        viewPager2.setAdapter(new DialerPageAdapter(getActivity()));
        new TabLayoutMediator(tabLayout,viewPager2, (tab, position) ->{
            tab.setText("Tab"+position);
        }).attach();
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