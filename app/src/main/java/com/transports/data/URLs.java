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

}
