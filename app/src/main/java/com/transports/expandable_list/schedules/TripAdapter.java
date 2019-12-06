package com.transports.expandable_list.schedules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transports.R;

import java.util.Collections;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripChildViewHolder> {

    private LayoutInflater mInflator;
    List<TripChild> tripChildren = Collections.EMPTY_LIST;

    public TripAdapter(Context context, List<TripChild> subActivityData) {
        mInflator = LayoutInflater.from(context);
        this.tripChildren = subActivityData;
    }

    @Override
    public TripChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflator.inflate(R.layout.trip_child, parent, false);
        TripChildViewHolder ticketViewHolder = new TripChildViewHolder(view);
        return ticketViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripChildViewHolder holder, int position) {
        TripChild currentTrip = tripChildren.get(position);
        holder.setTripChild(currentTrip);

        /*holder.useTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((RecyclerView) parent).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
                Intent intent = new Intent(v.getContext(), TicketUseActivity.class);
                //Log.d("ticketPos", tickets.get(holder.getAdapterPosition())+"");
                intent.putExtra(TICKET_EXTRA_INTENT, tickets.get(holder.getAdapterPosition()));
                v.getContext().startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return tripChildren.size();
    }
}