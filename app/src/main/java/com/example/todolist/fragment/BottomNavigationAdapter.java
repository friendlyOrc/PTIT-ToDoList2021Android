package com.example.todolist.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class BottomNavigationAdapter  extends FragmentStatePagerAdapter {
    private int numPage = 3;

    public BottomNavigationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new HomeFragment();
            case 1: return new NewsFragment();
            case 2: return new UtilityFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return numPage;
    }
}
