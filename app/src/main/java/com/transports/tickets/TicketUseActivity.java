package com.transports.tickets;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.transports.R;
import com.transports.utils.UtilityFunctions;

import java.io.IOException;

import static com.transports.utils.Constants.TICKET_EXTRA_INTENT;

public class TicketUseActivity extends AppCompatActivity {

    private Ticket ticket;
    private TextView transport;
    private ImageView transportIcon;
    private TextView ticketID;
    private TextView originDestination;
    private TextView schedule;
    private TextView duration;
    private TextView ticketState;
    private ImageView qrCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_use);

        //set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        transport = (TextView) findViewById(R.id.ticket_transport);
        ticketID = (TextView) findViewById(R.id.ticket_id);
        transportIcon= (ImageView) findViewById(R.id.ticket_transport_icon);
        originDestination = (TextView) findViewById(R.id.ticket_origin_destination);
        schedule = (TextView) findViewById(R.id.ticket_schedule);
        duration = (TextView) findViewById(R.id.ticket_duration);
        ticketState = (TextView) findViewById(R.id.ticket_state);
        qrCode = (ImageView) findViewById(R.id.qr_code_view);

        this.ticket = (Ticket) getIntent().getSerializableExtra(TICKET_EXTRA_INTENT);
        //set ticket info in right place
        transport.setText(ticket.getTransports());
        transportIcon.setImageResource(UtilityFunctions.getIconOfTransport(ticket.getTransports()));
        ticketID.setText("TicketID: "+ticket.getId());
        originDestination.setText(ticket.getOriginDestination());
        schedule.setText(ticket.getSchedule());
        duration.setText(ticket.getDuration());
        ticketState.setText(ticket.getState());
        Log.d("ticketDetails", ticket.getDetails());
        Log.d("qrcode", ticket.getDetails());
        try {
            Bitmap qrCodeTicket = getQRCodeImage(ticket.getDetails(), 500, 500);
            qrCode.setImageBitmap(qrCodeTicket);
        } catch (WriterException e) {
            e.printStackTrace();
            showQRErrorAndExit();
        } catch (IOException e) {
            e.printStackTrace();
            showQRErrorAndExit();
        }
    }

    private void showQRErrorAndExit(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.ticket_qr_code_error_title))
                .setMessage(getString(R.string.ticket_qr_code_error))
                .setIcon(android.R.drawable.ic_dialog_alert).setNeutralButton("OK", null).show();
        finish();
    }

    private Bitmap getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        Bitmap bmp = null;
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
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
