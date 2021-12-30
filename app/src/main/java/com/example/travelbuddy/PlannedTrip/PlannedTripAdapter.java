package com.example.travelbuddy.PlannedTrip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;

import java.util.List;

public class PlannedTripAdapter extends RecyclerView.Adapter<PlannedTripAdapter.ViewHolder> {

    private List<PlannedTripModel> plannedTrips;

    private RecyclerViewClickListener listener;

    public PlannedTripAdapter(List<PlannedTripModel> planned_trips_list, PlannedTripAdapter.RecyclerViewClickListener listener) {
        this.plannedTrips = planned_trips_list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planned_trips_item_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = plannedTrips.get(position).getName();
        String date = plannedTrips.get(position).getDate();

        holder.setData(name, date);
    }

    public interface RecyclerViewClickListener {
        void onClick (View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameField, dateField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameField = itemView.findViewById(R.id.place_name_1);
            dateField = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        public void setData(String name, String date) {
            nameField.setText(name);
            dateField.setText(date);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }



    @Override
    public int getItemCount() {
        return 0;
    }
}
