package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class place_view extends AppCompatActivity {

    TextView placeName, placeDescription;
    ImageView place_view_image;
    Button view_button, add_to_planned_trips;
    List<String> planned_trips, planned_dates;
    FirebaseAuth auth;
    FirebaseFirestore fire_store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);

        placeName = findViewById(R.id.placeName);
        placeDescription = findViewById(R.id.description);
        place_view_image = findViewById(R.id.place_view_image);
        view_button = findViewById(R.id.view_on_map);
        add_to_planned_trips = findViewById(R.id.add_to_planned_trips);

        planned_trips = new ArrayList<>();
        planned_dates = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        fire_store = FirebaseFirestore.getInstance();

        Bundle b = getIntent().getExtras();
        String name = b.getString("name");
        String description = b.getString("description");
        String imageUrl = b.getString("imageUrl");
        String lat = b.getString("lat");
        String lon = b.getString("lon");
        placeName.setText(name);
        placeDescription.setText(description);
        LoadImage loadImage = new LoadImage(place_view_image);
        loadImage.execute(imageUrl);

        view_button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:<" + lat + ">,<" + lon + ">?q=<" + lat + ">,<" + lon + ">(" + name + ")"));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

        add_to_planned_trips.setOnClickListener(v -> {

            Log.d("Tag", "Date Set");
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(place_view.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String userId = auth.getCurrentUser().getUid();
                            fire_store.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    User user = documentSnapshot.toObject(User.class);

                                    planned_trips = user.getPlanned_trips();
                                    planned_dates = user.getPlanned_dates();

                                    planned_trips.add(planned_trips.size(), name);
                                    planned_dates.add(planned_dates.size(), "" + day + "-" + month + "-" + year + "");

                                    user.setPlanned_trips(planned_trips);
                                    user.setPlanned_dates(planned_dates);

                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put(userId, user);
                                    fire_store.collection("users").document(userId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("Success", "Update Success");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Failure", e.toString());
                                        }
                                    });

                                    fire_store.collection("users").document(userId)
                                            .set(user)
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

                                }
                            });
//                            add_to_planned_trips.setText(planned_trips.get(0));
                        }
                    }, year, month, dayOfMonth);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

//        Log.d("Planned Trips", planned_trips.toString());

    }
}

class LoadImage extends AsyncTask<String, Void, Bitmap> {

    ImageView image_view;

    public LoadImage(ImageView placeImage) {
        this.image_view = placeImage;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlLink = strings[0];
        Bitmap bitmap = null;
        try {
            InputStream is = new java.net.URL(urlLink).openStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image_view.setImageBitmap(bitmap);
    }
}