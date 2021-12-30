package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {
    private String name;
    private String email;
    private String phone;
    private List<String> planned_trips;
    private List<String> planned_dates;

    public User() {

    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.planned_trips = new ArrayList<>();
        this.planned_dates = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getPlanned_trips() {
        return planned_trips;
    }

    public List<String> getPlanned_dates() {
        return planned_dates;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPlanned_trips(List<String> planned_trips) {
        this.planned_trips = planned_trips;
    }

    public void setPlanned_dates(List<String> planned_dates) {
        this.planned_dates = planned_dates;
    }
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

        fire_store = FirebaseFirestore.getInstance();

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

            if (phone.length() != 10) {
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

                    User newUser = new User(name, email, phone);
                    Map<String, Object> userData = new HashMap<>();
                    userData.put(userId, newUser);

                    fire_store.collection("users").document(userId)
                            .set(newUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Success", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error", "Error writing document", e);
                                }
                            });
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