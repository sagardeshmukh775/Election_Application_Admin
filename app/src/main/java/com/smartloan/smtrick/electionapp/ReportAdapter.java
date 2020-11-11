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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<MemberVO> leedsModelArrayList;
    private Context context;
    String language;

    public ReportAdapter(Context context, List<MemberVO> data, String lang) {
        this.context = context;
        this.leedsModelArrayList = data;
        this.language = lang;
    }


    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cataloglist, parent, false);
        ReportAdapter.ViewHolder viewHolder = new ReportAdapter.ViewHolder(v);
        //  context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ReportAdapter.ViewHolder holder, final int position) {
        //String pname = list.get(position);
        if (language.equalsIgnoreCase("Marathi")) {
            holder.name1.setText(R.string.card_name);
            holder.date1.setText(R.string.card_date);
            holder.cast1.setText(R.string.card_cast);
            holder.contact1.setText(R.string.card_contact);
        }

        final MemberVO pveo = leedsModelArrayList.get(position);

        holder.pname.setText(pveo.getMembername());
        holder.adate.setText(pveo.getMembercurrentaddress());
        holder.cdoctor.setText(pveo.getMembercontact());
        holder.address.setText(pveo.getMemberward());
//        Glide.with(holder.cardView.getContext()).load(pveo.getMemberimage()).placeholder(R.drawable.person).into(holder.memberImage);
        Picasso.with(holder.cardView.getContext()).load(pveo.getMemberimage()).placeholder(R.drawable.person).into(holder.memberImage);

//        try {
//            if (pveo.getCreatedDateTimeLong() > 0)
//                holder.adate.setText(Utility.convertMilliSecondsToFormatedDate(pveo.getCreatedDateTimeLong(), GLOBAL_DATE_FORMATE));
//            else
//                holder.adate.setText("Na");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do on click stuff

                Intent intent = new Intent(holder.cardView.getContext(), View_Member_Report_Activity.class);
                intent.putExtra("report", pveo);
                holder.cardView.getContext().startActivity(intent);

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

// custom dialog
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return leedsModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pname, adate, cdoctor, address;
        public CardView cardView;
        public TextView name1, date1, cast1, contact1;
        ImageView memberImage;

        public ViewHolder(View itemView) {
            super(itemView);
            pname = (TextView) itemView.findViewById(R.id.pname);
            adate = (TextView) itemView.findViewById(R.id.gendate);
            cdoctor = (TextView) itemView.findViewById(R.id.cdoctor);
            address = (TextView) itemView.findViewById(R.id.address);

            cardView = (CardView) itemView.findViewById(R.id.card);

            name1 = (TextView) itemView.findViewById(R.id.textpname);
            date1 = (TextView) itemView.findViewById(R.id.textgendate);
            cast1 = (TextView) itemView.findViewById(R.id.textcdoctor);
            contact1 = (TextView) itemView.findViewById(R.id.textaddress);

            memberImage = (ImageView) itemView.findViewById(R.id.memberImage);


        }
    }

    public void reload(ArrayList<MemberVO> list) {
        list.clear();
        list.addAll(list);
        notifyDataSetChanged();
    }
}
