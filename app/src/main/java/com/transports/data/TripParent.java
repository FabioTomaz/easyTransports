package com.transports.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A trip chas at least 1 transportation. Departure hour is same as first trip. and arriving hours are same as last trip's arriving hours.
 * Same logic for origin and destination. Price is the sum of all individual trips
 */
public class TripParent implements Serializable {
    private String departureHour;
    private String arrivalHour;
    private String date;
    private String origin;
    private String destination;
    private double totalPrice;
    private ArrayList<TripChild> trips;

    /*Getters and setters*/

    public String getDepartureHour() {
        return departureHour;
    }

    public void setDepartureHour(String departureHour) {
        this.departureHour = departureHour;
    }

    public String getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(String arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<TripChild> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<TripChild> trips) {
        this.trips = trips;
    }
}