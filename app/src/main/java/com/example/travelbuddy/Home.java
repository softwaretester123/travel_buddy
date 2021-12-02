package com.example.travelbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class Home extends AppCompatActivity {

    TextView welcomeText;
    Button logout;
    Button searchButton;
    FirebaseAuth auth;
    Button drawerOpenButton;

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getEmail();
        drawerOpenButton = findViewById(R.id.drawer_open_btn);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miItem1:
                        Toast.makeText(getApplicationContext(), "Restaurant selected", Toast.LENGTH_SHORT).show();
                        Intent restaurant = new Intent(getApplicationContext(), Restaurants.class);
                        startActivity(restaurant);
                        break;
                    case R.id.miItem2:
                        Toast.makeText(getApplicationContext(), "Planned Trips selected", Toast.LENGTH_SHORT).show();
                        Intent plannedTrips = new Intent(getApplicationContext(), PlannedTrips.class);
                        startActivity(plannedTrips);
                        break;
                    case R.id.miItem3:
                        Toast.makeText(getApplicationContext(), "Reviews selected", Toast.LENGTH_SHORT).show();
                        Intent reviews = new Intent(getApplicationContext(), Reviews.class);
                        startActivity(reviews);
                        break;
                }
                return true;
            }
        });

        drawerOpenButton.setOnClickListener(v -> {
            welcomeText = findViewById(R.id.welcome_text);
            welcomeText.setText("Welcome "+user);
            drawerLayout.open();
        });

        searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SearchInMap.class);
            startActivity(i);
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {

            Log.v("User", user);
            auth.signOut();
            Toast.makeText(Home.this, "Signed Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}