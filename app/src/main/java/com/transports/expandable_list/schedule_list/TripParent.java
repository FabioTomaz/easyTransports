package com.transports.expandable_list.schedule_list;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.transports.expandable_list.tickets_list.TicketGlobal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A trip chas at least 1 transportation. Departure hour is same as first trip. and arriving hours are same as last trip's arriving hours.
 * Same logic for origin and destination. Price is the sum of all individual trips
 */
public class TripParent extends ExpandableGroup<TripChild> implements Serializable {
    private String transports;
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
        totalPrice = (double) Math.round(totalPrice * 100) / 100;
        return totalPrice;
    }

    //get all diferent transports involved in the trip (in the format transport + transport...)
    public String getTransports(){
        if (trips.isEmpty())
            return "";
        String t = "";
        Set<TripChild> uniqueTransports = new HashSet<>(trips);
        Iterator<TripChild> iterator = uniqueTransports.iterator();
        int i = 0;
        while(iterator.hasNext()) {
            TripChild trip = iterator.next();
            if (i == uniqueTransports.size()-1)
                t += trip.getCompanyName();
            else
                t += trip.getCompanyName() + " + ";
            i++;
        }
        return t;
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

    public TicketGlobal convertToGlobalTicket(){
        return new TicketGlobal(this.getOrigin()+"-"+this.getDestination(), this.transports, this.departureHour +"-"+this.arrivalHour);
    }

}