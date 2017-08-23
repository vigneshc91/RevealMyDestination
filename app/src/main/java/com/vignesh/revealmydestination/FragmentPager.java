package com.vignesh.revealmydestination;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.vignesh.revealmydestination.Model.Trip;

/**
 * Created by vignesh on 18/6/17.
 */

public class FragmentPager extends FragmentPagerAdapter implements ListTripFragment.OnListFragmentInteractionListener {

    int pageCount = 2;

    private ListTripFragment listTripFragment;
    private CreateTripFragment createTripFragment;

    public FragmentPager(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                listTripFragment = new ListTripFragment();
                return  listTripFragment;
            case 1:
                createTripFragment = new CreateTripFragment();
                return createTripFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.pageCount;
    }

    @Override
    public void onListFragmentInteraction(Trip trip) {
        createTripFragment.onListFragmentInteraction(trip);
    }
}
