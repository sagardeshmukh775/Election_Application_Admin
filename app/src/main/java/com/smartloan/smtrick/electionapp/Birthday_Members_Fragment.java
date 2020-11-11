package com.smartloan.smtrick.electionapp;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Birthday_Members_Fragment extends Fragment {


    private LeedRepository leedRepository;
    ArrayList<Users> invoiceArrayList;
    BirthdayAdapter invoiceAdapter;
    private RecyclerView userrecycler;
    DatabaseReference databaseReference;
    ArrayList<MemberVO> catalogList;
    String Language;
    String date;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser Fuser;
    private String uid;

    ImageButton Whatsapp,Message;
    String number;

    public Birthday_Members_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);


        leedRepository = new LeedRepositoryImpl();
        invoiceArrayList = new ArrayList<>();
        catalogList = new ArrayList<>();
        userrecycler = (RecyclerView) view.findViewById(R.id.userRecycler);
        userrecycler.setHasFixedSize(true);
        userrecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        getCurrentuserdetails();
        Whatsapp = (ImageButton) view.findViewById(R.id.whatsapp);
        Message = (ImageButton) view.findViewById(R.id.message);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (isNetworkAvailable()){
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        date = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date());


        Whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog1 = new Dialog(getContext());
                dialog1.setContentView(R.layout.customdialogbox4);

                Button dialogEditButton = (Button) dialog1.findViewById(R.id.dialogButtonOK);
                Button dialogEditButtoncancle = (Button) dialog1.findViewById(R.id.dialogButtonCancle);
                EditText message = (EditText) dialog1.findViewById(R.id.whatsappmessage1);



                dialogEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int j = 0; j<catalogList.size(); j++) {
                            number = catalogList.get(j).getMembercontact();
                            if (number != null) {
                                PackageManager packageManager = getActivity().getPackageManager();
                                Intent i = new Intent(Intent.ACTION_VIEW);

                                try {
                                    String message1 = message.getText().toString();
                                    String url = "https://api.whatsapp.com/send?phone="+ number +"&text=" + URLEncoder.encode(message1, "UTF-8");
                                    i.setPackage("com.whatsapp");
                                    i.setData(Uri.parse(url));
                                    if (i.resolveActivity(packageManager) != null) {
                                        getActivity().startActivity(i);
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                dialogEditButtoncancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                dialog1.show();

            }


        });

        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog1 = new Dialog(getContext());
                dialog1.setContentView(R.layout.customdialogbox4);

                Button dialogEditButton = (Button) dialog1.findViewById(R.id.dialogButtonOK);
                Button dialogEditButtoncancle = (Button) dialog1.findViewById(R.id.dialogButtonCancle);
                EditText message = (EditText) dialog1.findViewById(R.id.whatsappmessage1);

                dialogEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            for (int j = 0; j < catalogList.size(); j++) {
                                number = catalogList.get(j).getMembercontact();
                                if (number != null) {

                                    sendMySMS();
                                }
                            }
                        }catch (Exception e){}
                    }

                    private void sendMySMS() {

                        String message1 = message.getText().toString();

                        //Check if the phoneNumber is empty
                        if (number.isEmpty() || message1.isEmpty()) {
                            Toast.makeText(getContext(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
                        } else {

                            SmsManager sms = SmsManager.getDefault();
                            // if message length is too long messages are divided
                            List<String> messages = sms.divideMessage(message1);
                            for (String msg : messages) {

                                PendingIntent sentIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent("SMS_SENT"), 0);
                                PendingIntent deliveredIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent("SMS_DELIVERED"), 0);
                                sms.sendTextMessage(number, null, msg, sentIntent, deliveredIntent);
                                dialog1.dismiss();
                                Toast.makeText(getContext(), "Message Sent", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                });

                dialogEditButtoncancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                dialog1.show();



            }
        });
        return view;
    }

    private void getCurrentuserdetails() {

        try {
            firebaseAuth = FirebaseAuth.getInstance();

            Fuser = firebaseAuth.getCurrentUser();
            uid = Fuser.getUid();
            uid = Fuser.getDisplayName();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {

                        Language = usersnapshot.child("language").getValue(String.class);

                        if (Language.equalsIgnoreCase("Marathi")) {
                         setLeeds();
                        }else if (Language.equalsIgnoreCase("English")){
                            setLeeds();
                        }
                    }
                }


                private void setLeeds() {

                    getActiveDoctors();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
        }
    }

    private void getActiveDoctors() {

        databaseReference.child("Members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                catalogList.clear();
                userrecycler.removeAllViews();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    MemberVO leedsModel = snapshot.getValue(MemberVO.class);

                    String memgberdate = leedsModel.getMemberbirthdate().substring(0, leedsModel.getMemberbirthdate().length() - 3);
                    if (date.equalsIgnoreCase(memgberdate)) {
                        catalogList.add(leedsModel);
                    }


                }

                serAdapter(catalogList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void serAdapter(ArrayList<MemberVO> invoiceArrayList) {
        if (invoiceArrayList != null) {
            if (invoiceAdapter == null) {
                invoiceAdapter = new BirthdayAdapter(getActivity(), invoiceArrayList, Language);
                userrecycler.setAdapter(invoiceAdapter);
                invoiceAdapter.notifyDataSetChanged();
            } else {
                ArrayList<MemberVO> arrayList = new ArrayList<>();
                arrayList.addAll(invoiceArrayList);
//                invoiceAdapter.reload(arrayList);
                userrecycler.setAdapter(invoiceAdapter);
                invoiceAdapter.notifyDataSetChanged();

            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
