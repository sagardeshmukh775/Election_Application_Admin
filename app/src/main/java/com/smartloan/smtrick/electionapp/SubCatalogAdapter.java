package com.smartloan.smtrick.electionapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class SubCatalogAdapter extends RecyclerView.Adapter<SubCatalogAdapter.ViewHolder> {

    private Context context;
    private List<String> sublist;
    String mainproductname;

    public SubCatalogAdapter(List<String> catalogList, String mainproductname) {
        this.mainproductname = mainproductname;
        sublist = catalogList;
    }

    @Override
    public SubCatalogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catalogsublist, parent, false);
        SubCatalogAdapter.ViewHolder viewHolder = new SubCatalogAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SubCatalogAdapter.ViewHolder holder, final int position) {
        final String subcatname = sublist.get(position);
        holder.catalogSubname.setText(subcatname);

        if (position % 2 == 0) {
            holder.catalogSubname.setBackgroundColor(Color.parseColor("#787474"));
        } else {
            holder.catalogSubname.setBackgroundColor(Color.parseColor("#ff6861"));
        }

        holder.subcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do on click stuff
                Toast.makeText(holder.catalogSubname.getContext(), sublist.get(position),
                        Toast.LENGTH_SHORT).show();
                Intent subintent = new Intent(holder.catalogSubname.getContext(), ShowImagesActivity.class);
                subintent.putExtra("mianproduct", mainproductname);
                subintent.putExtra("subproduct", subcatname);
                holder.catalogSubname.getContext().startActivity(subintent);
            }
        });



  holder.subcardView.setOnLongClickListener(new View.OnLongClickListener()

    {
        @Override
        public boolean onLongClick (View view){

         // custom dialog
            final Dialog dialog = new Dialog(holder.subcardView.getRootView().getContext());
            dialog.setContentView(R.layout.customdialogbox);
            //dialog.setTitle("Title...");

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text2);
            text.setText(subcatname);


            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        //////////2/////
                                                                        String item = sublist.get(position).toString();
                                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                                        Query applesQuery = ref.child("SubProducts").orderByChild("subproduct").equalTo(item);

                                                                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                                                    ///////3//////
                                                                                    try {

                                                                                        String item1 = sublist.get(position).toString();
                                                                                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                                                                                        Query applesQuery1 = ref1.child("NewImage").orderByChild("mainproduct").equalTo(item1);

                                                                                        applesQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                                                                    appleSnapshot.getRef().removeValue();

                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError databaseError) {
                                                                                                Log.e(TAG, "onCancelled", databaseError.toException());
                                                                                            }
                                                                                        });

                                                                                        appleSnapshot.getRef().removeValue();
                                                                                        Toast.makeText(holder.catalogSubname.getContext(), "Delete Product Successfully", Toast.LENGTH_SHORT).show();
                                                                                        sublist.clear();

                                                                                    } catch (Exception e) {
                                                                                    }
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {
                                                                                Log.e(TAG, "onCancelled", databaseError.toException());
                                                                            }
                                                                        });



                                                                    } catch (Exception e) {
                                                                    }



                                                    dialog.dismiss();
                                                }

            });

            dialog.show();




        return true;
    }
    });


}

@Override
    public int getItemCount() {
        return sublist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView catalogSubname;
        public CardView subcardView;

        public ViewHolder(View itemView) {
            super(itemView);

            catalogSubname = (TextView) itemView.findViewById(R.id.subcatalog_name);
            subcardView = (CardView) itemView.findViewById(R.id.newcard);
        }
    }
}
