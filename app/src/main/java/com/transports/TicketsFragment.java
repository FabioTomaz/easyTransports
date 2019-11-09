package com.transports;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.transports.data.SQLiteDatabaseHandler;
import com.transports.expandable_list.schedule_list.TripChild;
import com.transports.expandable_list.tickets_list.MyTicketsListAdapter;
import com.transports.expandable_list.tickets_list.Ticket;
import com.transports.expandable_list.tickets_list.TicketGlobal;
import com.transports.expandable_list.tickets_list.TicketListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;
import static com.transports.data.URLs.CREATE_TICKET;
import static com.transports.utils.Constants.DATE_FIELD;
import static com.transports.utils.Constants.PRICE;
import static com.transports.utils.Constants.SCHEDULE;
import static com.transports.utils.Constants.SECRET_FIELD;
import static com.transports.utils.Constants.TICKET_INFO_FIELD;
import static com.transports.utils.Constants.TICKET_STATE_FIELD;
import static com.transports.utils.Constants.TICKET_STATUS_FIELD;
import static com.transports.utils.Constants.TRANSPORT_COMPANY;
import static com.transports.utils.Constants.TRIP;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TicketsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView cardRecyclerView;
    private TicketListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView detailsRecylerView;
    private List<TicketGlobal> ticketList;
    private int currentIndex;
    private TextView noTicketsText;
    private SQLiteDatabaseHandler bd;
    /**
     * You shouldn't define first page = 0.
     * Let define firstpage = 'number viewpager size' to make endless carousel
     */
    private static int FIRST_PAGE = 10;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TicketsFragment() {
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
        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bd = new SQLiteDatabaseHandler(getContext());

        noTicketsText = (TextView) getView().findViewById(R.id.no_tickets);
        noTicketsText.setVisibility(View.INVISIBLE);
        currentIndex = 0;
        //RecyclerView
        cardRecyclerView = (RecyclerView) getView().findViewById(R.id.tickets_list_viewpager);
        cardRecyclerView.setHasFixedSize(true);
        detailsRecylerView = (RecyclerView) getView().findViewById(R.id.tickets_list_details_viewpager);
        getData(); //load tickets from user, update its state from server and
    }

    public void setTicketsOnView(){
        //initialize global ticket cards recyclerview
        this.ticketList = bd.getAllGlobalTickets();
        mAdapter = new TicketListAdapter(getContext(), ticketList);

        cardRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        cardRecyclerView.setLayoutManager(mLayoutManager);

        if (ticketList.isEmpty()){
            noTicketsText.setVisibility(View.VISIBLE);
        }
        else{
            showTicketsListOfGlobalTickets(currentIndex);
        }

        cardRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = getCurrentItem();
                    //onPageChanged(position);
                    Log.i("item", getCurrentItem()+"");
                    if (currentIndex != position && position >= 0 && position < ticketList.size()) {
                        currentIndex = position;
                        showTicketsListOfGlobalTickets(currentIndex);
                    }
                }
            }
        });
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(cardRecyclerView);
    }

    private int getCurrentItem(){
        return ((LinearLayoutManager) cardRecyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    private void setCurrentItem(int position, boolean smooth){
        if (smooth)
            cardRecyclerView.smoothScrollToPosition(position);
        else
            cardRecyclerView.scrollToPosition(position);
    }

    /**load simple tickets from db and call function to request ticket status update
     *
     */
    public void getData() {
        //Log.d("dbtickets", bd.getAllGlobalTickets()+"");
        List<Ticket> ticketList = bd.getAllTickets();
        getTicketStatesFromServer (ticketList);
        //return ticketList;

        /*List<Ticket> tickets1 = new ArrayList<>();
        tickets1.add(new Ticket("CP ", "12:50-13:25", "Aveiro - Porto"));
        tickets1.add(new Ticket("Carris ", "12:59-13:32", "Aveiro - Porto"));
        tickets1.add(new Ticket("CP ", "13:10-13:42", "Aveiro - Porto"));

        List<Ticket> tickets2 = new ArrayList<>();
        tickets2.add(new Ticket("CP ", "12:50-13:25", "Aveiro - Coimbra"));
        tickets2.add(new Ticket("Carris ", "12:59-13:32", "Aveiro - Coimbra"));

        ticketList.add(new TicketGlobal("Aveiro - Porto", "CP", "8:30-9:30", tickets1));
        ticketList.add(new TicketGlobal("Aveiro - Coimbra", "CP + moveAveiro + metro", "13:30-14:30", tickets2));*/
    }

    public void getTicketStatesFromServer(List<Ticket> tickets){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        List<JSONObject> jsonTickets = new ArrayList<>();
        JSONObject jsonTicketStatus = new JSONObject();
        try{
            jsonTicketStatus = new JSONObject();
            JSONArray ticketIDs = new JSONArray();
            for (Ticket ticket : tickets) {
                ticketIDs.put(ticket.getId());
            }
            jsonTicketStatus.put(TICKET_STATUS_FIELD, ticketIDs);
        } catch (JSONException e){ }


        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(
                Request.Method.POST,
                CREATE_TICKET,
                jsonTicketStatus,
                new Response.Listener<JSONObject >() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Process the JSON
                        //for ticket id
                        //for (Ticket t : response)
                         //   bd.updateTicketState(t.getId(), t.getState());
                        //TODO: complete code that gets tickets status from each id and updates on db, then call setTicketsOnView()


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Could not get ticket status. Set tickets on view but its status may not be updated
                        Toast.makeText(getContext(), getString(R.string.ticket_status_error_message), Toast.LENGTH_SHORT).show();
                        setTicketsOnView();
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public static int getFirstPage() {
        return FIRST_PAGE;
    }

    public void showTicketsListOfGlobalTickets(int position){
        //initialize individual tickets recycler view
        Log.i("item", ticketList.get(position).getTickets()+"");
        MyTicketsListAdapter adapter = new MyTicketsListAdapter(getContext(), ticketList.get(position).getTickets());
        detailsRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsRecylerView.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        detailsRecylerView.addItemDecoration(decoration);
    }
}
