package com.smartloan.smtrick.electionapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.smartloan.smtrick.electionapp.Constants.RELATION_PREFIX;

public class Fragment_Relation_Form extends Fragment implements View.OnClickListener {


    private String Srelationname, Srelationcontact, Srelationaddress, Srelationid, Sleedid;

    private DatabaseReference mDatabaseRefpatient;
    private DatabaseReference mDatabase;

    private Button AddsubData;
    private Button AddmainData;
    private List<String> mainproductlist;
    private List<String> subproductlist;
    // MainProducts mainProducts;

    private List<String> listoccupation;

    private EditText etrelationname, etrelationcontact, etrelationaddress;
    TextView relationform, relationdetails;

    String Language;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser Fuser;
    private String uid;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relation_form, container, false);

        etrelationname = (EditText) view.findViewById(R.id.relationmainpersonname);
        etrelationcontact = (EditText) view.findViewById(R.id.relationcontact);
        etrelationaddress = (EditText) view.findViewById(R.id.relationaddress);

        relationform = (TextView) view.findViewById(R.id.relationform);
        relationdetails = (TextView) view.findViewById(R.id.relationdetails);

        AddmainData = (Button) view.findViewById(R.id.submit);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getCurrentuserdetails();

        mDatabaseRefpatient = FirebaseDatabase.getInstance().getReference("Relations");
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_PATIENTS);

        mainproductlist = new ArrayList<>();
        subproductlist = new ArrayList<>();

        if (isNetworkAvailable()){
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        AddmainData.setOnClickListener(this);

//        setFromDateClickListner();

        return view;

    }

//    private void setFromCurrentDate() {
//        Calendar mcurrentDate = Calendar.getInstance();
//        fromYear = mcurrentDate.get(Calendar.YEAR);
//        fromMonth = mcurrentDate.get(Calendar.MONTH);
//        fromDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
//    }

//    private void setFromDateClickListner() {
//        setFromCurrentDate();
//
//        etmemberbirthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
//                        Calendar myCalendar = Calendar.getInstance();
//                        myCalendar.set(Calendar.YEAR, selectedyear);
//                        myCalendar.set(Calendar.MONTH, selectedmonth);
//                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
//                        SimpleDateFormat sdf = new SimpleDateFormat(CALANDER_DATE_FORMATE, Locale.FRANCE);
//                        String formatedDate = sdf.format(myCalendar.getTime());
//                        etmemberbirthdate.setText(formatedDate);
//                        fromDay = selectedday;
//                        fromMonth = selectedmonth;
//                        fromYear = selectedyear;
//                        fromDate = Utility.convertFormatedDateToMilliSeconds(formatedDate, CALANDER_DATE_FORMATE);
//                    }
//                }, fromYear, fromMonth, fromDay);
//                mDatePicker.show();
//            }
//        });
//
//    }

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
                            setLanguage("marathi");
                        }else if (Language.equalsIgnoreCase("Marathi")) {
                            setLanguage("english");
                        }
                    }
                }

                private void setLanguage(String lang) {
                    if (lang.equalsIgnoreCase("marathi")) {
                        etrelationname.setHint(R.string.relation_name);
                        etrelationaddress.setHint("पत्ता");
                        etrelationcontact.setHint(R.string.register_contact);
                        relationform.setText(R.string.relation_form);
                        relationdetails.setText(R.string.relation_details);
                        AddmainData.setText(R.string.register_btnsubmit);
                    }else if (lang.equalsIgnoreCase("english")){
                        etrelationname.setHint(R.string.relation_name);
                        etrelationaddress.setHint("ADDRESS");
                        etrelationcontact.setHint(R.string.rel_contact);
                        relationform.setText(R.string.rel_form);
                        relationdetails.setText(R.string.rel_details);
                        AddmainData.setText(R.string.register_btnsubmit);

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

    @Override
    public void onClick(View v) {

        try {


            if (v == AddmainData) {

                Srelationname = etrelationname.getText().toString();
                Srelationcontact = etrelationcontact.getText().toString();
                Srelationaddress = etrelationaddress.getText().toString();

                Srelationid = Utility.generateAgentId(RELATION_PREFIX);
                Sleedid = mDatabase.push().getKey();


                if (TextUtils.isEmpty(Srelationname)) {
                    Toast.makeText(getContext(), "Enter Patient Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                RelationVO memberdetails = new RelationVO(Srelationname, Srelationcontact, Srelationaddress, Srelationid, Sleedid);

                mDatabaseRefpatient.child(Sleedid).setValue(memberdetails);

                Toast.makeText(getContext(), "Details Submited", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
