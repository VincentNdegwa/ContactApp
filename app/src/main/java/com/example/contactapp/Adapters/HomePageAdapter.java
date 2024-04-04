package com.example.contactapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.contactapp.Fragments.ContactFragment;
import com.example.contactapp.Fragments.DialerFragment;
import org.jetbrains.annotations.NotNull;

public class HomePageAdapter extends FragmentStateAdapter {


    public HomePageAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DialerFragment();
            case 1:
                return new ContactFragment();
        }
        return new DialerFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
