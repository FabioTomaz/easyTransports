package com.transports.expandable_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.transports.R;

import java.util.List;

public class TripAdapter extends ExpandableRecyclerViewAdapter<TripParentViewHolder, TripChildViewHolder> {

    private LayoutInflater mInflator;

    public TripAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public TripParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_parent, parent, false);
        return new TripParentViewHolder(view);
    }

    @Override
    public TripChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedules_row, parent, false);
        return new TripChildViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(TripChildViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final TripChild artist = ((TripParent) group).getItems().get(childIndex);
        holder.setTripChild(artist);
    }

    @Override
    public void onBindGroupViewHolder(TripParentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTripParent((TripParent) group);
    }
}