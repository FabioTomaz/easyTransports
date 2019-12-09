package com.transports.schedules;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.transports.R;
import com.transports.data.Stop;

import java.util.ArrayList;

import static com.transports.utils.Constants.TRIPS_EXTRA;

public class RouteMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_map_layout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_route);
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        ArrayList<TripChild> tripChildren;

        Intent intent = getIntent();
        if (intent == null){
            return;
        }
        Bundle bundle = getIntent().getExtras();
        tripChildren = bundle.getParcelableArrayList(TRIPS_EXTRA);
        Log.d("tripParent", tripChildren+", ");
        // Add a marker in Sydney and move the camera
        Stop previousStop = null;
        for (int i = 0; i < tripChildren.size(); i++){
            Stop stop = tripChildren.get(i).getOrigin();
            addMarker(stop, tripChildren.get(i).getOrigin().getStop_name()+"->"+tripChildren.get(i).getDestination().getStop_name(),
                    tripChildren.get(i).getCompanyName(), tripChildren.get(i).getSchedule());
            if (previousStop != null && !stop.equals(previousStop)) {
                Stop stopDest = tripChildren.get(i).getDestination();
                addMarker(stopDest, tripChildren.get(i).getOrigin().getStop_name()+"->"+tripChildren.get(i).getDestination().getStop_name(),
                        tripChildren.get(i).getCompanyName(), tripChildren.get(i).getSchedule());
            }
            previousStop = stop;
        }
        //zoom to middle marker
        /*int middle = tripChildren.size()/2;
        Stop stopMiddle = tripChildren.get(middle).getOrigin();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng( stopMiddle.getStop_lat(), stopMiddle.getStop_lon() )));*/
        Stop stopFirst = tripChildren.get(0).getOrigin();
        Stop stopLast = tripChildren.get(tripChildren.size()-1).getDestination();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(stopFirst.getStop_lat(), stopFirst.getStop_lon())).include(new LatLng(stopLast.getStop_lat(), stopLast.getStop_lon()));

        //Animate to the bounds
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 100);
        mMap.moveCamera(cameraUpdate);

    }

    private void addMarker(Stop stop, String trip, String transport, String schedule){
        LatLng coords = new LatLng(stop.getStop_lat(), stop.getStop_lon());
        mMap.addMarker(new MarkerOptions().position(coords).title(stop.getStop_name()).snippet(stop.getStop_name()+"\n"+trip+"\n"+transport+"\n"+schedule));
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.marker_layout, null);
            TextView tripTxt = (TextView) view.findViewById(R.id.trip_marker_text);
            TextView transportTxt = (TextView) view.findViewById(R.id.transport_marker_text);

            tripTxt.setText(marker.getTitle());
            transportTxt.setText(marker.getSnippet());
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

}
