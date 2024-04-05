package com.example.contactapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.contactapp.R;
import com.example.contactapp.databinding.FragmentDailerPadBinding;

public class DailerPad extends Fragment {

    private FragmentDailerPadBinding bind;
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
        bind.intiateCall.setOnClickListener(view -> initiateCall());
    }



    public void onNumberButtonClick(String number) {
        String currentInput = bind.userInputDisplay.getText().toString();
        int selectionPosition = bind.userInputDisplay.getSelectionStart();
        String addedInput = currentInput.substring(0,selectionPosition)+number+currentInput.substring(selectionPosition);
        bind.userInputDisplay.setText(addedInput);
        bind.userInputDisplay.setSelection(selectionPosition + 1);
    }

    public void onDeleteButtonClick() {
        String currentInput = bind.userInputDisplay.getText().toString();
        int selectionPosition = bind.userInputDisplay.getSelectionStart();
        if (!currentInput.isEmpty() && selectionPosition>0) {
            String updatedInput = currentInput.substring(0, selectionPosition -1)+ currentInput.substring(selectionPosition);
            bind.userInputDisplay.setText(updatedInput);
            bind.userInputDisplay.setSelection(selectionPosition-1);
        }
    }
    public void onNumberButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        String currentInput = bind.userInputDisplay.getText().toString();
        bind.userInputDisplay.setText(currentInput + buttonText);
    }

    public void onDeleteButtonClick(View view) {
        String currentInput = bind.userInputDisplay.getText().toString();
        if (!currentInput.isEmpty()) {
            bind.userInputDisplay.setText(currentInput.substring(0, currentInput.length() - 1));
        }
    }
    private void initiateCall() {
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