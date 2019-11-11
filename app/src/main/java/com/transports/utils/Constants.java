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
    public static final String TICKETS_FIELD = "tickets";
    public static final String TICKET_ID_FIELD = "ticket_id";
    public static final String ID_FIELD = "ticket_id";
    public static final String HASH_FIELD = "hash";
    public static final String TICKET_INFO_FIELD = "details";

    //payment confirmation link field
    public static final String PAYMENT_CONFIRM_LINK_FIELD = "link";

    //get ticket status
    public static final String TICKET_STATE_FIELD = "ticketState";

    //register user in payments
    public static final String PAYMENT_USER_ID = "auth_token";


    //for testing
    public static final String TICKET_JSON_EXAMPLE = "{\n" +
            "    \"ticket_id\": 1,\n" +
            "    \"timestamp\": \"2019-11-11T15:11:47.782Z\",\n" +
            "    \"details\": {\n" +
            "        \"company\": \"CP\",\n" +
            "        \"schedule\": \"9:30 - 10:30\",\n" +
            "        \"price\": \"1.20€\",\n" +
            "        \"trip\": \"Aveiro - Porto\",\n" +

            "    },\n" +
            "    \"hash\": \"9c20c58501c881e8ae3f87ab0afcaadf828add2ca45079feeabf84a2e6dedb2a\",\n" +
            "    \"previousHash\": \"9d9054f50ce492d04609e5e19155f0556529a4c1ec721c26fc2becb6b860d094\",\n" +
            "    \"status\": \"valid\"\n" +
            "}";
}
