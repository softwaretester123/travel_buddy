package com.example.travelbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class Home extends AppCompatActivity {

    TextView  welcomeText;
    Button logout;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getEmail();

        welcomeText = findViewById(R.id.welcome_text);
        welcomeText.setText("Welcome "+user);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {

            Log.v("User", user);
            auth.signOut();
            Toast.makeText(Home.this, "Signed Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        });

    }
}