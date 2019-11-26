package com.transports.utils;

/**
 * Class used only for URLs
 */
public class URLs {

    //schedules/routing service
    public static final String SCHEDULES = "http://192.168.85.208/transit/schedules";
    //get list of schedules
    public static final String GET_SCHEDULES = SCHEDULES+"/stop";
    public static final String GET_STOPS = SCHEDULES+"/stop";
    public static final String GET_STOP_INFO = SCHEDULES+"/stop/"; //+stopid
    public static final String GET_ROUTE = SCHEDULES+"/route/"; //+stopid / stopid

    //get list of agencies
    public static final String GET_AGENCIES = SCHEDULES+"/agency";

    //tickets service
    public static final String TICKETS = "http://192.168.85.208/ticket";
    public static final String CREATE_TICKET = TICKETS+"/create";
    public static final String GET_TICKET_STATUS = TICKETS+"/status";
    public static final String TICKET_PAYMENT1 = TICKETS+"/createAuth";
    public static final String TICKET_PAYMENT2 = TICKETS+"/purchase2";

    //Payment service
    public static final String PAYMENTS = "http://192.168.85.208/";
    public static final String PAYMENTS_LOGIN_ACCOUNT = PAYMENTS + "user/login";
    public static final String PAYMENTS_CREATE_ACCOUNT = PAYMENTS + "account/";


}
