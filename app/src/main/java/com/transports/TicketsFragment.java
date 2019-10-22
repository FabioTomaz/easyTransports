package com.transports;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.transports.expandable_list.tickets_list.Ticket;
import com.transports.expandable_list.tickets_list.TicketListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TicketsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView mRecyclerView;
    private TicketListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * You shouldn't define first page = 0.
     * Let define firstpage = 'number viewpager size' to make endless carousel
     */
    private static int FIRST_PAGE = 10;

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
        //RecyclerView
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.tickets_list_viewpager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new TicketListAdapter(getContext(), getData());
        mRecyclerView.setAdapter(mAdapter);

        //Layout manager for the Recycler View
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = getCurrentItem();
                    //onPageChanged(position);
                    Log.i("item", getCurrentItem()+"");
                }
            }
        });
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
    }

    private int getCurrentItem(){
        return ((LinearLayoutManager)mRecyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    private void setCurrentItem(int position, boolean smooth){
        if (smooth)
            mRecyclerView.smoothScrollToPosition(position);
        else
            mRecyclerView.scrollToPosition(position);
    }

    public static List<Ticket> getData() {
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket("Aveiro - Porto", "CP + Metro", "12/10/2019", "12:00-13:00"));
        ticketList.add(new Ticket("Aveiro - Coimbra", "CP", "13/10/2019", "14:15-15:00"));
        ticketList.add(new Ticket("Aveiro - Coimbra", "CP", "14/10/2019", "9:30-10:15"));
        return ticketList;
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

    public static int getFirstPage() {
        return FIRST_PAGE;
    }
}
