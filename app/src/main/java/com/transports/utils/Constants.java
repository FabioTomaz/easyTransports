package com.transports.utils;

public class Constants {

    // ------------ intents attributes -------------
    public static final String MENU_INTENT = "menu_id";
    public static final String TICKET_EXTRA_INTENT = "ticket";
    //pass data between schedules search and list
    public static final String TRANSPORT_COMPANY = "transport";
    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final String DEPARTURE_DATE = "departure_date";

    // --------------------Json fields ------------------------

    // create ticket / receive tickets
    public static final String TRIP = "trip";
    public static final String COMPANY = "company";
    public static final String PRICE = "price";
    public static final String DATE_FIELD = "timestamp";
    public static final String SCHEDULE = "schedule";
    public static final String SECRET_FIELD = "secret";
    public static final String TICKET_STATUS_FIELD = "status";
    public static final String TICKET_ID_FIELD = "ticket_id";
    public static final String TICKET_INFO_FIELD = "details";

    //get ticket status
    public static final String TICKET_STATE_FIELD = "ticketState";


    //for testing
    public static final String TICKET_JSON_EXAMPLE = "{\n" +
            "    \"ticket_id\": 0,\n" +
            "    \"timestamp\": \"2019-10-14T10-04-30.651Z\",\n" +
            "    \"details\": {\n" +
            "        \"trip\": \"Aveiro - Porto\",\n" +
            "        \"company\": \"Transdev\",\n" +
            "        \"schedule\": \"9:30-10:30\",\n" +
            "        \"destinations\": \"Aveiro\",\n" +
            "        \"price\": 0\n" +
            "    },\n" +
            "    \"hash\": \"afd49088c68c2c17c329aedd6fbbe53a4d16b0ee8f187\",\n" +
            "    \"status\": \"inactive\"\n" +
            "}";
}
