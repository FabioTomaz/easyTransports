package com.transports.expandable_list.schedules;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.transports.R;
import com.transports.utils.UtilityFunctions;

public class TripChildViewHolder extends ChildViewHolder {

    private TextView transportName;
    private TextView destination;
    private TextView departureHour;
    private ImageView transportIcon;
    private TextView price;

    public TripChildViewHolder(View itemView) {
        super(itemView);

        transportName = itemView.findViewById(R.id.transport_name_schedule);
        destination = itemView.findViewById(R.id.trip);
        departureHour = itemView.findViewById(R.id.hour_departure);
        price = itemView.findViewById(R.id.price);
        transportIcon = itemView.findViewById(R.id.transport_sch_icon);
    }

    public void setTripChild(TripChild trip) {
        transportName.setText(trip.getCompanyName());
        departureHour.setText(trip.getDepartureHour() +" - "+trip.getArrivingHour());
        price.setText(trip.getPrice() + "€");
        if (trip.getOrigin().equals(trip.getDestination()))
            destination.setText(trip.getOrigin().getStop_name());
        else if(trip.getCompanyName().equalsIgnoreCase("walk")) {
            destination.setText(trip.getOrigin().getStop_name() + " (" + trip.getOrigin().getAgency_key() + ") -> "
                    + trip.getDestination().getStop_name() + " (" + trip.getDestination().getAgency_key() + ")");
            departureHour.setText("");
            price.setText("");
        }
        else
            destination.setText(trip.getOrigin().getStop_name()+" -> "+trip.getDestination().getStop_name());
        transportIcon.setImageResource(UtilityFunctions.getIconOfTransport(trip.getCompanyName()));
        Log.d("price", trip.getPrice()+"€");

    }
}