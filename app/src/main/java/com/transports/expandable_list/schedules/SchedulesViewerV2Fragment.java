package com.transports.expandable_list.schedules;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.transports.R;
import com.transports.data.SQLiteDatabaseHandler;
import com.transports.data.Stop;
import com.transports.expandable_list.schedules.SchedulesFragment;
import com.transports.expandable_list.schedules.TripAdapter;
import com.transports.expandable_list.schedules.TripChild;
import com.transports.expandable_list.schedules.TripParent;
import com.transports.expandable_list.tickets.Ticket;
import com.transports.expandable_list.tickets.TicketGlobal;
import com.transports.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.widget.LinearLayout.VERTICAL;
import static com.transports.data.AppDataInfo.stops;
import static com.transports.utils.Constants.DATE_FIELD;
import static com.transports.utils.Constants.PAYMENT_AUTH_TOKEN;
import static com.transports.utils.Constants.PAYMENT_MESSAGE_FIELD;
import static com.transports.utils.Constants.PAYMENT_PASSWORD;
import static com.transports.utils.Constants.PAYMENT_SHAREDPREFERENCES_PASS;
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
import static com.transports.utils.Constants.SHARED_PREFS_NAME;
import static com.transports.utils.Constants.TICKET_AUTH_TOKEN_HEADER_FIELD;
import static com.transports.utils.Constants.TRANSPORT_COMPANY;
import static com.transports.utils.Constants.TRIP;
import static com.transports.utils.Constants.TRIPS_EXTRA;
import static com.transports.utils.URLs.CREATE_TICKET;
import static com.transports.utils.URLs.GET_ROUTE;
import static com.transports.utils.URLs.PAYMENTS_LOGIN_ACCOUNT;
import static com.transports.utils.URLs.TICKET_PAYMENT1;
import static com.transports.utils.UtilityFunctions.generateString;

