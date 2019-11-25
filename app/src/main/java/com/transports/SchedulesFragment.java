package com.transports;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.transports.data.AppDataInfo;
import com.transports.data.Stop;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import static com.transports.utils.Constants.DESTINATION;
import static com.transports.utils.Constants.ORIGIN;
import static com.transports.utils.Constants.STOP_INFO_AGENCY_KEY;
import static com.transports.utils.Constants.STOP_INFO_COORDS_FIELD;
import static com.transports.utils.Constants.STOP_INFO_ID_FIELD;
import static com.transports.utils.Constants.STOP_INFO_NAME_FIELD;
import static com.transports.utils.Constants.STOP_INFO_TRANSPORT_FIELD;
import static com.transports.utils.URLs.GET_SCHEDULES;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SchedulesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SchedulesFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private AutoCompleteTextView originDropdown;
    private AutoCompleteTextView destinationDropdown;
    private Button btnTimePicker;

    private String[] stopNames;

    private int mHour, mMinute;
    private Date currentDate = new Date();

    public SchedulesFragment() {
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
        return inflater.inflate(R.layout.fragment_schedules, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        originDropdown = getView().findViewById(R.id.departure_stop);
        destinationDropdown = getView().findViewById(R.id.arrival_stop);
        btnTimePicker = getView().findViewById(R.id.btn_time);

        btnTimePicker.setOnClickListener(this);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, availableTransports);
        //originDropdown.setAdapter(adapter);


        if (AppDataInfo.stops.isEmpty()) {
            getAllStops();
        } else {
            setStopsOnDropDowns();
        }
        //
        final Button searchSchedulesBtn = (Button) getView().findViewById(R.id.schedules_submit_button);

        searchSchedulesBtn.setOnClickListener(v -> {
            //validate input
            if (TextUtils.isEmpty(originDropdown.getText())) {
                originDropdown.setError(getString(R.string.origin_error_message));
                return;
            }
            if (TextUtils.isEmpty(destinationDropdown.getText())) {
                destinationDropdown.setError(getString(R.string.destination_error_message));
                return;
            }

            //user cannot select same origin and destination stop
            if (destinationDropdown.getText().toString().equals(originDropdown.getText().toString())) {
                destinationDropdown.setError(getString(R.string.stop_error_message2));
                return;
            }

            Log.d("stop", originDropdown.getText().toString());
            int originSelectedIndex = -1;
            int destinationSelectedIndex = -1;
            for (int i = 0; i < stopNames.length; i++) {
                if (stopNames[i].equals(originDropdown.getText().toString()))
                    originSelectedIndex = i;
                if (stopNames[i].equals(destinationDropdown.getText().toString()))
                    destinationSelectedIndex = i;
            }
            if (originSelectedIndex < 0 || originSelectedIndex >= AppDataInfo.stops.size()) {
                originDropdown.setError(getString(R.string.stop_error_message));
                return;
            }
            if (destinationSelectedIndex < 0 || destinationSelectedIndex >= AppDataInfo.stops.size()) {
                destinationDropdown.setError(getString(R.string.stop_error_message));
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable(ORIGIN, AppDataInfo.stops.get(originSelectedIndex));
            bundle.putSerializable(DESTINATION, AppDataInfo.stops.get(destinationSelectedIndex));
            //bundle.putBoolean(ARRIVAL_DATE, isArrivalDate);
            //bundle.putSerializable(TIME, time);
            //bundle.putInt(VARIANCE, variance);

            SchedulesViewerFragment schedulesViewFragment = new SchedulesViewerFragment();
            schedulesViewFragment.setArguments(bundle);

            getFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, schedulesViewFragment)
                    .commit();
        });
    }


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

    @Override
    public void onClick(View v) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this.getContext(),
                (view, hourOfDay, minute) -> {
                    btnTimePicker.setText(hourOfDay + ":" + minute);
                    //date.setHours(hourOfDay);
                    //date.setMinutes(minute);
                },
                mHour,
                mMinute,
                false
        );
        timePickerDialog.show();
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

    private void getAllStops() {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonObjectRequest instanceresponse
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                GET_SCHEDULES,
                null,
                response -> {
                    // Process the JSON
                    try {
                        // Get the JSON array
                        // Loop through the array elements
                        for (int i = 0; i < response.length(); i++) {
                            // Get current json object
                            JSONObject stop = response.getJSONObject(i);

                            // Get the current student (json object) data
                            String stopId = stop.getString(STOP_INFO_ID_FIELD);
                            String stopName = stop.getString(STOP_INFO_NAME_FIELD);
                            String stopTransport = stop.getString(STOP_INFO_TRANSPORT_FIELD);
                            String agencyKey = stop.getString(STOP_INFO_AGENCY_KEY);
                            double stopLat = Double.parseDouble(stop.getJSONArray(STOP_INFO_COORDS_FIELD).getString(0));
                            double stopLong = Double.parseDouble(stop.getJSONArray(STOP_INFO_COORDS_FIELD).getString(1));
                            Stop stopObject = new Stop(stopId, stopName, stopTransport, stopLat, stopLong, agencyKey);
                            AppDataInfo.stops.add(stopObject);
                        }
                        setStopsOnDropDowns();
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    public void setStopsOnDropDowns() {
        stopNames = new String[AppDataInfo.stops.size()];
        for (int i = 0; i < AppDataInfo.stops.size(); i++) {
            Log.i("test", AppDataInfo.stops.get(i).getStop_name() + AppDataInfo.stops.get(i).getAgency_key());
            stopNames[i] = AppDataInfo.stops.get(i).getStop_name() + " (" + AppDataInfo.stops.get(i).getAgency_key() + ")";
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, stopNames);

        originDropdown.setAdapter(adapter);
        originDropdown.postDelayed(() -> {
            originDropdown.setThreshold(1); //will start working from first character
            originDropdown.setAdapter(adapter);
        }, 10);

        destinationDropdown.setAdapter(adapter);
        destinationDropdown.postDelayed(() -> {
            destinationDropdown.setThreshold(1); //will start working from first character
            destinationDropdown.setAdapter(adapter);

        }, 10);
    }
}
