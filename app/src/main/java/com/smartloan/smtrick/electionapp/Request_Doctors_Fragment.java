package com.smartloan.smtrick.electionapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Request_Doctors_Fragment extends Fragment {

    private LeedRepository leedRepository;
    ArrayList<Users> invoiceArrayList;
    UserAdapter invoiceAdapter;
    private RecyclerView userrecycler;
    LinearLayout Language;
    String lang;
    RadioButton gender;
    Users user;

    private FirebaseAuth firebaseAuth;

    private String uid;
    private FirebaseUser Fuser;
    private DatabaseReference databaseReference;
    String Userid, acctemail, acctname;

    RadioButton marathi, english;
    RadioGroup grouplanguage;

    public Request_Doctors_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        leedRepository = new LeedRepositoryImpl();
        invoiceArrayList = new ArrayList<>();
        user = new Users();

        grouplanguage = (RadioGroup) view.findViewById(R.id.radioSex);
        marathi = (RadioButton) view.findViewById(R.id.radioMarathi);
        english = (RadioButton) view.findViewById(R.id.radioEnglish);
        Language = (LinearLayout) view.findViewById(R.id.languagelayout);

        getCurrentuserdetails();

        if (isNetworkAvailable()){
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        grouplanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        gender = (RadioButton) view.findViewById(i);
//                        Toast.makeText(getContext(), gender.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (gender.getText().toString().equalsIgnoreCase("Marathi")) {
                            user.setName(acctname);
                            user.setEmail(acctemail);
                            user.setUserid(Userid);
                            user.setLanguage("Marathi");
                            updateData(user);
                        } else if (gender.getText().toString().equalsIgnoreCase("English")) {
                            user.setName(acctname);
                            user.setEmail(acctemail);
                            user.setUserid(Userid);
                            user.setLanguage("English");
                            updateData(user);
                        }
                    }

        });


        return view;
    }

    private void updateData(Users user1) {
        updateLeed(user1.getUserid(), user1.getLeedStatusMap());
    }

    private void updateLeed(String leedId, Map leedsMap) {

        leedRepository.updateUser(leedId, leedsMap, new CallBack() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onError(Object object) {

            }
        });
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
                        acctname = usersnapshot.child("name").getValue(String.class);
                        acctemail = usersnapshot.child("email").getValue(String.class);
                        lang = usersnapshot.child("language").getValue(String.class);
                        Userid = usersnapshot.child("userid").getValue(String.class);
//                        username.setText(acctname);
//                        userEmail.setText(acctemail);

                        setLanguage(lang);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
        }
    }

    private void setLanguage(String lan) {
        if (lan.equalsIgnoreCase("marathi")) {
            marathi.setChecked(true);
        } else if (lan.equalsIgnoreCase("english")) {
            english.setChecked(true);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
