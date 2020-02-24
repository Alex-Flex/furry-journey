package com.alexflex.soft.itipgu.logic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.alexflex.soft.itipgu.fragments.ITINewsFragment;
import com.alexflex.soft.itipgu.fragments.NewsFeedFragment;

public class mPagerAdapter extends FragmentPagerAdapter {

    public int currentItem = 0;

    public mPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            currentItem = 1;
            return new ITINewsFragment();
        }
        currentItem = 0;
        return new NewsFeedFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
