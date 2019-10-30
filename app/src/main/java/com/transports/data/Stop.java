package com.transports.data;

import java.io.Serializable;

/**
 * A stop in which a transport (train, bus, metro) stops to pick up passengers.
 */
public class Stop implements Serializable {
    private String stopId;
    private String stopName;
    private double stopLat;
    private double stopLong;

    public Stop(String stopId, String stopName) {
        this.stopId = stopId;
        this.stopName = stopName;
    }

    public Stop(String stopId, String stopName, double stopLat, double stopLong) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopLat = stopLat;
        this.stopLong = stopLong;
    }

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
}
