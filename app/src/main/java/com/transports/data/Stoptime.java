package com.transports.data;

public class Stoptime {
    private String arrival_time;
    private int arrival_timestamp;

    private String departure_time;
    private int departure_timestamp;

    private String stop_id;
    private int stop_sequence;

    private String agency_key;

    private String trip_id;

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public int getArrival_timestamp() {
        return arrival_timestamp;
    }

    public void setArrival_timestamp(int arrival_timestamp) {
        this.arrival_timestamp = arrival_timestamp;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public int getDeparture_timestamp() {
        return departure_timestamp;
    }

    public void setDeparture_timestamp(int departure_timestamp) {
        this.departure_timestamp = departure_timestamp;
    }

    public String getStop_id() {
        return stop_id;
    }

    public void setStop_id(String stop_id) {
        this.stop_id = stop_id;
    }

    public int getStop_sequence() {
        return stop_sequence;
    }

    public void setStop_sequence(int stop_sequence) {
        this.stop_sequence = stop_sequence;
    }

    public String getAgency_key() {
        return agency_key;
    }

    public void setAgency_key(String agency_key) {
        this.agency_key = agency_key;
    }
}