// -----> https://www.awsrh.com/2017/10/modern-profile-ui-design-in-android.html
public class SchedulesViewerV2Fragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private String date;
    private Stop origin;
    private Stop destination;
    private boolean isDepartureDate;
    private Date time;
    private int variance;
    private List<TripChild> tripChildren = new ArrayList<>();
    private TripParent tripParent;
    private RecyclerView recycler;
    private boolean browserOpened = false;
    private TripParent selectedTripParent;
    private String paymentsResponse;
    private String ticketsToken;
    private String authToken;
    private String paymentID;
    private TextView originLabel;
    private TextView destinationLabel;
    private TextView totalPriceLabel;
    private TextView transportsLabel;
    private TextView schedulesLabel;
    private Button buyTicketBtn;
    private Button openMapBtn;

    public SchedulesViewerV2Fragment() {
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
        return inflater.inflate(R.layout.fragment_schedules_viewer_v2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();

        originLabel = (TextView) getView().findViewById(R.id.origin_label);
        destinationLabel = (TextView) getView().findViewById(R.id.destination_label);
        totalPriceLabel = (TextView) getView().findViewById(R.id.transports_number_label);
        transportsLabel = (TextView) getView().findViewById(R.id.transports);
        schedulesLabel = (TextView) getView().findViewById(R.id.schedule_total_trip_label);
        buyTicketBtn = (Button) getView().findViewById(R.id.buy_ticket_btn);
        openMapBtn = (Button) getView().findViewById(R.id.open_map_route_btn);
        if (bundle != null) {
            date = bundle.getString(Constants.DEPARTURE_DATE);
            origin = (Stop) bundle.getSerializable(Constants.ORIGIN);
            destination = (Stop) bundle.getSerializable(Constants.DESTINATION);
            isDepartureDate = bundle.getBoolean(Constants.IS_DEPARTURE_DATE);
            time = (Date) bundle.getSerializable(Constants.TIME);
            variance = bundle.getInt(Constants.VARIANCE);

            originLabel.setText(origin.getStop_name());
            destinationLabel.setText(destination.getStop_name());

            tripChildren = new ArrayList<>();
            getActivity().setTitle(origin.getStop_name() + " - " + destination.getStop_name());

            buyTicketBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    handlePurchase();
                }
            });

            openMapBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (tripParent != null) {
                        Intent intent = new Intent(getContext(), RouteMap.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(TRIPS_EXTRA, (ArrayList<? extends Parcelable>) tripParent.getTripsChilds());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });

            //call service give info and receive
            getRoute(origin.getStopId(), destination.getStopId(), isDepartureDate, time, variance);
        }
        else {
            //TODO: finish activity
        }
    }

    private void setSchedulesOnList(final TripParent trip) {
        this.tripParent = trip;
        //add trip parent data to fields
        this.transportsLabel.setText(trip.getTransports());
        this.schedulesLabel.setText(trip.getschedules());
        this.totalPriceLabel.setText(trip.getTotalPrice()+"€");
        recycler = getView().findViewById(R.id.schedules_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //instantiate your adapter with the list of genres
        TripAdapter adapter = new TripAdapter(getContext(), trip.getTripsChilds());
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

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                goBack();
                return true;
            }
            return false;
        });
        //Check if we are resuming activity after closing a browser opened for payment service
        if (browserOpened && selectedTripParent != null && ticketsToken != null)
            this.purchaseTicket2(selectedTripParent, paymentsResponse);

        SharedPreferences sharedPref = getContext().getSharedPreferences(SHARED_PREFS_NAME, 0);
        String retrieved = sharedPref.getString(FirebaseAuth.getInstance().getUid(), "");
        Log.d("userSave", FirebaseAuth.getInstance().getUid() +", retr: "+retrieved);
    }

    private void goBack() {
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

    private void getRoute(
            final String originStopID,
            String destinationStopID,
            boolean isDepartureTime,
            Date time,
            int variance
    ) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        String url = GET_ROUTE + originStopID + "/" + destinationStopID;

        if (time != null || variance != -1) {
            url += "?";
            boolean previousQueryParam = false;
            if (time != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault());
                String timeQuery = isDepartureTime ? "departureTime=" : "arrivalTime=";
                timeQuery += df.format(time);
                url += timeQuery;
                previousQueryParam = true;
            }

            if (variance != -1) {
                if (previousQueryParam) {
                    url += "&";
                }
                url += "timeVariance=" + variance;
            }
        }

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d("schedulesRes", response + "");

                    // Process the JSON
                    try {
                        JSONArray routeAlternativesList = response.getJSONArray(ROUTE_LIST_FIELD);
                        double totalPrice = response.getDouble(ROUTE_TOTAL_PRICE_FIELD);
                        String departureTimeTotal = response.getString(ROUTE_DEPARTURE_FIELD);
                        String arrivalTimeTotal = response.getString(ROUTE_ARRIVAL_FIELD);
                        List<String> stopIds = new ArrayList<>();
                        if (routeAlternativesList.length() == 0) {
                            showErrorAndGoBack("No schedules found", "Transportation for the specified schedule could not be found. Please try another stop or another schedule",
                                    KAlertDialog.WARNING_TYPE, true);
                            return;
                        }
                        for (int i = 0; i < routeAlternativesList.length(); i++) {
                            JSONObject routeAlternative = routeAlternativesList.getJSONObject(i);//element inside "routes"
                            JSONArray tripChildList = routeAlternative.getJSONArray(ROUTE_TRIP_CHILD_FIELD);//"path" field of "routes"
                            //origin and destination stop of this child trip can be obtained by checking the first (origin) and last (destination) stop
                            String originStopChildID = tripChildList.getJSONObject(0).getString(ROUTE_STOP_ID_FIELD);
                            String destinationStopChildID = tripChildList.getJSONObject(tripChildList.length() - 1).getString(ROUTE_STOP_ID_FIELD);

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
                        this.tripParent = tripParent;
                        setSchedulesOnList(tripParent);
                    } catch (JSONException e) {
                        showErrorAndGoBack("Error getting schedules", "An error ocurred getting the shcedules", KAlertDialog.ERROR_TYPE, true);
                    }

                },
                error -> {
                    // Do something when error occurred
                    showErrorAndGoBack("Error getting schedules", "An error ocurred getting the shcedules. Please try again with a different schedule.", KAlertDialog.ERROR_TYPE, true);
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private Stop getStopFromID(String stopID) {
        for (Stop stop : stops) {
            if (stop.getStopId().equals(stopID)) {
                return stop;
            }
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    //called by user click on "buy" button
    public void handlePurchase() {
        Log.d("resTripParent", this.tripParent + "");
        showConfirmPurchaseDialog(this.tripParent);
    }

    private void showConfirmPurchaseDialog(final TripParent tripParent) {
        new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE)
                .setTitleText("Ticket purchase confirmation")
                .setContentText("Are you sure you want to purchase this(these) ticket(s)? \n All purchases are final.\n The total value is " + tripParent.getTotalPrice())
                .setConfirmText("Yes")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    loginUserInPayments(tripParent, FirebaseAuth.getInstance().getCurrentUser().getEmail());
                })
                .setCancelText("Cancel")
                .setCancelClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                })
                .show();
    }

    private void purchaseTicket(final TripParent tripParent) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //generate random token to use with ticket service
        ticketsToken = generateString();
        //create list of request ticket creation json objects
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SECRET_FIELD, ticketsToken);
            JSONArray jsonTicketsArray = new JSONArray();
            for (TripChild trip : tripParent.getTripsChilds()) {
                JSONObject jsonTicket = new JSONObject();
                jsonTicket.put(TRANSPORT_COMPANY, trip.getCompanyName());
                jsonTicket.put(SCHEDULE, trip.getSchedule());
                jsonTicket.put(TRIP, trip.getTrip());
                jsonTicket.put(PRICE, trip.getPrice());
                jsonTicket.put(DATE_FIELD, new Date() + "");
                jsonTicketsArray.put(jsonTicket);
            }
            jsonBody.put(Constants.TICKET_INFO_FIELD, jsonTicketsArray);

            Log.d("responsePurchaseSent", jsonBody + ", authToken-> " + authToken);
        } catch (JSONException e) {
            showErrorAndGoBack("Purchase failed", "Could not get tickets to buy.", KAlertDialog.ERROR_TYPE, false);
        }


        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                TICKET_PAYMENT1,
                jsonBody,
                response -> {
                    Log.d("responsePurchase", response + "");
                    // get list of purchased tickets and add them to a global ticket. Store in DB
                    String paymentConfirmationLink = "";

                    try {
                        paymentConfirmationLink = response.getString(Constants.PAYMENT_CONFIRM_LINK_FIELD);
                        paymentID = response.getString("paymentId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!paymentConfirmationLink.isEmpty()) {
                        selectedTripParent = tripParent;
                        paymentsResponse = jsonBody.toString();
                        openBrowser(paymentConfirmationLink);
                    } else {
                        //error getting link
                        showErrorAndGoBack(getString(R.string.ticket_purchase_error_title), getString(R.string.ticket_purchase_error_message2),
                                KAlertDialog.ERROR_TYPE, false);
                    }

                },
                error -> {
                    Log.d("responsePurchaseErr", error + "");
                    showErrorAndGoBack(getString(R.string.ticket_purchase_error_title), getString(R.string.ticket_purchase_error_message),
                            KAlertDialog.ERROR_TYPE, false);
                    ticketsToken = null;
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put(TICKET_AUTH_TOKEN_HEADER_FIELD, authToken);
                return params;
            }
        };

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private void purchaseTicket2(final TripParent tripParent, String request) {
        this.browserOpened = false;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //create list of request ticket creation json objects
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(request);
        } catch (JSONException e) {
        }


        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                CREATE_TICKET,
                jsonBody,
                response -> {
                    Log.d("responsePurchase2", response + "");
                    // first open browser for payment
                    List<Ticket> tickets = new ArrayList<>();
                    TicketGlobal ticketGlobal = tripParent.convertToGlobalTicket();
                    //parse list of purchased tickets and add to list
                    try {
                        JSONArray purchasedTickets = response.getJSONArray(Constants.TICKETS_FIELD);
                        for (int i = 0; i < purchasedTickets.length(); i++) {
                            JSONObject j = purchasedTickets.getJSONObject(i);
                            JSONObject details = new JSONObject();
                            details.put("secret", ticketsToken);
                            details.put("ticket", new JSONObject(j.toString()));
                            tickets.add( new Ticket(j.getInt(Constants.TICKET_ID_FIELD), details.toString(), j.getString(Constants.TICKET_STATUS_FIELD)));
                        }
                    } catch (JSONException e) {
                        ticketsToken = null;
                        e.printStackTrace();
                    }

                    if (tickets.isEmpty()) {
                        showErrorAndGoBack(getString(R.string.ticket_purchase_error_title), getString(R.string.ticket_purchase_error_message3), KAlertDialog.ERROR_TYPE, false);
                    }

                    //ticketGlobal = new TicketGlobal("Aveiro - Porto", "CP", "8:30-9:30", tickets);
                    ticketGlobal.setTickets(tickets);
                    SQLiteDatabaseHandler bd = new SQLiteDatabaseHandler(getContext());
                    bd.addGlobalTicket(ticketGlobal, FirebaseAuth.getInstance().getUid());
                    showSuccess("Ticket purchase successfull.", "You have succesffully purchased your tickets. Go to 'My Tickets' to see and use your tickets");
                },
                error -> {
                    Log.d("responsePurchase2ER", error + "");
                    showErrorAndGoBack(getString(R.string.ticket_purchase_error_title), getString(R.string.ticket_purchase_error_message), KAlertDialog.ERROR_TYPE, false);
                    ticketsToken = null;
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
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


    private void loginUserInPayments(final TripParent tripParent, String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //create list of request ticket creation json objects
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(PAYMENT_USER_ID, email);
            jsonBody.put(PAYMENT_PASSWORD, Constants.PAYMENT_DEFAULT_PASS);
            Log.d("payment", getPaymentPass());
            /*jsonBody.put(PAYMENT_USER_ID, "529116cc-33cc-4185-a915-77192a9658c2");
            jsonBody.put(PAYMENT_PASSWORD, "transdev");*/
        } catch (JSONException e){ }
        Log.d("paymentLogReq", jsonBody+"");
        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(
                Request.Method.POST,
                PAYMENTS_LOGIN_ACCOUNT,
                jsonBody,
                response -> {
                    Log.d("paymentLogResp", response+"");

                    String authTokenField = null;
                    String status = null;
                    try {
                        authTokenField = response.getJSONObject(PAYMENT_MESSAGE_FIELD).getString(PAYMENT_AUTH_TOKEN);
                        status = response.getJSONObject(PAYMENT_MESSAGE_FIELD).getString(PAYMENT_STATUS);
                        if (!status.equalsIgnoreCase(PAYMENT_STATUS_SUCCESSFULL)) {
                            showErrorAndGoBack("Payment Error", response.getJSONObject(PAYMENT_MESSAGE_FIELD).getString(PAYMENT_MESSAGE_FIELD), KAlertDialog.ERROR_TYPE, false);
                        }
                        authToken = authTokenField;
                        purchaseTicket(tripParent);
                    } catch (JSONException e) {
                        showErrorAndGoBack("Payment Error", "Error fetching payment account.", KAlertDialog.ERROR_TYPE, false);
                    }

                },
                error -> {
                    Log.d("errorPayment", error+"");
                    Toast.makeText(getContext(), "Could not login user in payment service", Toast.LENGTH_SHORT).show();
                    /*new AlertDialog.Builder(getApplication())
                            .setTitle(getString(R.string.ticket_purchase_error_title))
                            .setMessage(getString(R.string.ticket_purchase_error_message))
                            .setIcon(android.R.drawable.ic_dialog_alert).setNeutralButton("OK", null).show();*/
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private void openBrowser(String link) {
        this.browserOpened = true;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    private void showErrorAndGoBack(String title, String descr, int type, boolean goBack) {
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

    private void showSuccess(String title, String descr) {
        new KAlertDialog(getContext(), KAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(descr)
                .setConfirmText("OK")
                .confirmButtonColor(R.drawable.gree_confirm_btn_color)
                .setConfirmClickListener(sDialog -> sDialog.dismissWithAnimation())
                .show();
    }

    private String getPaymentID(){
        SharedPreferences sharedPref = getContext().getSharedPreferences(SHARED_PREFS_NAME, 0);
        return sharedPref.getString(FirebaseAuth.getInstance().getUid(), "");
    }

    private String getPaymentPass(){
        SharedPreferences sharedPref = getContext().getSharedPreferences(SHARED_PREFS_NAME, 0);
        return sharedPref.getString(FirebaseAuth.getInstance().getUid()+PAYMENT_SHAREDPREFERENCES_PASS, "");
    }
}
