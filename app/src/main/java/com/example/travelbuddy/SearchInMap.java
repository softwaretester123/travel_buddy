package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Place {
    private final String name;
    private final Double lon;
    private final Double lat;
    private final String state;
    private final String address;

    public Place(String name, Double lat, Double lon, String state, String address) {
        this.name = name;
        this.lon = (double) (Math.round(lon*1000.0)/1000.0);
        this.lat = (double) (Math.round(lat*1000.0)/1000.0);
        this.state = state;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }

    public String getState() {
        return state;
    }

    public String getAddress() {
        return address;
    }

    @NonNull
    @Override
    public String toString() {
        return "" + name + "  -  " + state + "";
    }
}

public class SearchInMap extends AppCompatActivity {

    AppCompatButton logout;
    FirebaseAuth auth;

    SearchView searchView;
    ListView search_result;

    ArrayList<Place> result_items;
    ArrayAdapter<Place> search_result_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_in_map);

        searchView = findViewById(R.id.search_view);
        search_result = findViewById(R.id.search_result);

        result_items = new ArrayList<Place>();

        search_result.setOnItemClickListener((parent, view, position, id) -> {
            Place selectedPlace = result_items.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("geo:"+selectedPlace.getLat()+", "+selectedPlace.getLon()+""));
            Log.d("COORDS LON-LAT", ""+selectedPlace.getLon()+"---"+selectedPlace.getLat());
            intent.setData(Uri.parse("geo:<" + selectedPlace.getLat() + ">,<" + selectedPlace.getLon() + ">?q=<" + selectedPlace.getLat() + ">,<" + selectedPlace.getLon() + ">(" + selectedPlace.getName() + ")"));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                RequestQueue queue = Volley.newRequestQueue(SearchInMap.this);
                String url = "https://api.geoapify.com/v1/geocode/autocomplete?text=" + s + "&apiKey=a5fd74a70fe54fcf9ebf992ca330117f";
                result_items.clear();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                    Log.v("Response: ", "Response is: " + response);
                    String jsonString = response.toString();
                    try {
                        JSONObject obj = new JSONObject(jsonString);
                        JSONArray features = obj.getJSONArray("features");
                        if (features.length() > 0) {
                            for (int i = 0; i < features.length(); i++) {
                                String name;
                                if (features.getJSONObject(i).getJSONObject("properties").has("name")) {
                                    name = features.getJSONObject(i).getJSONObject("properties").getString("name");
                                } else if (features.getJSONObject(i).getJSONObject("properties").has("old_name")) {
                                    name = features.getJSONObject(i).getJSONObject("properties").getString("old_name");
                                } else {
                                    name = s;
                                }


                                Double lat = features.getJSONObject(i).getJSONObject("properties").getDouble("lat");
                                Double lon = features.getJSONObject(i).getJSONObject("properties").getDouble("lon");
                                String state = features.getJSONObject(i).getJSONObject("properties").getString("state");
                                String address = features.getJSONObject(i).getJSONObject("properties").getString("address_line2");

                                Place newPlace = new Place(name, lat, lon, state, address);

                                result_items.add(newPlace);

                                Log.v("Place: ", "" + name + " " + lat + " " + lon + " " + state + " " + address);
                            }
                            search_result_adapter = new ArrayAdapter<Place>(SearchInMap.this, android.R.layout.simple_list_item_1, result_items);
                            search_result.setAdapter(search_result_adapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.v("Error: ", "Error occurred");
                });

                queue.add(stringRequest);
                return false;
            }
        });


        search_result_adapter = new ArrayAdapter<Place>(this, android.R.layout.simple_list_item_1, result_items);

        search_result.setAdapter(search_result_adapter);

        logout = findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();

        logout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(SearchInMap.this, "Signed Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        });

    }
}