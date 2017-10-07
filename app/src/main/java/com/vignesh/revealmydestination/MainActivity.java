package com.vignesh.revealmydestination;

import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.vignesh.revealmydestination.Model.Trip;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements ListTripFragment.OnListFragmentInteractionListener {

    FragmentPager fragmentPager;
    ViewPager viewPager;

    String[] pageTitles = {"Your Trips", "Create Trip", "Edit Trip"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBarTitle(pageTitles[0]);

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentPager = new FragmentPager(fragmentManager);

        viewPager.setAdapter(fragmentPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setActionBarTitle(pageTitles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public ViewPager getViewPager(){
        return viewPager;
    }

    public void setActionBarTitle(String title){
        setTitle(title);
    }

    @Override
    public void onListFragmentInteraction(Trip trip) {
        Log.d("listener", trip.getSrc_location());
        setActionBarTitle(pageTitles[2]);
        fragmentPager.onListFragmentInteraction(trip);
//        FragmentPager.tripId = trip.getId();
//        viewPager.setCurrentItem(2);
    }
}
