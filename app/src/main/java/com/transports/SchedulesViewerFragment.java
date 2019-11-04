package com.transports;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.transports.data.AppDataInfo;
import com.transports.data.Stop;
import com.transports.expandable_list.schedule_list.TripAdapter;
import com.transports.expandable_list.schedule_list.TripChild;
import com.transports.expandable_list.schedule_list.TripParent;
import com.transports.expandable_list.schedule_list.TripParentViewHolder;
import com.transports.expandable_list.tickets_list.TicketGlobal;
import com.transports.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;
import static com.transports.data.AppDataInfo.getAppContext;
import static com.transports.data.URLs.CREATE_TICKET;
import static com.transports.data.URLs.GET_ROUTE;
import static com.transports.data.URLs.GET_SCHEDULES;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SchedulesViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SchedulesViewerFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String transportCompany;
    private String date;
    private Stop origin;
    private Stop destination;
    private ArrayList<TripChild> schedulesList = new ArrayList<>();
    private ArrayList<TripParent> tripParentList = new ArrayList<>();
    private RecyclerView recycler;
    private TripParentViewHolder tripParentViewHolder;

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
            transportCompany = bundle.getString(Constants.TRANSPORT_COMPANY);
            date = bundle.getString(Constants.DEPARTURE_DATE);
            origin = (Stop) bundle.getSerializable(Constants.ORIGIN);
            destination = (Stop) bundle.getSerializable(Constants.DESTINATION);


            getActivity().setTitle(origin.getStopName()+" - "+destination.getStopName());

            //call service give info and receive


            //place all transports in the adapter
            schedulesList.add(new TripChild("CP ", "12/12/2019", "12:50", "13:25", "Aveiro", "Porto", 1.45));
            schedulesList.add(new TripChild("Carris ", "12/12/2019", "12:59", "13:32", "Aveiro", "Porto", 2.45));
            schedulesList.add(new TripChild("Move Aveiro ", "12/12/2019", "15:50", "16:15", "Aveiro", "Porto", 1.45));


            TripParent t1 = new TripParent("13:25", "14:20", "12/12/2019", "Aveiro", "Porto", schedulesList);
            TripParent t2 = new TripParent("14:25", "15:30", "12/12/2019", "Aveiro", "Porto", schedulesList);
            TripParent t3 = new TripParent("14:30", "15:50", "12/12/2019", "Aveiro", "Porto", schedulesList);
            final List<TripParent> tripParents = Arrays.asList(t1, t2, t3);


            recycler = (RecyclerView) getView().findViewById(R.id.schedules_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

            //instantiate your adapter with the list of genres
            TripAdapter adapter = new TripAdapter(tripParents, this);
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(adapter);

            DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
            recycler.addItemDecoration(decoration);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Get fragment one if exist.
                    Fragment schedulesFragment = new SchedulesFragment();

                    fragmentTransaction.replace(R.id.container, schedulesFragment);

                    // Do not add fragment three in back stack.
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    /*Fragment fragInstance = new SchedulesFragment();

                    getFragmentManager().beginTransaction()
                            .add(R.id.container, fragInstance)
                            .commit();*/

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().setTitle(getString(R.string.app_name_full));
    }

    public void getRoute(String originStopID, String destinationStopID) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonObjectRequest instance
        JsonArrayRequest jsonArrayRequest  = new JsonArrayRequest(
                Request.Method.GET,
                GET_ROUTE+"/"+originStopID+"/"+destinationStopID,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response

                        // Process the JSON
                        /*try {
                            Log.d("routes", response+"");
                            setStopsOnDropDowns();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(getContext(), "An error occured", Toast.LENGTH_SHORT).show();
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

    public void handlePurchase(int tripParentPosition){
        TripParent tripParent = tripParentList.get(tripParentPosition);
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
                        purchaseTicket(tripParent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void purchaseTicket(TripParent tripParent){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(
                Request.Method.GET,
                CREATE_TICKET,
                null,
                new Response.Listener<JSONObject >() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Process the JSON
                        //share the json in shared preferences:

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(getContext(), "An error occured", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
}
