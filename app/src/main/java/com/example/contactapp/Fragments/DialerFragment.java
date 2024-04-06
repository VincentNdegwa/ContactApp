package com.example.contactapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.CallLog;
import android.telecom.Call;
import android.text.TextUtils;
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
import java.util.*;


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
    private ArrayList<ArrayMap> allCallLog = new ArrayList<>();
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

        String selection = null;
        String[] selectionArgs = null;

        Cursor cursor = getContext().getContentResolver().query(
                CallLog.Calls.CONTENT_URI, projection, selection, selectionArgs, CallLog.Calls.DATE + " DESC"
        );
        if (cursor != null) {
            try {
                // Move to the first entry
                cursor.moveToFirst();
                do {
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    long timeInMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                    int sim = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID)); // Use PHONE_ACCOUNT_ID to get SIM info
                    int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

                    Date date = new Date(timeInMillis);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedTime = formatter.format(date);
                    String callType = getCallType(type);

                    // Determine SIM information
                    String simInfo = sim == 1 ? "SIM1" : "SIM2";
                    String key = !TextUtils.isEmpty(name) ? name : number;


                    ArrayMap<String, String> userLog = new ArrayMap<>();
                    userLog.put("name", name);
                    userLog.put("number", number);
                    userLog.put("sim", simInfo);
                    userLog.put("time", formattedTime);
                    userLog.put("type", callType);
                    allCallLog.add(userLog);

                    Log.d("CallLog", "Name: " + name + ", Number: " + number + ", Time: " + formattedTime + ", SIM: " + simInfo + ", Type: " + callType);
                } while (cursor.moveToNext());
            } finally {
                cursor.close(); // Close the cursor when done
            }
        } else {
            Log.d("TAG", "Cursor is null");
        }

        ArrayList<ArrayMap> uniqueLatestCallLogs = getUniqueLatestCallLogs(allCallLog);
        System.out.println(uniqueLatestCallLogs);

    }

    private String getCallType(int type) {
        String callType = "Unknown";
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
        return callType;
    }

    private ArrayList<ArrayMap> getUniqueLatestCallLogs(ArrayList<ArrayMap> allCallLogs) {
        ArrayList<ArrayMap> uniqueLatestCallLogs = new ArrayList<>();
        Set<String> encounteredNumbers = new HashSet<>();
        Set<String> encounteredNames = new HashSet<>();

        int index = 0;
        int size = allCallLogs.size();

        // Loop until all call logs are processed
        while (index < size) {
            ArrayMap<String, String> callLog = allCallLogs.get(index);
            String number = callLog.get("number");
            String name = callLog.get("name");

            // Check if the number or name has already been encountered
            if (!encounteredNumbers.contains(number) && !encounteredNames.contains(name)) {
                uniqueLatestCallLogs.add(callLog);
                encounteredNumbers.add(number);
                encounteredNames.add(name);
            }

            index++;
        }

        return uniqueLatestCallLogs;
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