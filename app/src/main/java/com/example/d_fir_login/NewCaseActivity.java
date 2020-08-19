package com.example.d_fir_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d_fir_login.Model.Case;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewCaseActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    EditText txtCaseId, txtCaseCategory, txtOfficerName, txtage;
    Button btnAddCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Cases");

        txtCaseId = findViewById(R.id.txtCaseId);
        txtCaseCategory = findViewById(R.id.txtCaseCategory);
        txtOfficerName = findViewById(R.id.txtOfficerName);
        txtage = findViewById(R.id.txtage);

        btnAddCase = findViewById(R.id.btnAddCase);

        btnAddCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                String Date = dateFormat.format(calendar.getTime());
                final Case cases = new Case(txtCaseId.getText().toString(), txtCaseCategory.getText().toString(), txtOfficerName.getText().toString(), Date, txtage.getText().toString());

                reference.child(cases.getCaseId()).setValue(cases);

                Intent intent = new Intent(NewCaseActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });
    }
}