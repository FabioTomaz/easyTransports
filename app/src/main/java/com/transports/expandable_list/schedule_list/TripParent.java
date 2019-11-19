package com.transports.expandable_list.schedule_list;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.transports.data.Stop;
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
    private Stop origin;
    private Stop destination;
    private double totalPrice;
    private List<TripChild> trips;

    public TripParent(String departureHour, String arrivalHour, String date, Stop origin, Stop destination, List<TripChild> trips) {
        super(departureHour +" - "+arrivalHour, trips);
        this.trips = trips;
        this.departureHour = departureHour;
        this.arrivalHour = arrivalHour;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.totalPrice = calculateToTalPrice();
    }

    public TripParent(String departureHour, String arrivalHour, String date, Stop origin, Stop destination, List<TripChild> trips, double totalPrice) {
        super(departureHour +" -> "+arrivalHour, trips);
        this.trips = trips;
        this.departureHour = departureHour;
        this.arrivalHour = arrivalHour;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.totalPrice = totalPrice;
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

    public Stop getOrigin() {
        return origin;
    }

    public void setOrigin(Stop origin) {
        this.origin = origin;
    }

    public Stop getDestination() {
        return destination;
    }

    public void setDestination(Stop destination) {
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
        return new TicketGlobal(this.getOrigin().getStopName()+"->"+this.getDestination().getStopName(), this.transports, this.departureHour +"-"+this.arrivalHour);
    }

    @Override
    public String toString() {
        return "TripParent{" +
                "transports='" + transports + '\'' +
                ", departureHour='" + departureHour + '\'' +
                ", arrivalHour='" + arrivalHour + '\'' +
                ", date='" + date + '\'' +
                ", origin=" + origin +
                ", destination=" + destination +
                ", totalPrice=" + totalPrice +
                ", trips=" + trips +
                '}';
    }
}