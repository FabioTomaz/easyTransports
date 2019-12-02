package com.transports.expandable_list.tickets_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.transports.R;
import com.transports.TicketUseActivity;
import com.transports.utils.UtilityFunctions;

import java.util.Collections;
import java.util.List;

import static com.transports.utils.Constants.TICKET_EXPIRED;
import static com.transports.utils.Constants.TICKET_EXTRA_INTENT;
import static com.transports.utils.Constants.TICKET_VALID;

/**
 * Adapter used for list of individual tickets, that make a global ticket. This ticket is a single trip, from one stop to another in 1 transportaion
 * This is used to place information in each item of the list
 */
public class MyTicketsListAdapter extends RecyclerView.Adapter<MyTicketsListAdapter.MyTicketViewHolder> {

    private final LayoutInflater inflater;
    List<Ticket> tickets = Collections.EMPTY_LIST;

    public MyTicketsListAdapter(Context context, List<Ticket> subActivityData) {
        inflater = LayoutInflater.from(context);
        this.tickets = subActivityData;
    }


    @Override
    public MyTicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ticket_item_details, parent, false);
        MyTicketViewHolder ticketViewHolder = new MyTicketViewHolder(view);
        return ticketViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyTicketViewHolder holder, final int position) {
        Ticket currentTicket = tickets.get(position);
        holder.originDestination.setText(currentTicket.getOriginDestination());
        holder.transports.setText(currentTicket.getTransports());
        holder.transportIcon.setImageResource(UtilityFunctions.getIconOfTransport(currentTicket.getTransports()));
        holder.schedule.setText(currentTicket.getSchedule());
        holder.status.setText(currentTicket.getState());
        //set status text color if ticket is ready to user (green) or already used (red)
        if (currentTicket.getState().equalsIgnoreCase(TICKET_EXPIRED))
            holder.status.setTextColor(Color.parseColor(("#ff0000")));
        else if (currentTicket.getState().equalsIgnoreCase(TICKET_VALID))
            holder.status.setTextColor(Color.parseColor(("#00ff00")));

        holder.useTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((RecyclerView) parent).performItemClick(v, position, 0); // Let the event be handled in onItemClick()
                Intent intent = new Intent(v.getContext(), TicketUseActivity.class);
                //Log.d("ticketPos", tickets.get(holder.getAdapterPosition())+"");
                intent.putExtra(TICKET_EXTRA_INTENT, tickets.get(holder.getAdapterPosition()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class MyTicketViewHolder extends RecyclerView.ViewHolder {

        TextView originDestination;
        TextView schedule;
        TextView transports;
        ImageView transportIcon;
        TextView status;
        Button useTicketButton;

        public MyTicketViewHolder(View itemView) {
            super(itemView);
            originDestination = (TextView) itemView.findViewById(R.id.ticket_origin_destination);
            schedule = (TextView) itemView.findViewById(R.id.ticket_hours);
            transports = (TextView) itemView.findViewById(R.id.transport_name);
            status = (TextView) itemView.findViewById(R.id.ticket_state);
            useTicketButton = (Button) itemView.findViewById(R.id.use_ticket_btn);
            transportIcon = (ImageView) itemView.findViewById(R.id.transport_icon);
        }
    }
}