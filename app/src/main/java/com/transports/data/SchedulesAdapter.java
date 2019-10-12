package com.transports.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transports.R;

import java.util.ArrayList;

public class SchedulesAdapter extends RecyclerView.Adapter<SchedulesAdapter.SchedulesHolder> {

    // List to store all the contact details
    private ArrayList<Schedule> schedulesList;
    private Context mContext;

    // Constructor for the Class
    public SchedulesAdapter(ArrayList<Schedule> schedulesList, Context context) {
        this.schedulesList = schedulesList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public SchedulesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.schedules_row, parent, false);
        return new SchedulesHolder(view);
    }

    @Override
    public int getItemCount() {
        return schedulesList == null? 0: schedulesList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull SchedulesHolder holder, final int position) {
        final Schedule schedule = schedulesList.get(position);

        // Set the data to the views here
        holder.setTransportName(schedule.getCompanyName());
        holder.setDestination(schedule.getDestination());

        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class SchedulesHolder extends RecyclerView.ViewHolder {

        private TextView transportName;
        private TextView destination;

        public SchedulesHolder(View itemView) {
            super(itemView);

            transportName = itemView.findViewById(R.id.transport_name);
            destination = itemView.findViewById(R.id.destination);
        }

        public void setTransportName(String name) {
            transportName.setText(name);
        }

        public void setDestination(String destination) {
            this.destination.setText(destination);
        }
    }
}