package com.transports.schedules;

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
    private final SchedulesViewerV2Fragment.OnFragmentInteractionListener mListener;

    List<TripChild> tripChildren = Collections.EMPTY_LIST;

    public TripAdapter(Context context, List<TripChild> subActivityData, SchedulesViewerV2Fragment.OnFragmentInteractionListener mListener) {
        mInflator = LayoutInflater.from(context);
        this.tripChildren = subActivityData;
        this.mListener = mListener;
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
        holder.itemView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                if (tripChildren.get(position).getTripID().equals("")){
                    return;
                }
                mListener.onFragmentInteraction(tripChildren.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripChildren.size();
    }
}