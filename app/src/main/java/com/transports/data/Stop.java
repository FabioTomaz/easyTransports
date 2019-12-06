package com.transports.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A stop in which a transport (train, bus, metro) stops to pick up passengers.
 */
public class Stop implements Serializable, Parcelable {
    private String stop_id;
    private String stop_name;
    private String stop_transport;
    private String agency_key;
    private double stop_lat;
    private double stop_lon;

    public Stop(String stop_id, String stop_name) {
        this.stop_id = stop_id;
        this.stop_name = stop_name;
    }

    public Stop(String stop_id, String stop_name, String stop_transport, double stop_lat, double stop_long, String agency_key) {
        this.stop_id = stop_id;
        this.stop_name = stop_name;
        this.stop_transport = stop_transport;
        this.stop_lat = stop_lat;
        this.stop_lon = stop_long;
        this.agency_key = agency_key;
    }

    protected Stop(Parcel in) {
        stop_id = in.readString();
        stop_name = in.readString();
        stop_transport = in.readString();
        stop_lat = in.readDouble();
        stop_lon = in.readDouble();
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

    public double getStop_lon() {
        return stop_lon;
    }

    public void setStop_lon(double stop_lon) {
        this.stop_lon = stop_lon;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "stop_id='" + stop_id + '\'' +
                ", stop_name='" + stop_name + '\'' +
                ", stop_lat=" + stop_lat +
                ", stop_lon=" + stop_lon +
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
        dest.writeDouble(this.stop_lon);
    }

    public String getAgency_key() {
        return agency_key;
    }

    public void setAgency_key(String agency_key) {
        this.agency_key = agency_key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop = (Stop) o;
        return Double.compare(stop.stop_lat, stop_lat) == 0 &&
                Double.compare(stop.stop_lon, stop_lon) == 0 &&
                Objects.equals(stop_id, stop.stop_id) &&
                Objects.equals(stop_name, stop.stop_name) &&
                Objects.equals(stop_transport, stop.stop_transport) &&
                Objects.equals(agency_key, stop.agency_key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stop_id, stop_name, stop_transport, agency_key, stop_lat, stop_lon);
    }
}
