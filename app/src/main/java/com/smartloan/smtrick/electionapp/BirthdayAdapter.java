package com.smartloan.smtrick.electionapp;

import android.content.Context;
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

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.ViewHolder> {

    private Context context;
    private List<MemberVO> list;
    String Language;


    String item;


    public BirthdayAdapter(Context context, List<MemberVO> list, String language) {
        this.context = context;
        this.list = list;
        this.Language = language;
    }


    @Override
    public BirthdayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cataloglist, parent, false);
        BirthdayAdapter.ViewHolder viewHolder = new BirthdayAdapter.ViewHolder(v);
        //  context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BirthdayAdapter.ViewHolder holder, final int position) {
        //String pname = list.get(position);

        if (Language.equalsIgnoreCase("Marathi")) {
            holder.name1.setText(R.string.register_membername);
            holder.date.setText(R.string.address);
            holder.cast.setText(R.string.card_contact);
            holder.contact.setText(R.string.register_wardno);
        }
        final MemberVO pveo = list.get(position);

        holder.pname.setText(pveo.getMembername());
        holder.cdoctor.setText(pveo.getMembercontact());
        holder.address.setText(pveo.getMemberward());
        holder.adate.setText(pveo.getMembercurrentaddress());
//        Glide.with(holder.cardView.getContext()).load(pveo.getMemberimage()).placeholder(R.drawable.person).into(holder.memberImage);
        Picasso.with(holder.cardView.getContext()).load(pveo.getMemberimage()).placeholder(R.drawable.person).into(holder.memberImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do on click stuff
//                String item = list.get(position).toString();
//                Intent intent = new Intent(holder.pname.getContext(), Add_Updatelead__bankresult_Activity.class);
//                intent.putExtra("itemName", item);
//                intent.putExtra("invoice", pveo);
//                holder.pname.getContext().startActivity(intent);
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

        public TextView pname, adate, cdoctor, address;
        TextView name1, date, cast, contact;
        public CardView cardView;
        ImageView memberImage;

        public ViewHolder(View itemView) {
            super(itemView);
            pname = (TextView) itemView.findViewById(R.id.pname);
            adate = (TextView) itemView.findViewById(R.id.gendate);
            cdoctor = (TextView) itemView.findViewById(R.id.cdoctor);
            address = (TextView) itemView.findViewById(R.id.address);
            cardView = (CardView) itemView.findViewById(R.id.card);

            name1 = (TextView) itemView.findViewById(R.id.textpname);
            date = (TextView) itemView.findViewById(R.id.textgendate);
            cast = (TextView) itemView.findViewById(R.id.textcdoctor);
            contact = (TextView) itemView.findViewById(R.id.textaddress);

            memberImage = (ImageView) itemView.findViewById(R.id.memberImage);
        }
    }

    public void reload(ArrayList<MemberVO> leedsModelArrayList) {
        list.clear();
        list.addAll(leedsModelArrayList);
        notifyDataSetChanged();
    }
}
