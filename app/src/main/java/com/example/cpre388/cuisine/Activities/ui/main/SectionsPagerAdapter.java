package com.example.cpre388.cuisine.Activities.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cpre388.cuisine.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_3 };
    private final Context mContext;

    /**
     * Fragment Pager Adapter constructor
     * @param context - context
     * @param fm - Fragment Manager
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Initializes a new fragment based on tab position:
     * @param position - tab position
     * @return - fragment corresponding to position
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0: fragment = new ReservationFragment(); break;
            case 1: fragment = new OverviewFragment();break;
            }

        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragment;
    }

    /**
     * Returns Page Title based on position
     * @param position - current tab position
     * @return - returns tab title based on tab position
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    /**
     * gets number of tabs to show
     * @return - number of tabs to show
     */
    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}