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

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.SubActivityViewHolder> {

    private final LayoutInflater inflater;
    List<Ticket> subActivityData = Collections.EMPTY_LIST;

    public TicketListAdapter(Context context, List<Ticket> subActivityData) {
        inflater = LayoutInflater.from(context);
        this.subActivityData = subActivityData;
    }


    @Override
    public SubActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.ticket_item, parent, false);
        SubActivityViewHolder subActivityViewHolder = new SubActivityViewHolder(view);
        return subActivityViewHolder;
    }

    @Override
    public void onBindViewHolder(SubActivityViewHolder holder, int position) {
        Ticket currentCard = subActivityData.get(position);
        holder.title.setText(currentCard.getOriginDestination());

    }

    @Override
    public int getItemCount() {
        return subActivityData.size();
    }

    class SubActivityViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public SubActivityViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.card_text);
        }
    }
}