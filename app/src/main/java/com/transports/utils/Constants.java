package com.transports.utils;

public class Constants {

    // ------------ intents attributes -------------
    public static final String MENU_INTENT = "menu_id";
    public static final String TICKET_EXTRA_INTENT = "ticket";
    //pass data between schedules search and list
    public static final String TRANSPORT_COMPANY = "transport";
    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final String IS_DEPARTURE_DATE = "is_departure_date";
    public static final String TIME= "time";
    public static final String VARIANCE= "variance";
    public static final String DEPARTURE_DATE = "departure_date";
    //pass data between route and route map
    public static final String TRIPS_EXTRA = "tripChilds";

    // --------------------Json fields ------------------------

    //get stops, stop info and route
    public static final String STOP_INFO_TRANSPORT_FIELD = "agency_key";
    public static final String STOP_INFO_ID_FIELD = "stop_id";
    public static final String STOP_INFO_NAME_FIELD = "stop_name";
    public static final String STOP_INFO_COORDS_FIELD = "loc";
    public static final String STOP_INFO_AGENCY_KEY= "agency_key";

    //get rout fields
    public static final String ROUTE_LIST_FIELD = "routes";
    public static final String ROUTE_TRIP_CHILD_FIELD = "path";
    public static final String ROUTE_LINK_FIELD = "link";
    public static final String ROUTE_PATH_STOPFROM_FIELD = "fromId";
    public static final String ROUTE_PATH_STOPTO_FIELD = "toId";
    public static final String ROUTE_STOP_ID_FIELD = "id";
    public static final String ROUTE_TOTAL_PRICE_FIELD = "fareTotal";
    public static final String ROUTE_PRICE_FIELD = "fare";
    public static final String ROUTE_DEPARTURE_FIELD = "departureTime";
    public static final String ROUTE_ARRIVAL_FIELD = "arrivalTime";
    public static final String ROUTE_CHILD_DEPARTURE_FIELD = "departure_time";
    public static final String ROUTE_CHILD_ARRIVAL_FIELD = "arrival_time";
    public static final String ROUTE_TRIP_ID_FIELD = "trip_id";

    // create ticket / receive tickets
    public static final String TRIP = "trip";
    public static final String COMPANY = "transport";
    public static final String PRICE = "price";
    public static final String DATE_FIELD = "timestamp";
    public static final String SCHEDULE = "schedule";
    public static final String SECRET_FIELD = "secret";
    public static final String TICKET_STATUS_FIELD = "status";
    public static final String TICKETS_FIELD = "tickets";
    public static final String TICKET_FIELD = "ticket";
    public static final String TICKET_ID_FIELD = "ticket_id";
    public static final String ID_FIELD = "id";
    public static final String HASH_FIELD = "hash";
    public static final String TICKET_INFO_FIELD = "details";
    public static final String TICKET_AUTH_TOKEN_HEADER_FIELD = "AuthToken";

    //payment confirmation link field
    public static final String PAYMENT_CONFIRM_LINK_FIELD = "url";

    //get ticket status
    public static final String TICKET_STATE_FIELD = "status";

    //shared prefs name
    public static final String SHARED_PREFS_NAME = "transport";

    //temp
    public static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NzM5NzMzOTgsImlhdCI6MTU3Mzk2OTc5OCwic3ViIjoiM2U5ZjcxNGYtZjhjYi00NzZjLThmMWQtNTM0MjJjZDI1MmY5In0.G45ROa52iytFvpWEW9ayoO3USsMEiaVdQJ15QoER10A";
    //register user in payments
    public static final String PAYMENT_AUTH_TOKEN = "auth_token";
    public static final String PAYMENT_USER_ID = "user_id";
    public static final String PAYMENT_PASSWORD = "password";
    public static final String PAYMENT_CURRENCY_EUR = "EUR";
    public static final String PAYMENT_CURRENCY = "currency";
    public static final String PAYMENT_UUID = "uuid";
    public static final String PAYMENT_SHAREDPREFERENCES_PASS = "_pass";//shared preferences pass field identifier (<userID>_pass
    public static final String PAYMENT_STATUS = "status";
    public static final String PAYMENT_MESSAGE = "message";
    public static final String PAYMENT_STATUS_SUCCESSFULL = "success";
    public static final String PAYMENT_MESSAGE_FIELD = "message";
    public static final String PAYMENT_DEFAULT_PASS = "pass1234";


    //ticket states strings
    public static final String TICKET_EXPIRED = "expired";
    public static final String TICKET_INACTIVE = "inactive";
    public static final String TICKET_VALID = "valid";
}
