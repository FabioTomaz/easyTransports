package com.transports;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.transports.data.Stop;
import com.transports.data.Stoptime;

public class StopDetailActivity extends AppCompatActivity implements OnMapReadyCallback, StoptimeFragment.OnListFragmentInteractionListener {

    private GoogleMap mMap;
    private Stop stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stop_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        stop = (Stop) getIntent().getSerializableExtra("Stop");
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle(stop.getStop_name());
        ((TextView) findViewById(R.id.stop_id)).setText(stop.getStopId());
        ((TextView) findViewById(R.id.agency_name)).setText(stop.getAgency_key());
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // Creates an Intent that will load a map of San Francisco
            Uri gmmIntentUri = Uri.parse("geo:" + stop.getStop_lat()
                    + "," + stop.getStop_lon());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map
        );
        mapFragment.getMapAsync(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("stopId", stop.getStopId());
        StoptimeFragment stoptimeFragment = new StoptimeFragment();
        stoptimeFragment.setArguments(bundle);
        transaction.replace(R.id.stoptimes_list_fragment, stoptimeFragment);
        transaction.commit();
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
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(stop.getStop_lat(), stop.getStop_lon());
        mMap.addMarker(new MarkerOptions().position(location).title(stop.getStop_name()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Stoptime item) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new jsonRequest instance
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                GET_AGENCIES,
                null,
                response -> {
                    // make request for geojson
                    if (fileUris != null) {
                        Intent mapsIntent = new Intent(this, GoogleMapsActivity.class);
                        mapsIntent.putStringArrayListExtra(GeoJsonViewerConstants.INTENT_EXTRA_JSON_URI, fileUris);
                        mapsIntent.putIntegerArrayListExtra(GeoJsonViewerConstants.INTENT_EXTRA_JSON_COLORS, layerColors);
                        startActivity(mapsIntent);
                    } else {
                        Toast.makeText(
                            getApplicationContext(), 
                            R.string.geojson_opener_unable_to_read, 
                            Toast.LENGTH_LONG
                        ).show();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make(
                            getView(),
                            "Error...",
                            Snackbar.LENGTH_LONG
                    ).show();
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonRequest);
    }
}
