package com.example.travelbuddy.Places;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<PlaceModelClass> placesList;

    private RecyclerViewClickListener listener;

    public Adapter(List<PlaceModelClass> placesList, RecyclerViewClickListener listener) {
        this.placesList = placesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String resource = placesList.get(position).getPlace_image();
        String name = placesList.get(position).getPlace_name();

        holder.setData(resource, name);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public interface RecyclerViewClickListener {
        void onClick (View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView placeImage;
        private TextView placeName, placeDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.image_view);
            placeName = itemView.findViewById(R.id.place_name);
            itemView.setOnClickListener(this);
        }


        public void setData(String resource, String name) {
            LoadImage loadImage = new LoadImage(placeImage);
            loadImage.execute(resource);
            placeName.setText(name);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }

        public class LoadImage extends AsyncTask<String, Void, Bitmap> {

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
    }
}
