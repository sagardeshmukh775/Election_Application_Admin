package com.smartloan.smtrick.electionapp;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<Upload> uploads;

    public MyAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Upload upload = uploads.get(position);

        holder.textViewName.setText(upload.getName());
        holder.textViewdesc.setText(upload.getDesc());

        Glide.with(context).load(upload.getUrl()).placeholder(R.drawable.loading).into(holder.imageView);

        holder.imagecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent shared = new Intent(holder.imagecard.getContext(), sharedtransitionActivity.class);
//                Pair[] pair =  new Pair[3];
//                pair[0] = new Pair<View,String>(holder.textViewName,"text1transition");
//                pair[1] = new Pair<View,String>(holder.textViewdesc,"text2transition");
//                pair[2] = new Pair<View,String>(holder.imageView,"imagetransition");
//
//                ActivityOptions options =ActivityOptions.makeSceneTransitionAnimation(,pair);
                shared.putExtra("url",upload.getUrl());
                ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in, R.anim.slide_out);
                holder.imagecard.getContext().startActivity(shared,options.toBundle());



            }
        });

        holder.imagecard.setOnLongClickListener(new View.OnLongClickListener()

        {
            @Override
            public boolean onLongClick (View view){

                // custom dialog
                final Dialog dialog = new Dialog(holder.imagecard.getRootView().getContext());
                dialog.setContentView(R.layout.customdialogbox);
                //dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text2);
                text.setText(upload.getName());


                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                                        ///////3//////
                                        try {

                                            String item1 = upload.getName().toString();
                                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                                            Query applesQuery1 = ref1.child("NewImage").orderByChild("name").equalTo(item1);

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


                                            Toast.makeText(holder.imagecard.getContext(), "Delete Product Successfully", Toast.LENGTH_SHORT).show();
                                            uploads.clear();


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
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewdesc;
        public ImageView imageView;
        public CardView imagecard;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewdesc = (TextView) itemView.findViewById(R.id.textViewdescription);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imagecard = (CardView)itemView.findViewById(R.id.cardimage);
        }
    }
}
