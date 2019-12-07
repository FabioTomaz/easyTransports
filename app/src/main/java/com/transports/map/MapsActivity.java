package com.transports.map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import com.transports.R;

import java.util.ArrayList;

import static com.transports.utils.URLs.GET_TRIP_GEOJSON;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static String INTENT_EXTRA_TRIP_ID = "trip";

    private GoogleMap mMap;

    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getIntentExtras(getIntent());
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setTitle("Trip Map");
    }


    private String tripId;
    private ArrayList<Uri> jsonUris = new ArrayList<>();
    private Context context;

    protected void getIntentExtras(Intent intent) {
        context = getApplicationContext();
        tripId = intent.getStringExtra(INTENT_EXTRA_TRIP_ID);
    }

    public Context getContext() {
        return context;
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(38.736946, -9.142685);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new jsonRequest instance
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                GET_TRIP_GEOJSON.replace(":tripId", tripId),
                null,
                response -> {
                    GeoJsonLayer layer = new GeoJsonLayer(mMap, response);
                    addGeoJsonLayerToMap(layer);
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make(
                            this.mapFragment.getView(),
                            "Error...",
                            Snackbar.LENGTH_LONG
                    ).show();
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonRequest);
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


    private void addGeoJsonLayerToMap(GeoJsonLayer layer) {
        addColorsToMarkers(layer);
        layer.addLayerToMap();
        // Demonstrate receiving features via GeoJsonLayer clicks.
        layer.setOnFeatureClickListener(feature -> Toast.makeText(
                MapsActivity.this,
                "Feature clicked: " + feature.getProperty("route_id"),
                Toast.LENGTH_SHORT
        ).show());
    }

    /**
     * Adds a point style to all features to change the color of the marker based on its magnitude
     * property
     */
    private void addColorsToMarkers(GeoJsonLayer layer) {
        // Iterate over all the features stored in the layer
        for (GeoJsonFeature feature : layer.getFeatures()) {
            // Check if the magnitude property exists
            if (feature.getProperty("mag") != null && feature.hasProperty("place")) {
                double magnitude = Double.parseDouble(feature.getProperty("mag"));

                // Get the icon for the feature
                BitmapDescriptor pointIcon = BitmapDescriptorFactory
                        .defaultMarker(magnitudeToColor(magnitude));

                // Create a new point style
                GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();

                // Set options for the point style
                pointStyle.setIcon(pointIcon);
                pointStyle.setTitle("Magnitude of " + magnitude);
                pointStyle.setSnippet("Earthquake occured " + feature.getProperty("place"));

                // Assign the point style to the feature
                feature.setPointStyle(pointStyle);
            }
        }
    }

    /**
     * Assigns a color based on the given magnitude
     */
    private static float magnitudeToColor(double magnitude) {
        if (magnitude < 1.0) {
            return BitmapDescriptorFactory.HUE_CYAN;
        } else if (magnitude < 2.5) {
            return BitmapDescriptorFactory.HUE_GREEN;
        } else if (magnitude < 4.5) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else {
            return BitmapDescriptorFactory.HUE_RED;
        }
    }
}
