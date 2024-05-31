package com.example.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

public class CreateActivity extends AppCompatActivity {

    // References for screen elements
    Button btn_back, btn_save, btn_currentLocation;
    EditText et_itemName, et_itemDescription, et_itemFoundDate, et_itemLocation, et_userName, et_userPhone;
    RadioButton rbtn_lost, rbtn_found;

    // Global variables
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng currentLocation = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);

        // Find screen elements
        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);
        btn_currentLocation = findViewById(R.id.btn_currentLocation);
        et_itemName = findViewById(R.id.et_itemName);
        et_itemDescription = findViewById(R.id.et_itemDescription);
        et_itemFoundDate = findViewById(R.id.et_itemFoundDate);
        et_itemLocation = findViewById(R.id.et_itemLocation);
        et_userName = findViewById(R.id.et_userName);
        et_userPhone = findViewById(R.id.et_userPhone);
        rbtn_lost = findViewById(R.id.rbtn_lost);
        rbtn_found = findViewById(R.id.rbtn_found);

        // Create location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
        };

        // Set up functionality for selecting location on map
        et_itemLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the user clicks into the location text box, bring up a Google Maps activity
                Intent intent = new Intent(CreateActivity.this, CreateActivityMap.class);
                // When we return from the Google Maps activity to this one, grab the longitude and
                // latitude from it with the myActivityResultLauncher callback.
                myActivityResultLauncher.launch(intent);
            }
        });

        // Set up functionality for selecting current location
        btn_currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If we have a current location
                if (currentLocation != null) {
                    // Populate location field with latitude and longitude
                    String currentLocationString = Double.toString(currentLocation.latitude) + "," + Double.toString(currentLocation.longitude);
                    et_itemLocation.setText(currentLocationString);
                }
            }
        });

        // When the user clicks save, perform field validation and save to database
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LostAndFoundModel LostAndFoundModel;

                // First, get all fields
                String itemName = et_itemName.getText().toString();
                String itemDescription = et_itemDescription.getText().toString();
                String itemFoundDate = et_itemFoundDate.getText().toString();
                String itemLocation = et_itemLocation.getText().toString();
                String userName = et_userName.getText().toString();
                String userPhone = et_userPhone.getText().toString();
                String postType;
                if (rbtn_found.isChecked()) {
                    postType = "Found";
                } else { // Lost is checked by default in the XML, so we know it's one of the two
                    postType = "Lost";
                }

                // Strings for matching due date and phone number fields
                String dateMatchRegex = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
                String phoneMatchRegex = "^(?:\\+?(61))? ?(?:\\((?=.*\\)))?(0?[2-57-8])\\)? ?(\\d\\d(?:[- ](?=\\d{3})|(?!\\d\\d[- ]?\\d[- ]))\\d\\d[- ]?\\d[- ]?\\d{3})$"; // source: https://regex101.com/r/dkFASs/6

                // Validate fields aren't blank and valid
                if (itemName.equals("")) {
                    Toast.makeText(CreateActivity.this, "Item name is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if (itemFoundDate.equals("")) {
                    Toast.makeText(CreateActivity.this, "Please enter a date the item was lost or found", Toast.LENGTH_SHORT).show();
                }
                // Check that a valid found date has been entered
                else if (!itemFoundDate.matches(dateMatchRegex)) {
                    Toast.makeText(CreateActivity.this, "Lost/found date must be in format yyyy-mm-dd", Toast.LENGTH_SHORT).show();
                }
                else if (itemLocation.equals("")) {
                    Toast.makeText(CreateActivity.this, "Location field is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if (userName.equals("")) {
                    Toast.makeText(CreateActivity.this, "Your name is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if (userPhone.equals("")) {
                    Toast.makeText(CreateActivity.this, "Your phone number is mandatory", Toast.LENGTH_SHORT).show();
                }
                else if (!userPhone.matches(phoneMatchRegex)) {
                    Toast.makeText(CreateActivity.this, "Please enter a valid Australian phone number with area code", Toast.LENGTH_SHORT).show();
                }
                // If everything was OK, add the post to the database
                else {
                    DatabaseHelper dbHelper = new DatabaseHelper(CreateActivity.this);
                    LostAndFoundModel = new LostAndFoundModel(postType, itemName, itemDescription, itemFoundDate, itemLocation, userName, userPhone);
                    boolean success = dbHelper.addPost(LostAndFoundModel);
                    if (success) {
                        Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });

        // If they click back instead, take them back to the main activity screen without saving
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CreateNew.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        // Ask for location permission, if needed
        if (ActivityCompat.checkSelfPermission(CreateActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
        }

    }

    ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        double latitude = data.getDoubleExtra("latitude", 0);
                        double longitude = data.getDoubleExtra("longitude", 0);
                        String latLngString = latitude + "," + longitude;
                        et_itemLocation.setText(latLngString);
                    }
                }
            }
    );
}

/***
 * 10:43 PM 23/05/2024 to implement tomorrow
 * -----------------------------------------
 * / View all
 * 	/ Map activity
 * 	/ Get all from database
 * 	/ Iterate over their locations, parsing each string as LatLng
 * 	/ Place a pin on the map for each.
 * 		- Make each clickable?
 * / View item on map
 * 	/ When location text field is clicked on, launch ViewSingle activity
 * 	/ Pass location string to new ViewSingle intent
 * - Current location functionality
 */