package com.transports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.transports.utils.Constants;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private com.google.android.material.bottomnavigation.BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(this);
        int menuID = getIntent().getIntExtra(Constants.MENU_INTENT, R.id.bottom_menu_tickets);
        navigateToMenu(menuID);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return navigateToMenu(item.getItemId());
    }

    public boolean navigateToMenu(int menuID){
        switch (menuID) {
            case R.id.bottom_menu_tickets: {
                openFragment(new TicketsFragment());
                break;
            }
            case R.id.bottom_menu_schedule: {
                openFragment(new SchedulesFragment());
                break;
            }
            case R.id.bottom_menu_settings: {
                openFragment(new AccountSettingsFragment());
                break;
            }
        }
        return true;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
