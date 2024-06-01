package com.example.lostandfound;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ShowAll extends AppCompatActivity {

    // ELEMENTS ADDED
    Button btn_back;
    ListView lv_lostAndFound;
    ArrayAdapter postArrayAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_all);

        // ELEMENTS SETUP
        btn_back = findViewById(R.id.btn_back);
        lv_lostAndFound = findViewById(R.id.lv_lostAndFound);

        // GET DATA FROM DATABASE
        DatabaseHelper dbHelper = new DatabaseHelper(ShowAll.this);

        // OUTPUT LIST
        showPosts(dbHelper);

        // CLICK LIST FUNCTIONALITY
        lv_lostAndFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LostAndFoundModel LostAndFoundModel = (LostAndFoundModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(ShowAll.this, ViewPostActivity.class);
                intent.putExtra("ID", LostAndFoundModel.getId());
                startActivity(intent);
            }
        });

        // BACK BUTTON FUNCTIONALITY
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ShowAll.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
    private void showPosts(DatabaseHelper dbHelper) {
        postArrayAdapter = new ArrayAdapter<LostAndFoundModel>(ShowAll.this, android.R.layout.simple_list_item_1, dbHelper.getAll());
        lv_lostAndFound.setAdapter(postArrayAdapter);
    }
}