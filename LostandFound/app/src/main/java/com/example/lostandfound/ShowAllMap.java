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

        // CREATE MAP FRAGMENT IN ACTIVITY
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // GET DATA FROM DATABASE
        dbHelper = new DatabaseHelper(ShowAllMap.this);
        items = dbHelper.getAll();;

        mapFragment.getMapAsync(this);

        // BACK BUTTON FUNCTIONALITY
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // SELECT AND CONFIRM ON MAP
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

        // MAP CREATION
        mMap = googleMap;

        // CREATE ARRAY LIST FOR MAP
        List<Marker> markers = new ArrayList<Marker>();

        // DATABASE DATA ARE PLACED INTO MARKER
        for (LostAndFoundModel item : items) {
            String[] latlong = item.getItemFoundLocation().split(",");
            double itemLatitude = Double.parseDouble(latlong[0]);
            double itemLongitude = Double.parseDouble(latlong[1]);
            LatLng itemLocation = new LatLng(itemLatitude, itemLongitude);

            // GET DATABASE WITH ID AND NAME
            String itemTitle = item.getId() + ": " + item.getItemName();

            // ADD MARKER TO MAP
            Marker itemMarker = mMap.addMarker(new MarkerOptions().position(itemLocation).title(itemTitle));
            markers.add(itemMarker);

            // SIMPLE ON CLICK LISTENER FOR MARKER
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    selectedId = Integer.parseInt(marker.getTitle().split(":")[0]);
                    return false;
                }
            });
        }

        // ZOOM ABILITY
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