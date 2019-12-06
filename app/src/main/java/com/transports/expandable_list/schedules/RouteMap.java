package com.transports.expandable_list.schedules;

import androidx.fragment.app.FragmentActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.transports.R;
import com.transports.data.Stop;

import java.util.ArrayList;
import java.util.List;

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
            addMarker(stop);
            if (previousStop != null && !stop.equals(previousStop)) {
                Stop stopDest = tripChildren.get(i).getDestination();
                addMarker(stopDest);
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

    private void addMarker(Stop stop){
        LatLng coords = new LatLng(stop.getStop_lat(), stop.getStop_lon());
        mMap.addMarker(new MarkerOptions().position(coords).title(stop.getStop_name()));
    }

    public float distance (float lat_a, float lng_a, float lat_b, float lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }
}
