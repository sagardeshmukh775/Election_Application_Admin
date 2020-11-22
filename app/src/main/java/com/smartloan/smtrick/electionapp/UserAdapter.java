package com.smartloan.smtrick.electionapp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private ArrayList<Users> invoiceArrayList;
    private Context context;
    private DatabaseReference mDatabase;

    public UserAdapter(Context context, ArrayList<Users> data) {
        this.invoiceArrayList = data;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userlist, parent, false);
        UserAdapter.ViewHolder viewHolder = new UserAdapter.ViewHolder(v);
//        notifyDataSetChanged();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        Users users = invoiceArrayList.get(position);

//        if (users.getStatus().equalsIgnoreCase("Active")){
//            holder.Activate.setText("Active");
//            holder.Activate.setBackgroundColor(Integer.parseInt("#008000"));
//        }

        holder.Dname.setText(users.getName());
//        holder.Designation.setText(users.getStorename());

        holder.usercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(holder.usercard.getRootView().getContext());
                dialog.setContentView(R.layout.activity_user_details);

                dialog.setTitle("Title...");
                TextView name, email, mobile, store;
                Button dismiss;
                name = (TextView) dialog.findViewById(R.id.user_name);
                store = (TextView) dialog.findViewById(R.id.user_store1);
                email = (TextView) dialog.findViewById(R.id.user_email1);
                mobile = (TextView) dialog.findViewById(R.id.user_mobile1);
                dismiss = (Button) dialog.findViewById(R.id.dialogButtonaccept);

                name.setText(users.getName());
//                store.setText(users.getStorename());
                email.setText(users.getEmail());
//                mobile.setText(users.getContact());
                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        holder.Activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").child(users.getUserId()).child("status").setValue("Active");
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return invoiceArrayList.size();
    }

    public void reload(ArrayList<Users> arrayList) {
        invoiceArrayList.clear();
        invoiceArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Dname, Designation;
        CardView usercard;
        Button Activate;

        public ViewHolder(View itemView) {
            super(itemView);

            Dname = (TextView) itemView.findViewById(R.id.name);
            Designation = (TextView) itemView.findViewById(R.id.user_mobile);
            usercard = (CardView) itemView.findViewById(R.id.usercard);
            Activate = (Button) itemView.findViewById(R.id.btnactive);
        }
    }


//    @Override
//    public UserViewHolder onCreateViewHolder(ViewGroup parent,
//                                                int viewType) {
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.layout_images, parent, false);
//        MyAdapter.ViewHolder viewHolder = new UsersAdapter(v).ViewHolder(v);
//        return viewHolder;
//    }
//
//    private Users getModel(int position) {
//        return (invoiceArrayList.get(invoiceArrayList.size() - 1 - position));
//    }
//
//    @Override
//    public void onBindViewHolder(final UserViewHolder holder, final int listPosition) {
//        try {
//            Users user = getModel(listPosition);
//            holder.txtcnamevalue.setText(user.getUserName());
//            holder.txtbankvalue.setText(user.getMobileNumber());
//           // holder.invoiceAdapterLayoutBinding.txtbankvalue.setText(invoice.getUserName());
//          //  holder.invoiceAdapterLayoutBinding.txtStatusValue.setText(invoice.getUserName());
//          //  holder.invoiceAdapterLayoutBinding.txtcommisionvalue.setText(invoice.getUserName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return invoiceArrayList.size();
//    }
//
//    public void reload(ArrayList<Users> arrayList) {
//        invoiceArrayList.clear();
//        invoiceArrayList.addAll(arrayList);
//        notifyDataSetChanged();
//    }
}