package com.example.d_fir_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    Button my_profile, speech,  contact_us, faq, gen_65b, scan, my_case, upload;
    private String id, policeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        my_profile = findViewById(R.id.my_profile);
        speech = findViewById(R.id.speech);
        contact_us = findViewById(R.id.contact_us);
        faq = findViewById(R.id.faq);
        gen_65b = findViewById(R.id.gen_65b);
        scan = findViewById(R.id.scan);
        my_case = findViewById(R.id.my_case);
        upload = findViewById(R.id.upload);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("EmployeeId");
        policeName = extras.getString("OfficerName");

        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }

        });
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, SpeechActivity.class);
                startActivity(intent);
            }

        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, FaqActivity.class);
                startActivity(intent);
            }

        });

        my_case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
                startActivity(intent);

            }

        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, UploadActivity.class);

                Bundle extras1 = new Bundle();
                extras1.putString("EmployeeId", id);
                extras1.putString("OfficerName", policeName);
                intent.putExtras(extras1);

                startActivity(intent);
            }
        });
        gen_65b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, Search65BActivity.class);
                startActivity(intent);
            }

        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, SearchScanActivity.class);

                Bundle extras2 = new Bundle();
                extras2.putString("EmployeeId", id);
                intent.putExtras(extras2);

                startActivity(intent);
            }
        });


    }

}