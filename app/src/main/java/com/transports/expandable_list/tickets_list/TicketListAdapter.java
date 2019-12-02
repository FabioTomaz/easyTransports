package com.transports.expandable_list.tickets_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.transports.R;

import java.util.Collections;
import java.util.List;

/**
 * Adapter used for list of global tickets, shown in cards
 */
public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.TicketViewHolder> {

    private final LayoutInflater inflater;
    List<TicketGlobal> subActivityData = Collections.EMPTY_LIST;

    public TicketListAdapter(Context context, List<TicketGlobal> subActivityData) {
        inflater = LayoutInflater.from(context);
        this.subActivityData = subActivityData;
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.ticket_item, parent, false);
        TicketViewHolder ticketViewHolder = new TicketViewHolder(view);
        return ticketViewHolder;
    }

    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
        TicketGlobal currentTicketGlobal = subActivityData.get(position);
        holder.originDestination.setText(currentTicketGlobal.getOriginDestination());
        holder.schedule.setText(currentTicketGlobal.getSchedule());
        holder.transport.setText(currentTicketGlobal.getTransports());
    }

    @Override
    public int getItemCount() {
        return subActivityData.size();
    }

    class TicketViewHolder extends RecyclerView.ViewHolder {

        TextView originDestination;
        TextView schedule;
        TextView transport;

        public TicketViewHolder(View itemView) {
            super(itemView);

            originDestination = (TextView) itemView.findViewById(R.id.ticket_origin_destination);
            schedule = (TextView) itemView.findViewById(R.id.ticket_hour);
            transport = (TextView) itemView.findViewById(R.id.ticket_transports);
        }
    }
}