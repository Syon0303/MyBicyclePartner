package com.example.myBicycleMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public TabPagerAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                SpeedometerFragment speedometerFragment = new SpeedometerFragment();
                return speedometerFragment;
            case 1:
                FixFragment fixFragment = new FixFragment();
                return fixFragment;
            case 2:
                CoffeeFragment coffeeFragment = new CoffeeFragment();
                return coffeeFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
