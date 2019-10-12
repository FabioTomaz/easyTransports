package com.transports;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transports.data.Schedule;
import com.transports.data.SchedulesAdapter;
import com.transports.utils.Constants;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SchedulesViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SchedulesViewerFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String transportCompany;
    private String departureDate;
    private SchedulesAdapter listAdapter;
    private ArrayList<Schedule> schedulesList = new ArrayList<>();
    private RecyclerView recycler;

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
            departureDate = bundle.getString(Constants.DEPARTURE_DATE);

            //call service give info and receive


            //place this on the rest api call response
            recycler = getView().findViewById(R.id.schedules_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(layoutManager);
            listAdapter = new SchedulesAdapter(schedulesList, getContext());
            recycler.setAdapter(listAdapter);

            //Load the date from the network or other resources
            //into the array list asynchronously

            schedulesList.add(new Schedule("CP ", "Aveiro"));
            schedulesList.add(new Schedule("CP", "Estarreja"));
            schedulesList.add(new Schedule("CP", "Aveiro"));

            listAdapter.notifyDataSetChanged();
        }
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
}
