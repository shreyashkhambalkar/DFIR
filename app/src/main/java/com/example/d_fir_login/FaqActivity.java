package com.example.d_fir_login;

import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

public class FaqActivity extends AppCompatActivity {

    ExpandableListView expandableTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        expandableTextView = findViewById(R.id.expandable_View);
        ExpandableTextViewAdapter adapter = new ExpandableTextViewAdapter(FaqActivity.this);
        expandableTextView.setAdapter(adapter);
    }
}