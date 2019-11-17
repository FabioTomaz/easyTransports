package com.transports.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * A stop in which a transport (train, bus, metro) stops to pick up passengers.
 */
public class Stop implements Serializable, Parcelable {
    private String stopId;
    private String stopName;
    private String stopTransport;
    private double stopLat;
    private double stopLong;

    public Stop(String stopId, String stopName) {
        this.stopId = stopId;
        this.stopName = stopName;
    }

    public Stop(String stopId, String stopName, String stopTransport, double stopLat, double stopLong) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopTransport = stopTransport;
        this.stopLat = stopLat;
        this.stopLong = stopLong;
    }

    protected Stop(Parcel in) {
        stopId = in.readString();
        stopName = in.readString();
        stopTransport = in.readString();
        stopLat = in.readDouble();
        stopLong = in.readDouble();
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
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStopTransport() {
        return stopTransport;
    }

    public void setStopTransport(String stopTransport) {
        this.stopTransport = stopTransport;
    }

    public double getStopLat() {
        return stopLat;
    }

    public void setStopLat(double stopLat) {
        this.stopLat = stopLat;
    }

    public double getStopLong() {
        return stopLong;
    }

    public void setStopLong(double stopLong) {
        this.stopLong = stopLong;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "stopId='" + stopId + '\'' +
                ", stopName='" + stopName + '\'' +
                ", stopLat=" + stopLat +
                ", stopLong=" + stopLong +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //The parcelable object has to be the first one
        dest.writeString(this.stopId);
        dest.writeString(this.stopName);
        dest.writeString(this.stopTransport);
        dest.writeDouble(this.stopLat);
        dest.writeDouble(this.stopLong);
    }
}
