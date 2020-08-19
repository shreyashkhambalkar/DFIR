package com.example.d_fir_login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d_fir_login.Model.Case;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchActivity extends AppCompatActivity {

    private EditText searchView;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseRecyclerOptions<Case> options;
    private Button btnCreateNewCase;
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Cases");

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        btnCreateNewCase = findViewById(R.id.btnCreateNewCase);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<Case>().setQuery(ref, Case.class).build();

        adapter = new PostAdapter(options);
        recyclerView.setAdapter(adapter);

        btnCreateNewCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, NewCaseActivity.class);
                startActivity(intent);
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString() != null)
                    LoadData(editable.toString());
                else
                    LoadData("");
            }
        });
    }

    private void LoadData(String s) {
        Query query = ref.orderByChild("caseId").startAt(s).endAt(s + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<Case>().setQuery(query, Case.class).build();

        adapter = new PostAdapter(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder, parent, false);

                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Case model) {
                holder.caseId.setText(model.getCaseId());
                holder.officerName.setText(model.getOfficerName());
                holder.age.setText(model.getAge());
                holder.caseCategory.setText(model.getCaseCategory());
                holder.timeStamp.setText(model.getTimeStamp());
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, DisplayCaseActivity.class);
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}