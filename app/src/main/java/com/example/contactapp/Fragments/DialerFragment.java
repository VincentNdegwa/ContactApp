package com.example.contactapp.Fragments;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.example.contactapp.Adapters.DialerPageAdapter;
import com.example.contactapp.R;
import com.example.contactapp.databinding.FragmentDialerBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.transition.MaterialElevationScale;


public class DialerFragment extends Fragment {

    FragmentDialerBinding bind;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    private Boolean openDialer =false;


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




    public static DialerFragment newInstance(String param1, String param2) {
        DialerFragment fragment = new DialerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}