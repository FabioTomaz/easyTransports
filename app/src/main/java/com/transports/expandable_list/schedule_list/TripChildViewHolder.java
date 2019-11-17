package com.transports.expandable_list.schedule_list;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.transports.R;

public class TripChildViewHolder extends ChildViewHolder {

    private TextView transportName;
    private TextView destination;
    private TextView departureHour;
    private TextView arrivalHour;
    private TextView price;

    public TripChildViewHolder(View itemView) {
        super(itemView);

        transportName = itemView.findViewById(R.id.transport_name);
        destination = itemView.findViewById(R.id.trip);
        departureHour = itemView.findViewById(R.id.hour_departure);
        price = itemView.findViewById(R.id.price);
    }

    public void setTripChild(TripChild trip) {
        transportName.setText(trip.getCompanyName());
        departureHour.setText(trip.getDepartureHour() +" - "+trip.getArrivingHour());
        destination.setText(trip.getOrigin().getStopName()+" -> "+trip.getDestination().getStopName());
        Log.d("price", trip.getPrice()+"$");
        price.setText(trip.getPrice() + "€");
    }
}