package com.example.contactapp;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.contactapp.Fragments.MainFragment;
import com.example.contactapp.databinding.ActivityHomePageBinding;


public class HomePage extends AppCompatActivity {
    private ActivityHomePageBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        setupInterface();
    }

    private void setupInterface() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,MainFragment.newInstance(getSupportFragmentManager()));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof MainFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
