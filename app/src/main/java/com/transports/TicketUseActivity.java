package com.transports;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.transports.expandable_list.tickets_list.Ticket;

import static com.transports.utils.Constants.TICKET_EXTRA_INTENT;

public class TicketUseActivity extends AppCompatActivity {

    private Ticket ticket;
    private TextView transport;
    private TextView originDestination;
    private TextView schedule;
    private TextView duration;
    private TextView ticketState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_use);

        //set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        transport = (TextView) findViewById(R.id.ticket_transport);
        originDestination = (TextView) findViewById(R.id.ticket_origin_destination);
        schedule = (TextView) findViewById(R.id.ticket_schedule);
        duration = (TextView) findViewById(R.id.ticket_duration);
        ticketState = (TextView) findViewById(R.id.ticket_state);

        this.ticket = (Ticket) getIntent().getSerializableExtra(TICKET_EXTRA_INTENT);
        //set ticket info in right place
        transport.setText(ticket.getTransports());
        originDestination.setText(ticket.getOriginDestination());
        schedule.setText(ticket.getSchedule());
        duration.setText(ticket.getDuration());
        ticketState.setText(ticket.getState());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
