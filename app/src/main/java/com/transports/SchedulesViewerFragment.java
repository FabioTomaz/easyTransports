package com.transports;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.transports.data.SQLiteDatabaseHandler;
import com.transports.data.Stop;
import com.transports.expandable_list.schedule_list.TripAdapter;
import com.transports.expandable_list.schedule_list.TripChild;
import com.transports.expandable_list.schedule_list.TripParent;
import com.transports.expandable_list.schedule_list.TripParentViewHolder;
import com.transports.expandable_list.tickets_list.Ticket;
import com.transports.expandable_list.tickets_list.TicketGlobal;
import com.transports.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.LinearLayout.VERTICAL;
import static com.transports.data.AppDataInfo.stops;
import static com.transports.utils.Constants.DATE_FIELD;
import static com.transports.utils.Constants.PAYMENT_AUTH_TOKEN;
import static com.transports.utils.Constants.PAYMENT_MESSAGE_FIELD;
import static com.transports.utils.Constants.PAYMENT_PASSWORD;
import static com.transports.utils.Constants.PAYMENT_STATUS;
import static com.transports.utils.Constants.PAYMENT_STATUS_SUCCESSFULL;
import static com.transports.utils.Constants.PAYMENT_USER_ID;
import static com.transports.utils.Constants.PRICE;
import static com.transports.utils.Constants.ROUTE_ARRIVAL_FIELD;
import static com.transports.utils.Constants.ROUTE_CHILD_ARRIVAL_FIELD;
import static com.transports.utils.Constants.ROUTE_CHILD_DEPARTURE_FIELD;
import static com.transports.utils.Constants.ROUTE_DEPARTURE_FIELD;
import static com.transports.utils.Constants.ROUTE_LIST_FIELD;
import static com.transports.utils.Constants.ROUTE_PRICE_FIELD;
import static com.transports.utils.Constants.ROUTE_STOP_ID_FIELD;
import static com.transports.utils.Constants.ROUTE_TOTAL_PRICE_FIELD;
import static com.transports.utils.Constants.ROUTE_TRIP_CHILD_FIELD;
import static com.transports.utils.Constants.SCHEDULE;
import static com.transports.utils.Constants.SECRET_FIELD;
import static com.transports.utils.Constants.TICKET_AUTH_TOKEN_HEADER_FIELD;
import static com.transports.utils.Constants.TRANSPORT_COMPANY;
import static com.transports.utils.Constants.TRIP;
import static com.transports.utils.URLs.CREATE_TICKET;
import static com.transports.utils.URLs.GET_ROUTE;
import static com.transports.utils.URLs.PAYMENTS_LOGIN_ACCOUNT;
import static com.transports.utils.URLs.TICKET_PAYMENT1;
import static com.transports.utils.UtilityFunctions.generateString;


/**
 * Fragment that shows list of schedules.
 * Receives the origin and destination stop from SchedulesFragment, calls schedule service and parses response and shows an expandable
 * list with a trip (TripParent) that may contain sub trips (TripChild).
 * Calls payment service and handles the payment logic communication when user presses 'buy' button
 */
