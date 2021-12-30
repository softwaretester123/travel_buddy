package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.travelbuddy.Places.Adapter;
import com.example.travelbuddy.Places.PlaceModelClass;
import com.example.travelbuddy.PlannedTrip.PlannedTripAdapter;
import com.example.travelbuddy.PlannedTrip.PlannedTripModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlannedTrips extends AppCompatActivity {

    String res = "";
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<PlannedTripModel> plannedTrips;
    PlannedTripAdapter adapter;
    TextView result;
    List<String> planned_trips;
    List<String> planned_dates;

    FirebaseAuth auth;

    FirebaseFirestore fire_store;

    private PlannedTripAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_trips);

        initData();
        initRecyclerView();

    }

    private void initData() {

        auth = FirebaseAuth.getInstance();
        fire_store = FirebaseFirestore.getInstance();
        plannedTrips = new ArrayList<>();


        String userId = auth.getCurrentUser().getUid();


        fire_store.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                result = findViewById(R.id.res);
                User user = documentSnapshot.toObject(User.class);

                planned_trips = user.getPlanned_trips();
                planned_dates = user.getPlanned_dates();


                for (int i = 0; i < planned_trips.size(); i++){
                    plannedTrips.add(new PlannedTripModel(planned_trips.get(i), planned_dates.get(i)));
                    Log.d("Place", planned_trips.get(i));
                    res = res + "" + planned_trips.get(i) + "  -  " + planned_dates.get(i) + "\n";
                    adapter.notifyDataSetChanged();
                }
                result.setText(res);

            }
        });


    }

    private void initRecyclerView() {
        setOnClickListener();
        recyclerView = findViewById(R.id.planned_trips_list);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlannedTripAdapter(plannedTrips, listener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setOnClickListener () {
        listener = new PlannedTripAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {

            }
        };
    }

}