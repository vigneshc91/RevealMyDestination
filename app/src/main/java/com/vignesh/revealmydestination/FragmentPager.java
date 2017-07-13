package com.vignesh.revealmydestination;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by vignesh on 18/6/17.
 */

public class FragmentPager extends FragmentPagerAdapter {

    int pageCount = 2;

    public FragmentPager(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ListTripFragment listTripFragment = new ListTripFragment();
                return  listTripFragment;
            case 1:
                CreateTripFragment createTripFragment = new CreateTripFragment();
                return createTripFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.pageCount;
    }


}
