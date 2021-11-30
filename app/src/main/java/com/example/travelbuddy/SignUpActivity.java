package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

class User {
    private String name;
    private String email;
    private String password;
}

public class SignUpActivity extends AppCompatActivity {

    TextView sign_in;
    EditText nameField, emailField, passwordField, phoneField;
    Button sign_up;
    FirebaseAuth auth;
    String userId;
    FirebaseFirestore fire_store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

        nameField = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        sign_up = findViewById(R.id.sign_up_btn);
        sign_in = findViewById(R.id.sign_in);
        phoneField = findViewById(R.id.phone);


        sign_up.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String phone = phoneField.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                nameField.setError("Name is Empty");
                return;
            }

            if (phone.isEmpty()) {
                phoneField.setError("Phone is Empty");
            }

            if (phone.length() <= 10) {
                phoneField.setError("Phone must be at least 10 characters");
            }

            if (TextUtils.isEmpty(email)) {
                emailField.setError("Email is Empty");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordField.setError("Password is Empty");
                return;
            }

            if (password.length() < 6) {
                passwordField.setError("Password must be at-least 6 characters long");
                return;
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(T -> {
                if (T.isSuccessful()) {
                    userId = auth.getCurrentUser().getUid();
                    DocumentReference dr = fire_store.collection("users").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    user.put("phone", phone);

                    Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Error " + T.getException(), Toast.LENGTH_SHORT).show();
                }
            });


        });


        sign_in.setOnClickListener(v -> {
            finish();
        });
    }
}