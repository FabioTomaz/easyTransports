package com.transports.expandable_list.schedule_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.transports.R;
import com.transports.SchedulesViewerFragment;

import java.util.List;

public class TripAdapter extends ExpandableRecyclerViewAdapter<TripParentViewHolder, TripChildViewHolder> {

    private LayoutInflater mInflator;
    private SchedulesViewerFragment schedulesViewerFragment;

    public TripAdapter(List<? extends ExpandableGroup> groups, SchedulesViewerFragment schedulesViewerFragment) {
        super(groups);
        this.schedulesViewerFragment = schedulesViewerFragment;
    }

    @Override
    public TripParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_parent, parent, false);
        return new TripParentViewHolder(view, schedulesViewerFragment);
    }

    @Override
    public TripChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_child, parent, false);
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