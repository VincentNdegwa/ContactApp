package com.example.contactapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.contactapp.Fragments.DialerAllFragement;
import com.example.contactapp.Fragments.DialerIncommingFragment;
import com.example.contactapp.Fragments.DialerMissedFragment;
import com.example.contactapp.Fragments.DialerOutgoingFragment;
import org.jetbrains.annotations.NotNull;

public class DialerPageAdapter extends FragmentStateAdapter {
    public DialerPageAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 0:
               return new DialerAllFragement();
           case 1:
               return new DialerIncommingFragment();
           case 2:
               return new DialerOutgoingFragment();
           case 3:
               return new DialerMissedFragment();
       }
       return new DialerAllFragement();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
