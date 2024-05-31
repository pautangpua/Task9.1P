package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ShowAllMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;;
    DatabaseHelper dbHelper;
    List<LostAndFoundModel> items;
    Button btn_back, btn_select;
    Integer selectedId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_all_map);

        // Create our map fragment in our activity
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Get our items from the database
        dbHelper = new DatabaseHelper(ShowAllMap.this);
        items = dbHelper.getAll();;

        mapFragment.getMapAsync(this);

        // Provide a back button for users
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // If user selects item on map and clicks 'confirm', take them to that item
        btn_select = findViewById(R.id.btn_select);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId != null) {
                    Intent intent = new Intent(ShowAllMap.this, ViewPostActivity.class);
                    intent.putExtra("ID", selectedId);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Create our map
        mMap = googleMap;

        // Create a list we can add markers to, as well as making them visible on the map
        List<Marker> markers = new ArrayList<Marker>();

        // Iterate through our database items and make map markers out of them
        for (LostAndFoundModel item : items) {
            // First, parse our location string to convert it to a LagLng object
            String[] latlong = item.getItemFoundLocation().split(",");
            double itemLatitude = Double.parseDouble(latlong[0]);
            double itemLongitude = Double.parseDouble(latlong[1]);
            LatLng itemLocation = new LatLng(itemLatitude, itemLongitude);

            // Create a string for the title of our item
            String itemTitle = item.getId() + ": " + item.getItemName();

            // And add the marker to the map
            Marker itemMarker = mMap.addMarker(new MarkerOptions().position(itemLocation).title(itemTitle));
            markers.add(itemMarker);

            // Set an on-click listener to each marker, to store that ID in a global variable if selected
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    selectedId = Integer.parseInt(marker.getTitle().split(":")[0]);
                    return false;
                }
            });
        }

        // Now, use our list of markers to set an appropriate zoom level to make them all visible.
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 250;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

    }

}