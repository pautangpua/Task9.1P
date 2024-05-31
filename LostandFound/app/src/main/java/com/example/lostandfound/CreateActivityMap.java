package com.example.lostandfound;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.lostandfound.databinding.ActivityCreateMapBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class CreateActivityMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityCreateMapBinding binding;
    Button btn_selectLocation;
    LatLng itemLocation;
    String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find our submit buton
        btn_selectLocation = findViewById(R.id.btn_selectLocation);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set up place search box
        Places.initialize(getApplicationContext(), "AIzaSyBU11jPtrDnak_YYmWjvSNCv0zPjX_yAow");
        AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.fr_placeSearch);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                // TO DO
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (place.getLatLng() != null) {
                    itemLocation = place.getLatLng();
                    mMap.addMarker(new MarkerOptions().position(itemLocation).title(place.getAddress()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, 17));
                }
            }
        });

        btn_selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemLocation != null) {
                    Intent intent = new Intent();
                    intent.putExtra("longitude", itemLocation.longitude);
                    intent.putExtra("latitude", itemLocation.latitude);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // HERE IM USING THE DEFAULT LOCATION AS THE DEAKIN 
        LatLng deakin = new LatLng(-37.84739654797611, 145.11492364027188);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(deakin, 14));
    }
}