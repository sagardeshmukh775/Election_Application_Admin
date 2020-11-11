package com.smartloan.smtrick.electionapp;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    private Context context;
    private List<MemberVO> list;
    String Language;


    String item;


    public CatalogAdapter(Context context, List<MemberVO> list, String language) {
        this.context = context;
        this.list = list;
        this.Language = language;
    }


    @Override
    public CatalogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cataloglist, parent, false);
        CatalogAdapter.ViewHolder viewHolder = new CatalogAdapter.ViewHolder(v);
        //  context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CatalogAdapter.ViewHolder holder, final int position) {
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
                String item = list.get(position).toString();
                Intent intent = new Intent(holder.pname.getContext(), Member_Details_Activity.class);
                intent.putExtra("itemName", item);
                intent.putExtra("invoice", pveo);
                holder.pname.getContext().startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog dialog1 = new Dialog(holder.cardView.getContext());
                dialog1.setContentView(R.layout.customdialogbox5);

                ImageButton dialogEditButtonmessage = (ImageButton) dialog1.findViewById(R.id.dialogButtonmessage);
                ImageButton dialogEditButtonwhatsapp = (ImageButton) dialog1.findViewById(R.id.dialogButtonwhatsapp);
                EditText message = (EditText) dialog1.findViewById(R.id.whatsappmessage1);
                String number = pveo.getMembercontact();

                dialogEditButtonmessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sendMySMS();

                    }

                    private void sendMySMS() {
                        try {

                        String phone = pveo.getMembercontact();
                        String message1 = message.getText().toString();

                        //Check if the phoneNumber is empty
                        if (number.isEmpty() || message1.isEmpty()) {
                            Toast.makeText(holder.cardView.getContext(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
                        } else {

                            SmsManager sms = SmsManager.getDefault();
                            // if message length is too long messages are divided
                            List<String> messages = sms.divideMessage(message1);
                            for (String msg : messages) {

                                PendingIntent sentIntent = PendingIntent.getBroadcast(holder.cardView.getContext(), 0, new Intent("SMS_SENT"), 0);
                                PendingIntent deliveredIntent = PendingIntent.getBroadcast(holder.cardView.getContext(), 0, new Intent("SMS_DELIVERED"), 0);
                                sms.sendTextMessage(phone, null, msg, sentIntent, deliveredIntent);
                                Toast.makeText(holder.cardView.getContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                            }
                        }
                        }catch (Exception e){}
                    }
                });

                dialogEditButtonwhatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String number = pveo.getMembercontact();
                        if (number != null) {
                            PackageManager packageManager = holder.cardView.getContext().getPackageManager();
                            Intent i = new Intent(Intent.ACTION_VIEW);

                            try {
                                String message1 = message.getText().toString();
                                String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(message1, "UTF-8");
                                i.setPackage("com.whatsapp");
                                i.setData(Uri.parse(url));
                                if (i.resolveActivity(packageManager) != null) {
                                    holder.cardView.getContext().startActivity(i);
                                    dialog1.dismiss();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                dialog1.show();
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
