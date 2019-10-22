package com.transports.expandable_list.tickets_list;

public class Ticket {

    private String ticketHash;
    private String originDestination;
    private String transports;
    private String date;
    private String schedule;

    public Ticket(String originDestination, String transports, String date, String schedule) {
        this.originDestination = originDestination;
        this.transports = transports;
        this.date = date;
        this.schedule = schedule;
    }

    public String getOriginDestination() {
        return originDestination;
    }

    public void setOriginDestination(String originDestination) {
        this.originDestination = originDestination;
    }

    public String getTransports() {
        return transports;
    }

    public void setTransports(String transports) {
        this.transports = transports;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