public class SchedulesViewerFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String date;
    private Stop origin;
    private Stop destination;
    private List<TripChild> tripChildren = new ArrayList<>();
    private List<TripParent> tripParentList = new ArrayList<>();
    private RecyclerView recycler;
    private TripParentViewHolder tripParentViewHolder;
    private boolean browserOpened = false;
    private TripParent selectedTripParent;
    private String paymentsResponse;
    private String ticketsToken;
    private String authToken;
    private String paymentID;

    public SchedulesViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedules_viewer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();

        if(bundle != null){
            date = bundle.getString(Constants.DEPARTURE_DATE);
            origin = (Stop) bundle.getSerializable(Constants.ORIGIN);
            destination = (Stop) bundle.getSerializable(Constants.DESTINATION);
            tripChildren = new ArrayList<>();
            tripParentList = new ArrayList<>();
            getActivity().setTitle(origin.getStop_name()+" - "+destination.getStop_name());

            //call service give info and receive
            getRoute(origin.getStopId(), destination.getStopId());
/*
            //place all transports in the adapter
            tripChildren.add(new TripChild("CP ", "12/12/2019", "12:50", "13:25", "Aveiro", "Porto", 1.45));
            tripChildren.add(new TripChild("Carris ", "12/12/2019", "12:59", "13:32", "Aveiro", "Porto", 2.45));
            tripChildren.add(new TripChild("Move Aveiro ", "12/12/2019", "15:50", "16:15", "Aveiro", "Porto", 1.45));


            TripParent t1 = new TripParent("13:25", "14:20", "12/12/2019", "Aveiro", "Porto", tripChildren);
            TripParent t2 = new TripParent("14:25", "15:30", "12/12/2019", "Aveiro", "Porto", tripChildren);
            TripParent t3 = new TripParent("14:30", "15:50", "12/12/2019", "Aveiro", "Porto", tripChildren);*/

        }
    }

    private void setSchedulesOnList(final List<TripParent> trips){
        this.tripParentList = trips;
        recycler = (RecyclerView) getView().findViewById(R.id.schedules_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        //instantiate your adapter with the list of genres
        TripAdapter adapter = new TripAdapter(trips, this);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        recycler.addItemDecoration(decoration);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                goBack();
                return true;
            }
            return false;
        });
        if (browserOpened && selectedTripParent != null && ticketsToken!= null)
            this.purchaseTicket2(selectedTripParent, paymentsResponse);
    }

    private void goBack(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Get fragment one if exist.
        Fragment schedulesFragment = new SchedulesFragment();

        fragmentTransaction.replace(R.id.container, schedulesFragment);

        // Do not add fragment three in back stack.
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().setTitle(getString(R.string.app_name_full));
    }

    public void getRoute(final String originStopID, String destinationStopID) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(
                Request.Method.GET,
                GET_ROUTE+originStopID+"/"+destinationStopID,
                null,
                response -> {
                    Log.d("schedulesRes", response+"");

                    // Process the JSON
                    try {
                        JSONArray routeAlternativesList = response.getJSONArray(ROUTE_LIST_FIELD);
                        double totalPrice = response.getDouble(ROUTE_TOTAL_PRICE_FIELD);
                        String departureTimeTotal = response.getString(ROUTE_DEPARTURE_FIELD);
                        String arrivalTimeTotal = response.getString(ROUTE_ARRIVAL_FIELD);
                        List<String> stopIds = new ArrayList<>();
                        if (routeAlternativesList.length() == 0){
                            showErrorAndGoBack("No schedules found", "Transportation for the specified schedule could not be found. Please try another stop or another schedule",
                                    KAlertDialog.WARNING_TYPE, true);
                            return;
                        }
                        for (int i = 0; i < routeAlternativesList.length(); i++){
                            JSONObject routeAlternative = routeAlternativesList.getJSONObject(i);//element inside "routes"
                            JSONArray tripChildList = routeAlternative.getJSONArray(ROUTE_TRIP_CHILD_FIELD);//"path" field of "routes"
                            //origin and destination stop of this child trip can be obtained by checking the first (origin) and last (destination) stop
                            String originStopChildID = tripChildList.getJSONObject(0).getString(ROUTE_STOP_ID_FIELD);
                            String destinationStopChildID = tripChildList.getJSONObject(tripChildList.length()-1).getString(ROUTE_STOP_ID_FIELD);

                            Stop originStopChild = getStopFromID(originStopChildID);
                            Stop destinationStopChild = getStopFromID(destinationStopChildID);
                            String companyName = originStopChild.getStop_transport();
                            double price = routeAlternative.getDouble(ROUTE_PRICE_FIELD);
                            String departureTime = routeAlternative.getString(ROUTE_CHILD_DEPARTURE_FIELD);
                            String arrivalTime = routeAlternative.getString(ROUTE_CHILD_ARRIVAL_FIELD);
                            tripChildren.add(new TripChild(companyName, "", departureTime, arrivalTime,
                                    originStopChild, destinationStopChild, price));
                        }
                        TripParent tripParent = new TripParent(departureTimeTotal, arrivalTimeTotal, date, origin, destination, tripChildren);
                        tripParentList.add(tripParent);
                        setSchedulesOnList(tripParentList);
                    } catch (JSONException e) {
                        showErrorAndGoBack("Error getting schedules", "An error ocurred getting the shcedules",  KAlertDialog.ERROR_TYPE, true);
                    }

                },
                error -> {
                    // Do something when error occurred
                    Toast.makeText(getContext(), "An error occured", Toast.LENGTH_SHORT).show();
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    public Stop getStopFromID(String stopID){
        for (Stop stop : stops){
            if (stop.getStopId().equals(stopID)){
                return stop;
            }
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //called by user click on "buy" button of a list of tickets
    public void handlePurchase(int tripParentPosition){
        TripParent tripParent = tripParentList.get(tripParentPosition);
        Log.d("resTripParent", tripParent+"");
        showConfirmPurchaseDialog(tripParent);
    }

    public void showConfirmPurchaseDialog(final TripParent tripParent){
        new AlertDialog.Builder(getContext())
                .setTitle("Ticket purchase confirmation")
                .setMessage("Are you sure you want to purchase this(these) ticket(s)? \n All purchases are final.\n The total value is "+tripParent.getTotalPrice())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //user confirmed ticket purchase
                        loginUserInPayments(tripParent, FirebaseAuth.getInstance().getCurrentUser().getUid());

                        /*List<Ticket> tickets = new ArrayList<>();
                        TicketGlobal ticketGlobal = tripParent.convertToGlobalTicket();
                        //parse list of purchased tickets

                        List<Ticket> tickets1 = new ArrayList<>();
                        tickets1.add(new Ticket(1, Constants.TICKET_JSON_EXAMPLE, "active"));
                        tickets1.add(new Ticket(2, Constants.TICKET_JSON_EXAMPLE, "active"));

                        ticketGlobal = new TicketGlobal("Aveiro - Coimbra", "CP", "8:30-9:30", tickets1);
                        SQLiteDatabaseHandler bd = new SQLiteDatabaseHandler(getContext());
                        //Log.d("dbtickets", bd.getAllGlobalTickets()+"");
                        bd.addGlobalTicket(ticketGlobal);*/

                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void purchaseTicket(final TripParent tripParent){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //generate random token to use with ticket service
        ticketsToken = generateString();
        //create list of request ticket creation json objects
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put(SECRET_FIELD, ticketsToken);
            JSONArray jsonTicketsArray = new JSONArray();
            for (TripChild trip : tripParent.getTripsChilds()) {
                JSONObject jsonTicket = new JSONObject();
                jsonTicket.put(TRANSPORT_COMPANY, trip.getCompanyName());
                jsonTicket.put(SCHEDULE, trip.getSchedule());
                jsonTicket.put(TRIP, trip.getTrip());
                jsonTicket.put(PRICE, trip.getPrice());
                jsonTicket.put(DATE_FIELD, new Date()+"");
                jsonTicketsArray.put(jsonTicket);
            }
            jsonBody.put(Constants.TICKET_INFO_FIELD, jsonTicketsArray);

            Log.d("responsePurchaseSent", jsonBody+", authToken-> "+authToken);
        } catch (JSONException e){
            showErrorAndGoBack("Purchase failed", "Could not get tickets to buy.",  KAlertDialog.ERROR_TYPE, false);
        }


        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(
                Request.Method.POST,
                TICKET_PAYMENT1,
                jsonBody,
                new Response.Listener<JSONObject >() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("responsePurchase", response+"");
                        // get list of purchased tickets and add them to a global ticket. Store in DB
                        String paymentConfirmationLink = "";

                        try {
                            paymentConfirmationLink = response.getString(Constants.PAYMENT_CONFIRM_LINK_FIELD);
                            paymentID = response.getString("paymentId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!paymentConfirmationLink.isEmpty()){
                            selectedTripParent = tripParent;
                            paymentsResponse = jsonBody.toString();
                            openBrowser(paymentConfirmationLink);
                        }
                        else{
                            //error getting link
                            showErrorAndGoBack(getString(R.string.ticket_purchase_error_title), getString(R.string.ticket_purchase_error_message2),
                                    KAlertDialog.ERROR_TYPE, false);
                        }

                    }
                },
                error -> {
                    Log.d("responsePurchaseErr", error+"");
                    showErrorAndGoBack(getString(R.string.ticket_purchase_error_title), getString(R.string.ticket_purchase_error_message),
                            KAlertDialog.ERROR_TYPE, false);
                    ticketsToken = null;
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put(TICKET_AUTH_TOKEN_HEADER_FIELD, authToken);
                return params;
            }
        };

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private void purchaseTicket2(final TripParent tripParent, String request){
        this.browserOpened = false;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //create list of request ticket creation json objects
        JSONObject jsonBody = null;
        try{
            jsonBody = new JSONObject(request);
        } catch (JSONException e){ }


        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(
                Request.Method.POST,
                CREATE_TICKET,
                jsonBody,
                response -> {
                    Log.d("responsePurchase2", response+"");
                    // first open browser for payment
                    List<Ticket> tickets = new ArrayList<>();
                    TicketGlobal ticketGlobal = tripParent.convertToGlobalTicket();
                    //parse list of purchased tickets and add to list
                    try {
                        JSONArray purchasedTickets = response.getJSONArray(Constants.TICKETS_FIELD);
                        for (int i = 0; i < purchasedTickets.length(); i++){
                            JSONObject j = purchasedTickets.getJSONObject(i);
                            tickets.add( new Ticket(j.getInt(Constants.TICKET_ID_FIELD), response.toString(), j.getString(Constants.TICKET_STATUS_FIELD)));
                        }
                    } catch (JSONException e) {
                        ticketsToken = null;
                        e.printStackTrace();
                    }

                    if(tickets.isEmpty()){
                        new AlertDialog.Builder(getContext())
                                .setTitle(getString(R.string.ticket_purchase_error_title))
                                .setMessage(getString(R.string.ticket_purchase_error_message3))
                                .setIcon(android.R.drawable.ic_dialog_alert).setNeutralButton("OK", null).show();
                        return;
                    }

                    //ticketGlobal = new TicketGlobal("Aveiro - Porto", "CP", "8:30-9:30", tickets);
                    ticketGlobal.setTickets(tickets);
                    SQLiteDatabaseHandler bd = new SQLiteDatabaseHandler(getContext());
                    bd.addGlobalTicket(ticketGlobal);
                    showSuccess("Ticket purchase successfull.", "You have succesffully purchased your tickets. Go to 'My Tickets' to see and use your tickets");
                },
                error -> {
                    Log.d("responsePurchase2ER", error+"");
                    showErrorAndGoBack(getString(R.string.ticket_purchase_error_title), getString(R.string.ticket_purchase_error_message), KAlertDialog.ERROR_TYPE, false);
                    ticketsToken = null;
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put(TICKET_AUTH_TOKEN_HEADER_FIELD, authToken);
                params.put("paymentId", paymentID);
                return params;
            }
        };
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    private void loginUserInPayments(final TripParent tripParent, String firebaseID){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //create list of request ticket creation json objects
        JSONObject jsonBody = new JSONObject();
        try{
            //jsonBody.put(PAYMENT_USER_ID, firebaseID);
            jsonBody.put(PAYMENT_USER_ID, "529116cc-33cc-4185-a915-77192a9658c2");
            jsonBody.put(PAYMENT_PASSWORD, "transdev");
        } catch (JSONException e){ }

        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(
                Request.Method.POST,
                PAYMENTS_LOGIN_ACCOUNT,
                jsonBody,
                response -> {
                    Log.d("responsPayment", response+"");

                    String authTokenField = null;
                    String status = null;
                    try {
                        authTokenField = response.getJSONObject(PAYMENT_MESSAGE_FIELD).getString(PAYMENT_AUTH_TOKEN);
                        status = response.getJSONObject(PAYMENT_MESSAGE_FIELD).getString(PAYMENT_STATUS);
                        if (!status.equalsIgnoreCase(PAYMENT_STATUS_SUCCESSFULL)){
                            showErrorAndGoBack("Payment Error", response.getJSONObject(PAYMENT_MESSAGE_FIELD).getString(PAYMENT_MESSAGE_FIELD), KAlertDialog.ERROR_TYPE, false);
                        }
                        authToken = authTokenField;
                        purchaseTicket( tripParent );
                    } catch (JSONException e) {
                        showErrorAndGoBack("Payment Error", "Error fetching payment account.", KAlertDialog.ERROR_TYPE, false);
                    }

                },
                error -> {
                    Log.d("errorPayment", error+"");
                    Toast.makeText(getContext(), "Could not login user in payment service", Toast.LENGTH_SHORT);
                    /*new AlertDialog.Builder(getApplication())
                            .setTitle(getString(R.string.ticket_purchase_error_title))
                            .setMessage(getString(R.string.ticket_purchase_error_message))
                            .setIcon(android.R.drawable.ic_dialog_alert).setNeutralButton("OK", null).show();*/
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    public void openBrowser(String link){
        this.browserOpened = true;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    private void showErrorAndGoBack(String title, String descr, int type, boolean goBack){
        new KAlertDialog(getContext(), type)
                .setTitleText(title)
                .setContentText(descr)
                .setConfirmText("OK")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    if (goBack)
                        goBack();
                })
                .show();
    }

    private void showSuccess(String title, String descr){
        new KAlertDialog(getContext(), KAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(descr)
                .setConfirmText("OK")
                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}
