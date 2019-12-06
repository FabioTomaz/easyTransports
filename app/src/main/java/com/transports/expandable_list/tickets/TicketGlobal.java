package com.transports.expandable_list.tickets;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a set of tickets making a trip.
 */
public class TicketGlobal {

    private int id;
    private String originDestination;
    private String transports;
    private String schedule;
    private List<Ticket> tickets;

    public TicketGlobal(){}

    public TicketGlobal(String originDestination, String transports, String schedule, List<Ticket> tickets) {
        this.originDestination = originDestination;
        this.transports = transports;
        this.schedule = schedule;
        this.tickets = tickets;
    }

    public TicketGlobal(String originDestination, String transports, String schedule) {
        this.originDestination = originDestination;
        this.transports = transports;
        this.schedule = schedule;
        this.tickets = new ArrayList<>();
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TicketGlobal{" +
                "originDestination='" + originDestination + '\'' +
                ", transports='" + transports + '\'' +
                ", schedule='" + schedule + '\'' +
                ", tickets=" + tickets +
                '}';
    }
}
