package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowActivityMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper dbHelper;
    int postId;
    LostAndFoundModel item;
    Button btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_map);

        // CREATE MAP FRAGMENT TO ACTIVITY
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // GET ITEM ID FROM INTENT
        Intent intent = getIntent();
        postId = intent.getIntExtra("ID", -1);

        // GET THE POST FROM ID
        dbHelper = new DatabaseHelper(ShowActivityMap.this);
        item = dbHelper.getPostById(postId);

        mapFragment.getMapAsync(this);

        // BACK BUTTON AND FUNCTIONALITY
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // MAP CREATION
        mMap = googleMap;

        String[] latlong = item.getItemFoundLocation().split(",");
        double itemLatitude = Double.parseDouble(latlong[0]);
        double itemLongitude = Double.parseDouble(latlong[1]);
        LatLng itemLocation = new LatLng(itemLatitude, itemLongitude);

        // BUILD MARKER ON MAP WITH GET ID AND NAME
        String itemTitle = item.getId() + ": " + item.getItemName();

        // ADD MARKER TO THE NAME
        Marker itemMarker = mMap.addMarker(new MarkerOptions().position(itemLocation).title(itemTitle));

        // ZOOM ABILITY
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, 14));
    }
}