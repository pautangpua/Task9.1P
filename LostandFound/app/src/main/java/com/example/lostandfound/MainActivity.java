package com.example.lostandfound;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // ELEMENTS PASSING
    Button btn_createNew, btn_showAll, btn_showAllOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ELEMENTS SETUP
        btn_createNew = findViewById(R.id.btn_createNew);
        btn_showAll = findViewById(R.id.btn_showAll);
        btn_showAllOnMap = findViewById(R.id.btn_showAllOnMap);

        // BUTTON ELEMENTS FUNCTIONALITY SETUP
        btn_createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        btn_showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowAll.class);
                startActivity(intent);
            }
        });

        btn_showAllOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowAllMap.class);
                startActivity(intent);
            }
        });
    }
}