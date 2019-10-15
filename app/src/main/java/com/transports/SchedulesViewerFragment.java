package com.transports;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private String origin;
    private String destination;
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
            origin = bundle.getString(Constants.ORIGIN);
            destination = bundle.getString(Constants.DESTINATION);


            getActivity().setTitle(origin+" - "+destination);

            //call service give info and receive


            //place this on the rest api call response
            recycler = getView().findViewById(R.id.schedules_list);
            recycler.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL));//add separator
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(layoutManager);
            listAdapter = new SchedulesAdapter(schedulesList, getContext());
            recycler.setAdapter(listAdapter);

            //Load the date from the network or other resources
            //into the array list asynchronously

            //place all transports in the adapter
            schedulesList.add(new Schedule("CP ", "12/12/2019", "12:50", "13:25", "Aveiro", "Porto", 1.45));
            schedulesList.add(new Schedule("Carris ", "12/12/2019", "12:59", "13:32", "Aveiro", "Porto", 2.45));
            schedulesList.add(new Schedule("CP ", "12/12/2019", "13:10", "13:42", "Aveiro", "Porto", 1.45));
            schedulesList.add(new Schedule("CP ", "12/12/2019", "13:20", "13:52", "Aveiro", "Porto", 1.45));
            schedulesList.add(new Schedule("CP ", "12/12/2019", "14:50", "15:10", "Aveiro", "Porto", 1.45));
            schedulesList.add(new Schedule("Move Aveiro ", "12/12/2019", "15:50", "16:15", "Aveiro", "Porto", 1.45));

            listAdapter.notifyDataSetChanged();
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

                    Fragment fragInstance = new SchedulesFragment();

                    getFragmentManager().beginTransaction()
                            .add(R.id.container, fragInstance)
                            .commit();

                    return true;
                }
                return false;
            }
        });
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
