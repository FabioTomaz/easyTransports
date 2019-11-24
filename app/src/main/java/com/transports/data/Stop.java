package com.transports.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * A stop in which a transport (train, bus, metro) stops to pick up passengers.
 */
public class Stop implements Serializable, Parcelable {
    private String stop_id;
    private String stop_name;
    private String stop_transport;
    private String agency_key;
    private double stop_lat;
    private double stop_long;

    public Stop(String stop_id, String stop_name) {
        this.stop_id = stop_id;
        this.stop_name = stop_name;
    }

    public Stop(String stop_id, String stop_name, String stop_transport, double stop_lat, double stop_long, String agency_key) {
        this.stop_id = stop_id;
        this.stop_name = stop_name;
        this.stop_transport = stop_transport;
        this.stop_lat = stop_lat;
        this.stop_long = stop_long;
        this.agency_key = agency_key;
    }

    protected Stop(Parcel in) {
        stop_id = in.readString();
        stop_name = in.readString();
        stop_transport = in.readString();
        stop_lat = in.readDouble();
        stop_long = in.readDouble();
    }

    public static final Creator<Stop> CREATOR = new Creator<Stop>() {
        @Override
        public Stop createFromParcel(Parcel in) {
            return new Stop(in);
        }

        @Override
        public Stop[] newArray(int size) {
            return new Stop[size];
        }
    };

    public String getStopId() {
        return stop_id;
    }

    public void setStopId(String stopId) {
        this.stop_id = stopId;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }

    public String getStop_transport() {
        return stop_transport;
    }

    public void setStop_transport(String stop_transport) {
        this.stop_transport = stop_transport;
    }

    public double getStop_lat() {
        return stop_lat;
    }

    public void setStop_lat(double stop_lat) {
        this.stop_lat = stop_lat;
    }

    public double getStop_long() {
        return stop_long;
    }

    public void setStop_long(double stop_long) {
        this.stop_long = stop_long;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "stop_id='" + stop_id + '\'' +
                ", stop_name='" + stop_name + '\'' +
                ", stop_lat=" + stop_lat +
                ", stop_long=" + stop_long +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //The parcelable object has to be the first one
        dest.writeString(this.stop_id);
        dest.writeString(this.stop_name);
        dest.writeString(this.stop_transport);
        dest.writeDouble(this.stop_lat);
        dest.writeDouble(this.stop_long);
    }

    public String getAgency_key() {
        return agency_key;
    }

    public void setAgency_key(String agency_key) {
        this.agency_key = agency_key;
    }
}
