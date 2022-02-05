package com.example.travelbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Home extends AppCompatActivity {

    TextView welcomeText;
    Button logout;
    Button searchButton, karnataka, andhra, tamilnadu, kerala;
    FirebaseAuth auth;
    Button drawerOpenButton;
    FirebaseFirestore fire_store;

    public static User u;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        fire_store = FirebaseFirestore.getInstance();

        String userId = auth.getCurrentUser().getUid();
        drawerOpenButton = findViewById(R.id.drawer_open_btn);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        karnataka = findViewById(R.id.front);
        andhra = findViewById(R.id.andhra);
        tamilnadu = findViewById(R.id.tamilnadu);
        kerala = findViewById(R.id.kerala);

        karnataka.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Karnataka.class);
            startActivity(i);
        });

        andhra.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Andhra.class);
            startActivity(i);
        });

        tamilnadu.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), TamilNadu.class);
            startActivity(i);
        });

        kerala.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Kerala.class);
            startActivity(i);
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miItem1:
                        Toast.makeText(getApplicationContext(), "Planned Trips selected", Toast.LENGTH_SHORT).show();
                        Intent plannedTrips = new Intent(getApplicationContext(), PlannedTrips.class);
                        startActivity(plannedTrips);
                        break;
                    case R.id.miItem2:
                        Toast.makeText(getApplicationContext(), "About Us selected", Toast.LENGTH_SHORT).show();
                        Intent restaurant = new Intent(getApplicationContext(), AboutUs.class);
                        startActivity(restaurant);
                        break;
                }
                return true;
            }
        });

        drawerOpenButton.setOnClickListener(v -> {
            welcomeText = findViewById(R.id.welcome_text);
            Log.v("user", "" + userId);
            u = new User();

            Map<String, Object> userData = new HashMap<>();

            fire_store.collection("users").document(userId)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        welcomeText.setText("Welcome " + name);
                    } else {
                        Toast.makeText(getApplicationContext(), "Document Does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                }
            });

            drawerLayout.open();
        });

        searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SearchInMap.class);
            startActivity(i);
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {

            Log.v("User", "Paramesh");
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