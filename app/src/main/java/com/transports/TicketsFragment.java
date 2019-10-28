package com.transports;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transports.expandable_list.tickets_list.MyTicketsListAdapter;
import com.transports.expandable_list.tickets_list.Ticket;
import com.transports.expandable_list.tickets_list.TicketGlobal;
import com.transports.expandable_list.tickets_list.TicketListAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;


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
    private int currentTicketIndex;
    private TextView noTicketsText;

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
        noTicketsText = (TextView) getView().findViewById(R.id.no_tickets);
        noTicketsText.setVisibility(View.VISIBLE);
        //RecyclerView
        cardRecyclerView = (RecyclerView) getView().findViewById(R.id.tickets_list_viewpager);
        cardRecyclerView.setHasFixedSize(true);
        detailsRecylerView = (RecyclerView) getView().findViewById(R.id.tickets_list_details_viewpager);

        //initialize global ticket cards recyclerview
        this.ticketList = getData();
        mAdapter = new TicketListAdapter(getContext(), ticketList);

        cardRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        cardRecyclerView.setLayoutManager(mLayoutManager);

        currentTicketIndex = 0;

        if (ticketList.isEmpty()){
            noTicketsText.setVisibility(View.VISIBLE);
        }
        else
            showTicketsListForGlobalTicket(currentTicketIndex);

        cardRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = getCurrentItem();
                    //onPageChanged(position);
                    Log.i("item", getCurrentItem()+"");
                    if (currentTicketIndex != position ){
                        currentTicketIndex = position;
                        //initialize individual tickets recycler view
                        showTicketsListForGlobalTicket(currentTicketIndex);
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

    public static List<TicketGlobal> getData() {
        List<TicketGlobal> ticketList = new ArrayList<>();
        /*String[] cardTitle = {
                "Card 1",
                "Card 2",
                "Card 3",
                "Card 4",
                "Card 5",
                "Card 6",
        };
        for (int i = 0; i < cardTitle.length; i++) {
            TicketGlobal current = new TicketGlobal();
            current.setOriginDestination(cardTitle[i]);
            ticketList.add(current);
        }

        return ticketList;*/

        List<Ticket> tickets1 = new ArrayList<>();
        tickets1.add(new Ticket("CP ", "12:50-13:25", "Aveiro - Porto"));
        tickets1.add(new Ticket("Carris ", "12:59-13:32", "Aveiro - Porto"));
        tickets1.add(new Ticket("CP ", "13:10-13:42", "Aveiro - Porto"));

        List<Ticket> tickets2 = new ArrayList<>();
        tickets1.add(new Ticket("CP ", "12:50-13:25", "Aveiro - Porto"));
        tickets1.add(new Ticket("Carris ", "12:59-13:32", "Aveiro - Porto"));
        tickets1.add(new Ticket("CP ", "13:10-13:42", "Aveiro - Porto"));

        ticketList.add(new TicketGlobal("Aveiro - Porto", "CP", "8:30-9:30", tickets1));
        ticketList.add(new TicketGlobal("Aveiro - Coimbra", "CP + moveAveiro + metro", "13:30-14:30", tickets2));

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

    private void showTicketsListForGlobalTicket (int position){
        Log.i("item", ticketList.get(position).getTickets()+"");
        MyTicketsListAdapter adapter = new MyTicketsListAdapter(getContext(), ticketList.get(position).getTickets());
        detailsRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsRecylerView.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        detailsRecylerView.addItemDecoration(decoration);
    }
}
