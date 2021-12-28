package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;


public class place_view extends AppCompatActivity {

    TextView placeName, placeDescription;
    ImageView place_view_image;
    Button view_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);

        placeName = findViewById(R.id.placeName);
        placeDescription = findViewById(R.id.description);
        place_view_image = findViewById(R.id.place_view_image);
        view_button = findViewById(R.id.view_on_map);

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