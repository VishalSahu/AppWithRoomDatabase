package com.anr.appwithroomdatabase.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.anr.appwithroomdatabase.R;
import com.anr.appwithroomdatabase.fragments.HomeFragment;
import com.anr.appwithroomdatabase.fragments.SigInFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SharedPreferences sp = getSharedPreferences("Session", MODE_PRIVATE);
        if (!sp.getString("current_email", "").isEmpty()) {
            activeUserHomePage(sp, fragmentTransaction);
        } else {
            fragmentTransaction.replace(R.id.main_activity_layout, new SigInFragment()).commit();
        }
    }

    private void activeUserHomePage(SharedPreferences sp, FragmentTransaction fragmentTransaction) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HomeFragment.ARG_EMAIL, sp.getString("current_email", "no_email"));
        homeFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_activity_layout, homeFragment).commit();
    }
}