package com.transports.expandable_list.tickets_list;

import java.io.Serializable;

public class Ticket implements Serializable {

    private String ticketHash;
    private String originDestination;
    private String transports;
    private String schedule;
    private String state;

    public Ticket(String transports, String originDestination, String schedule) {
        this.originDestination = originDestination;
        this.transports = transports;
        this.schedule = schedule;
    }

    public String getOriginDestination() {
        return originDestination;
    }

    public void setOriginDestination(String originDestination) {
        this.originDestination = originDestination;
    }

    public String getTicketHash() {
        return ticketHash;
    }

    public void setTicketHash(String ticketHash) {
        this.ticketHash = ticketHash;
    }

    public String getTransports() {
        return transports;
    }

    public void setTransports(String transports) {
        this.transports = transports;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "originDestination='" + originDestination + '\'' +
                ", transports='" + transports + '\'' +
                ", schedule='" + schedule + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
