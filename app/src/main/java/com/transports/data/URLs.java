package com.transports.data;

/**
 * Class used only for URLs
 */
public class URLs {

    //schedules/routing service
    public static final String SCHEDULES = "http://192.168.85.208/transit/schedules";
    //get list of schedules
    public static final String GET_SCHEDULES = SCHEDULES+"/stop";
    public static final String GET_STOPS = SCHEDULES+"/stop";
    public static final String GET_ROUTE = SCHEDULES+"/route/";


    //tickets service
    public static final String TICKETS = "http://192.168.85.208/ticket";
    public static final String CREATE_TICKET = TICKETS+"/createTicket";
    public static final String GET_TICKET_STATUS = TICKETS+"/status";
    public static final String TICKET_PAYMENT1 = TICKETS+"/purchase";
    public static final String TICKET_PAYMENT2 = TICKETS+"/purchase2";

    //Payment service
    public static final String PAYMENTS = "http://192.168.85.208/Payment";
    public static final String PAYMENTS_CREATE_ACCOUNT = PAYMENTS + "account";


}
