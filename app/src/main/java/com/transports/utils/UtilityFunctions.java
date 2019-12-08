package com.transports.utils;

import android.util.Log;

import com.transports.R;
import com.transports.expandable_list.tickets.Ticket;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.transports.utils.Constants.COMPANY;
import static com.transports.utils.Constants.DATE_FIELD;
import static com.transports.utils.Constants.HASH_FIELD;
import static com.transports.utils.Constants.SCHEDULE;
import static com.transports.utils.Constants.TICKET_ID_FIELD;
import static com.transports.utils.Constants.TICKET_INFO_FIELD;
import static com.transports.utils.Constants.TICKET_STATUS_FIELD;
import static com.transports.utils.Constants.TRIP;

public class UtilityFunctions {

    public static String getDifferentBetweenHours(String h1, String h2) {
        String duration = "";

        try
        {
            SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");
            Date startTime = parseFormat.parse(h1);
            Date endTime = parseFormat.parse(h2);

            long mills = endTime.getTime() - startTime.getTime();
            long minutes = mills/(1000 * 60);

            if (minutes < 60)
                duration = minutes + " minutes";
            else{
                long h = minutes / 60;
                long m = minutes % 60;
                duration = h+"h"+m+"m";
            }
        }
        catch(ParseException ex)
        {
            // exception handling here
        }

        return duration;
    }

    public static Ticket parseJsonToTicket(String json) {
        Ticket ticket = null;
        try {
            JSONObject obj = new JSONObject(json).getJSONObject(Constants.TICKET_FIELD);
            String date = obj.getString(DATE_FIELD);
            String hash = obj.getString(HASH_FIELD);
            String status = obj.getString(TICKET_STATUS_FIELD);
            int id = obj.getInt(TICKET_ID_FIELD);
            obj = obj.getJSONObject(TICKET_INFO_FIELD);
            String trip = obj.getString(TRIP);
            String schedule = obj.getString(SCHEDULE);
            String transport = obj.getString(COMPANY);

            ticket = new Ticket(id, trip, transport, schedule, date, status, json, hash);

        } catch (Throwable t) {
            Log.e("JSONERROR", "Could not parse JSON (either is malformed or field does not exist): \"" + json + "\"" +t.getMessage());
        }
        return ticket;
    }

    public static String updateStatusJSON(Ticket t, String statusNew){
        String jsonUpdated = "";
        try {
            JSONObject obj = new JSONObject(t.getDetails()).getJSONObject(Constants.TICKET_FIELD);
            obj.put(TICKET_STATUS_FIELD, statusNew);
            JSONObject parentUpdate = new JSONObject(t.getDetails());
            parentUpdate.put(Constants.TICKET_FIELD, obj);
            jsonUpdated = parentUpdate.toString();

        } catch (Throwable thr) {
            Log.e("JSONERROR", "Could not parse malformed JSON: \"" + t.getDetails() + "\"");
        }
        return jsonUpdated;
    }

    public static int getIconOfTransport(String transport){
        if (transport.equalsIgnoreCase("cp")){
            return R.drawable.ic_cp;
        }
        if (transport.equalsIgnoreCase("metro")){
            return R.drawable.ic_metro;
        }
        if (transport.equalsIgnoreCase("transtejo")){
            return R.drawable.ic_transtejo;
        }
        if (transport.equalsIgnoreCase("walk")){
            return R.drawable.ic_walk_icon;
        }
        return R.drawable.ic_trip;
    }


    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
