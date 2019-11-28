package com.transports;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.transports.data.Agency;

public class AgencyDetailActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Agency agency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agency_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        agency = (Agency) getIntent().getSerializableExtra("Agency");
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle(agency.getAgency_name());
        ((TextView)findViewById(R.id.agency_id)).setText(agency.getAgency_id());
        ((TextView)findViewById(R.id.agency_url)).setText(agency.getAgency_url());
        ((TextView)findViewById(R.id.agency_lang)).setText(agency.getAgency_lang());
        ((TextView)findViewById(R.id.agency_timezone)).setText(agency.getAgency_timezone());
        ((TextView)findViewById(R.id.agency_phone)).setText(agency.getAgency_phone());
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // Creates an Intent that will load a map of San Francisco
            Uri gmmIntentUri = Uri.parse(
                    "geo:" + agency.getAgency_center().get(1)
                            + "," + agency.getAgency_center().get(0));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });
        FloatingActionButton fabSearch = findViewById(R.id.fab_url);
        fabSearch.setOnClickListener(view -> {
            Uri uri = Uri.parse(agency.getAgency_url());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map
        );
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
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(agency.getAgency_center().get(1), agency.getAgency_center().get(0));
        mMap.addMarker(new MarkerOptions().position(location).title(agency.getAgency_name()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }

}
