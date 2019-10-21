package com.transports.expandable_list.schedule_list;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A trip chas at least 1 transportation. Departure hour is same as first trip. and arriving hours are same as last trip's arriving hours.
 * Same logic for origin and destination. Price is the sum of all individual trips
 */
public class TripParent extends ExpandableGroup<TripChild> implements Serializable {
    private String departureHour;
    private String arrivalHour;
    private String date;
    private String origin;
    private String destination;
    private double totalPrice;
    private List<TripChild> trips;

    public TripParent(String departureHour, String arrivalHour, String date, String origin, String destination, List<TripChild> trips) {
        super(departureHour +" - "+arrivalHour, trips);
        this.trips = trips;
        this.departureHour = departureHour;
        this.arrivalHour = arrivalHour;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.totalPrice = calculateToTalPrice();
    }

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

    public double calculateToTalPrice() {
        double totalPrice = 0.0;
        for (TripChild t : trips){
            totalPrice+=t.getPrice();
        }
        return totalPrice;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public List<TripChild> getTripsChilds() {
        return trips;
    }

    public void setTrips(ArrayList<TripChild> trips) {
        this.trips = trips;
    }

    public int getTotalTransports(){
        return trips.size();
    }

}