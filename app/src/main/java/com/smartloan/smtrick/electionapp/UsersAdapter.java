package com.smartloan.smtrick.electionapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<Users> mlist;

    public UsersAdapter(List<Users> mlist) {
        this.mlist = mlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userlist, parent, false);
        UsersAdapter.ViewHolder viewHolder = new UsersAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Users users = mlist.get(position);

        holder.name.setText(users.getName());
//        holder.mobile.setText(users.getContact());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.cardView.getContext(),UserDetailsActivity.class);
               intent.putExtra("user",users);
                holder.cardView.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView mobile;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            mobile = (TextView) itemView.findViewById(R.id.user_mobile);
            cardView = (CardView) itemView.findViewById(R.id.usercard);


        }
    }
}
