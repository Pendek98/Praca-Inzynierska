package com.example.inzynierka.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.inzynierka.FirstFragment;
import com.example.inzynierka.ForthFragment;
import com.example.inzynierka.ReadRecipeFragment;
import com.example.inzynierka.CreateRecipeFragment;
import com.example.inzynierka.SecondFragment;
import com.example.inzynierka.ThirdFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public FragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: return new SecondFragment();
            case 2: return new ThirdFragment();
            case 3: return new ForthFragment();
            case 4: return new CreateRecipeFragment();
            case 5: return new ReadRecipeFragment();
        }
        return new FirstFragment();
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
