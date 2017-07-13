package com.vignesh.revealmydestination;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentPager fragmentPager = new FragmentPager(fragmentManager);

        viewPager.setAdapter(fragmentPager);
    }

    public void setActionBarTitle(String title){
        setTitle(title);
    }
}
