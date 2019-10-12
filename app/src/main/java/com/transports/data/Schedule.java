package com.transports.data;

public class Schedule {

    private String companyName;
    private String departureDate;
    private String departureHour;
    private String departureLocation;
    private String destination;
    private String arrivingHour;

    public Schedule(String companyName, String departureDate, String departureHour, String departureLocation,
                    String arrivingLocation, String arrivingHour) {
        this.companyName = companyName;
        this.departureDate = departureDate;
        this.departureHour = departureHour;
        this.departureLocation = departureLocation;
        this.destination = arrivingLocation;
        this.arrivingHour = arrivingHour;
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

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
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
}
