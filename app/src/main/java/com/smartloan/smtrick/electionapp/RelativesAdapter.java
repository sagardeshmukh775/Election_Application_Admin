package com.smartloan.smtrick.electionapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RelativesAdapter extends RecyclerView.Adapter<RelativesAdapter.ViewHolder> {

    private Context context;
    private List<RelationVO> list;
    String Language;


    String item;


    public RelativesAdapter(Context context, List<RelationVO> list, String language) {
        this.context = context;
        this.list = list;
        this.Language = language;
    }


    @Override
    public RelativesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relativelist, parent, false);
        RelativesAdapter.ViewHolder viewHolder = new RelativesAdapter.ViewHolder(v);
        //  context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RelativesAdapter.ViewHolder holder, final int position) {
        //String pname = list.get(position);

        if (Language.equalsIgnoreCase("Marathi")) {
            holder.name.setText(R.string.register_membername);
            holder.address.setText(R.string.address);
            holder.contact.setText(R.string.card_contact);
        }
        final RelationVO pveo = list.get(position);

        holder.name1.setText(pveo.getRelationname());
        holder.contact1.setText(pveo.getRelationcontact());
        holder.address1.setText(pveo.getRelationaddress());
//        Glide.with(holder.cardView.getContext()).load(pveo.getMemberimage()).placeholder(R.drawable.person).into(holder.memberImage);
//        Picasso.with(holder.cardView.getContext()).load(pveo.getMemberimage()).placeholder(R.drawable.person).into(holder.memberImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do on click stuff
//                String item = list.get(position).toString();
                Intent intent = new Intent(holder.name.getContext(), Update_Relative_Activity.class);
//                intent.putExtra("itemName", item);
                intent.putExtra("invoice", pveo);
                holder.name.getContext().startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, contact, address ;
        TextView name1, contact1, address1;
        public CardView cardView;
        ImageView memberImage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.relativename);
            contact = (TextView) itemView.findViewById(R.id.relativecontact);
            address = (TextView) itemView.findViewById(R.id.relativeaddress);
            cardView = (CardView) itemView.findViewById(R.id.card);

            name1 = (TextView) itemView.findViewById(R.id.relativename1);
            contact1 = (TextView) itemView.findViewById(R.id.relativecontact1);
            address1 = (TextView) itemView.findViewById(R.id.relativeaddress1);

            memberImage = (ImageView) itemView.findViewById(R.id.memberImage);
        }
    }

    public void reload(ArrayList<RelationVO> leedsModelArrayList) {
        list.clear();
        list.addAll(leedsModelArrayList);
        notifyDataSetChanged();
    }
}
