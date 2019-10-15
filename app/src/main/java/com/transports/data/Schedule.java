package com.transports.data;

public class Schedule {

    private String companyName;
    private String departureDate;
    private String departureHour;
    private String origin;
    private String destination;
    private String arrivingHour;
    private double price;

    public Schedule(String companyName, String departureDate, String departureHour, String arrivingHour,
                    String origin, String arrivingLocation, double price) {
        this.companyName = companyName;
        this.departureDate = departureDate;
        this.departureHour = departureHour;
        this.origin = origin;
        this.destination = arrivingLocation;
        this.arrivingHour = arrivingHour;
        this.price = price;
    }

    //temporary...
    public Schedule(String companyName, String destination) {
        this.companyName = companyName;
        this.destination = destination;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureHour() {
        return departureHour;
    }

    public void setDepartureHour(String departureHour) {
        this.departureHour = departureHour;
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

    public String getArrivingHour() {
        return arrivingHour;
    }

    public void setArrivingHour(String arrivingHour) {
        this.arrivingHour = arrivingHour;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
