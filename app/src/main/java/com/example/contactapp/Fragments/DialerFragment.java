package com.example.contactapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.CallLog;
import android.telecom.Call;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Button;
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
import com.example.contactapp.R;
import com.example.contactapp.databinding.FragmentDialerBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.transition.MaterialElevationScale;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
    private ArrayMap<String, ArrayMap> allCallLog = new ArrayMap<>();
    @SuppressLint("Range")

    private void fetchData() {
        Log.d("TAG", "fetchData");
        String[] projection = {
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.PHONE_ACCOUNT_ID
        };

        String selection = null; // You can specify a selection if needed
        String[] selectionArgs = null; // You can specify selection arguments if needed
        String sortOrder = CallLog.Calls.NUMBER + " ASC, " + CallLog.Calls.DATE + " DESC"; // Order by number in ascending and date in descending order

        String groupBy = CallLog.Calls.NUMBER; // Group by phone number to get unique entries
        String having = "MAX(" + CallLog.Calls.DATE + ") = " + CallLog.Calls.DATE; // Filter only the latest call for each number

        Cursor cursor = getContext().getContentResolver().query(
                CallLog.Calls.CONTENT_URI,projection,selection,selectionArgs,sortOrder
        );
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    long timeInMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                    int sim = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID)); // Use PHONE_ACCOUNT_ID to get SIM info
                    int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

                    Date date = new Date(timeInMillis);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedTime = formatter.format(date);
                    String callType;
                    switch (type) {
                        case CallLog.Calls.INCOMING_TYPE:
                            callType = "Incoming";
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            callType = "Outgoing";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            callType = "Missed";
                            break;
                        default:
                            callType = "Unknown";
                    }

                    // Determine SIM information
                    String simInfo = sim == 1 ? "SIM1" : "SIM2";
                    ArrayMap<String, String> userLog = new ArrayMap<>();
                    if(allCallLog.containsKey(name+"-"+number)){
                        userLog.put("name", name);
                        userLog.put("number", number);
                        userLog.put("time", formattedTime);
                        userLog.put("sim", simInfo);
                        userLog.put("type", callType);
                        allCallLog.get(name+"-"+number).putAll(userLog);
                    }else {
                        userLog.put("name", name);
                        userLog.put("number", number);
                        userLog.put("time", formattedTime);
                        userLog.put("sim", simInfo);
                        userLog.put("type", callType);
                        allCallLog.put(name+"-"+number,userLog);
                    }

                }
            } finally {
                cursor.close(); // Close the cursor when done
            }
        } else {
            Log.d("TAG", "Cursor is null");
        }

        Log.d("Data", "fetchData: "+ allCallLog);
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