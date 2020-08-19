package com.example.d_fir_login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d_fir_login.Model.Case;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PostAdapter extends FirebaseRecyclerAdapter<Case, PostAdapter.PostViewHolder> {

    public PostAdapter(@NonNull FirebaseRecyclerOptions<Case> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Case model) {

        holder.caseId.setText(model.getCaseId());
        holder.officerName.setText(model.getOfficerName());
        holder.age.setText(model.getAge());
        holder.caseCategory.setText(model.getCaseCategory());
        holder.timeStamp.setText(model.getTimeStamp());

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder, parent, false);

        return new PostViewHolder(view);
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView caseId, officerName, caseCategory, timeStamp, age;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            caseId = itemView.findViewById(R.id.caseId);
            officerName = itemView.findViewById(R.id.officerName);
            caseCategory = itemView.findViewById(R.id.caseCategory);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            cardView = itemView.findViewById(R.id.cardView);
            age = itemView.findViewById(R.id.age);
        }
    }
}