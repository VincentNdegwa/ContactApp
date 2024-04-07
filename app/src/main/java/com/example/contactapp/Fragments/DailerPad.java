package com.example.contactapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.contactapp.Adapters.SearchContactAdapter;
import com.example.contactapp.Data.contactUser;
import com.example.contactapp.Modules.SearchContacts;
import com.example.contactapp.R;
import com.example.contactapp.databinding.FragmentDailerPadBinding;

import java.util.ArrayList;

public class DailerPad extends Fragment {

    private FragmentDailerPadBinding bind;
    public String userInput;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentDailerPadBinding.inflate(getLayoutInflater());
        setEventListeners();
        return bind.getRoot();
    }

    public DailerPad() {
        // Required empty public constructor
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setEventListeners() {
        bind.userInputDisplay.setOnTouchListener((view, motionEvent) -> {
            view.onTouchEvent(motionEvent);
            bind.userInputDisplay.setFocusableInTouchMode(true);
            bind.userInputDisplay.setShowSoftInputOnFocus(false);
            int touchPosition = bind.userInputDisplay.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
            bind.userInputDisplay.setSelection(touchPosition);
            return false;
        });
        bind.oneButton.setOnClickListener(view -> onNumberButtonClick("1"));
        bind.twoButton.setOnClickListener(view -> onNumberButtonClick("2"));
        bind.threeButton.setOnClickListener(view -> onNumberButtonClick("3"));
        bind.fourButton.setOnClickListener(view -> onNumberButtonClick("4"));
        bind.fiveButton.setOnClickListener(view -> onNumberButtonClick("5"));
        bind.sixButton.setOnClickListener(view -> onNumberButtonClick("6"));
        bind.sevenButton.setOnClickListener(view -> onNumberButtonClick("7"));
        bind.eightButton.setOnClickListener(view -> onNumberButtonClick("8"));
        bind.nineButton.setOnClickListener(view -> onNumberButtonClick("9"));
        bind.starButton.setOnClickListener(view -> onNumberButtonClick("*"));
        bind.zeroButton.setOnClickListener(view -> onNumberButtonClick("0"));
        bind.hashButton.setOnClickListener(view -> onNumberButtonClick("#"));
        bind.deleteButton.setOnClickListener(view -> onDeleteButtonClick());
        bind.intiateCall.setOnClickListener(view -> initiateCall(userInput));

        bind.userInputDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<contactUser> searchData = SearchContacts.Search(getContext(),charSequence.toString());
                if (searchData!= null && !searchData.isEmpty()){
                    renderSearchData(searchData);
                    System.out.println(searchData);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bind.downKeypad.setOnClickListener(view -> {
            bind.dialerLayout.setVisibility(View.INVISIBLE);
        });
        bind.fabDialer.setOnClickListener(view -> {
            bind.dialerLayout.setVisibility(View.VISIBLE);
        });
    }

    private void renderSearchData(ArrayList<contactUser> searchData) {
        RecyclerView recyclerView = bind.searchResults;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        SearchContactAdapter adapter = new SearchContactAdapter(searchData,getContext());
        recyclerView.setAdapter(adapter);;
        recyclerView.setLayoutManager(layoutManager);
    }


    public void onNumberButtonClick(String number) {
        String currentInput = bind.userInputDisplay.getText().toString();
        int selectionPosition = bind.userInputDisplay.getSelectionStart();
        String addedInput = currentInput.substring(0,selectionPosition)+number+currentInput.substring(selectionPosition);
        userInput = addedInput;
        bind.userInputDisplay.setText(addedInput);
        bind.userInputDisplay.setSelection(selectionPosition + 1);
    }

    public void onDeleteButtonClick() {
        String currentInput = bind.userInputDisplay.getText().toString();
        int selectionPosition = bind.userInputDisplay.getSelectionStart();
        if (!currentInput.isEmpty() && selectionPosition>0) {
            String updatedInput = currentInput.substring(0, selectionPosition -1)+ currentInput.substring(selectionPosition);
            userInput = updatedInput;
            bind.userInputDisplay.setText(updatedInput);
            bind.userInputDisplay.setSelection(selectionPosition-1);
        }
    }
    private void initiateCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        startActivity(intent);
    }

    public static DailerPad newInstance(String param1, String param2) {
        DailerPad fragment = new DailerPad();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}