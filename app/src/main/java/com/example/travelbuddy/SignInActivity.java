package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Button sign_in;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        sign_in = findViewById(R.id.sign_in_btn);

        sign_in.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                emailField.setError("Email is empty");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordField.setError("Password is empty");
                return;
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(T -> {
                if (T.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                    homeIntent.putExtra("username", email);
                    startActivity(homeIntent);
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            });

        });


        TextView sign_up = findViewById(R.id.sign_up);
        sign_up.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(signUpIntent);
        });
    }



}