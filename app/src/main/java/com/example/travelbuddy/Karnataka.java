package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.travelbuddy.Places.Adapter;
import com.example.travelbuddy.Places.PlaceModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Karnataka extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<PlaceModelClass> placesList;
    Adapter adapter;

    FirebaseFirestore fire_store;

    private Adapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karnataka);

        initData();
        initRecyclerView();




    }

    private void initData() {

        fire_store = FirebaseFirestore.getInstance();
        placesList = new ArrayList<>();

        fire_store.collection("places").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("TAG", "Error" + error.getMessage());
                }

                for (DocumentSnapshot document : value) {
                    String id = document.getId().toString();
                    String url = document.getString("imageUrl");
                    String name = document.getString("name");
                    String description = document.getString("description");
                    String state = document.getString("state");
                    String lat = document.getString("lat");
                    String lon = document.getString("lon");
                    Log.d("TAG", "ID: " + id);

                    assert state != null;
                    if (state.equals("Karnataka"))
                    placesList.add(new PlaceModelClass(id, url, name, description, lon, lat));
                    adapter.notifyDataSetChanged();
                }

                Log.d("TAG", placesList.get(0).getPlace_name());
            }
        });



    }

    private void initRecyclerView() {
        setOnClickListener();
        recyclerView = findViewById(R.id.placesList);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(placesList, listener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setOnClickListener () {
        listener = new Adapter.RecyclerViewClickListener() {

            @Override
            public void onClick(View v, int position) {
                Intent i = new Intent (getApplicationContext(), place_view.class);
                i.putExtra("name", placesList.get(position).getPlace_name());
                i.putExtra("imageUrl", placesList.get(position).getPlace_image());
                i.putExtra("description", placesList.get(position).getPlace_description());
                i.putExtra("lat", placesList.get(position).getLat());
                i.putExtra("lon", placesList.get(position).getLon());
                startActivity(i);
            }
        };
    }

}