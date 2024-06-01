package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ViewPostActivity extends AppCompatActivity {

    // ELEMENTS SETUP
    TextView tv_postTypeLabel, tv_itemName, tv_timeSinceLostOrFound, tv_locationFound, tv_itemDescription, tv_userName, tv_userPhone, tv_userDetailsHeading;
    Button btn_back, btn_delete;
    int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_post);

        // ELEMENTS SETUP
        tv_postTypeLabel = findViewById(R.id.tv_postTypeLabel);
        tv_itemName = findViewById(R.id.tv_itemName);
        tv_timeSinceLostOrFound = findViewById(R.id.tv_timeSinceLostOrFound);
        tv_locationFound = findViewById(R.id.tv_locationFound);
        tv_itemDescription = findViewById(R.id.tv_itemDescription);
        tv_userName = findViewById(R.id.tv_userName);
        tv_userPhone = findViewById(R.id.tv_userPhone);
        tv_userDetailsHeading = findViewById(R.id.tv_userDetailsHeading);
        btn_back = findViewById(R.id.btn_back);
        btn_delete = findViewById(R.id.btn_delete);

        // DATABASE GET POST
        Intent intent = getIntent();
        postId = intent.getIntExtra("ID", -1);
        DatabaseHelper dbHelper = new DatabaseHelper(ViewPostActivity.this);
        LostAndFoundModel LostAndFoundModel = dbHelper.getPostById(postId);

        // COMPLETE ELEMETNS AND FIELDS WITH DATA
        tv_postTypeLabel.setText(LostAndFoundModel.getPostType() + ":");
        tv_itemName.setText(LostAndFoundModel.getItemName());
        tv_timeSinceLostOrFound.setText(LostAndFoundModel.getItemFoundDate());
        tv_locationFound.setText(LostAndFoundModel.getItemFoundLocation());
        tv_itemDescription.setText(LostAndFoundModel.getItemDescription());
        tv_userDetailsHeading.setText("Contact the user who " + LostAndFoundModel.getPostType().toLowerCase() + " this item");
        tv_userName.setText(LostAndFoundModel.getFinderName());
        tv_userPhone.setText(LostAndFoundModel.getFinderPhone());

        // LOCATION LISTENER
        tv_locationFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPostActivity.this, ShowActivityMap.class);
                intent.putExtra("ID", postId);
                startActivity(intent);
            }
        });

        // BUTTON FUNCTIONALITY
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ViewPostActivity.this, ShowAll.class);
//                startActivity(intent);
                finish();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deletePostById(postId);
//                Intent intent = new Intent(ViewPostActivity.this, ShowAll.class);
//                startActivity(intent);
                finish();
            }
        });
    }
}