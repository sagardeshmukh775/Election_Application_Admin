package com.smartloan.smtrick.electionapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class Update_Relative_Activity extends AppCompatActivity implements View.OnClickListener {

    private String Srelationname, Srelationcontact, Srelationaddress, Srelationid, Sleedid;

    private DatabaseReference mDatabaseRefpatient;
    private DatabaseReference mDatabase;

    private Button UpdateData;
    ImageView Delete;

    private EditText etrelationname, etrelationcontact, etrelationaddress;
    private TextView etrelationname1, etrelationcontact1, etrelationaddress1;
    TextView relationform, relationdetails;

    String Language;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser Fuser;
    private String uid;
    DatabaseReference databaseReference;
    RelationVO invoice;
    LeedRepository leedRepository;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__relative_);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.toolbar)));

        Intent intent = getIntent();
        invoice = (RelationVO) intent.getSerializableExtra("invoice");
        leedRepository = new LeedRepositoryImpl();

        etrelationname = (EditText) findViewById(R.id.relationmainpersonname);
        etrelationcontact = (EditText) findViewById(R.id.relationcontact);
        etrelationaddress = (EditText) findViewById(R.id.relationaddress);

        etrelationname1 = (TextView) findViewById(R.id.relationmainpersonname1);
        etrelationcontact1 = (TextView) findViewById(R.id.relationcontact1);
        etrelationaddress1 = (TextView) findViewById(R.id.relationaddress1);

        relationform = (TextView) findViewById(R.id.relationform);
        relationdetails = (TextView) findViewById(R.id.relationdetails);

        UpdateData = (Button) findViewById(R.id.submit);
        Delete = (ImageView) findViewById(R.id.deletemember);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getCurrentuserdetails();

        mDatabaseRefpatient = FirebaseDatabase.getInstance().getReference("Relations");
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_PATIENTS);

        getdata();
        if (isNetworkAvailable()){

        }else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        UpdateData.setOnClickListener(this);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Update_Relative_Activity.this);
                TextView title = new TextView(getApplicationContext());
                title.setText("Do you wnt to Delete");
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.RED);
                title.setTextSize(20);
                alert.setCustomTitle(title);

                alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query applesQuery = ref.child("Relations").orderByChild("leedid").equalTo(invoice.getLeedid());
                        try {

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {

                                        appleSnapshot.getRef().removeValue();
                                        Toast.makeText(Update_Relative_Activity.this, "Delete Product Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Update_Relative_Activity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, "onCancelled", databaseError.toException());
                                }
                            });

                        } catch (Exception e) {
                        }
                    }
                });

                alert.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option
                        dialog.cancel();
                    }
                });

                alert.show();

            }
        });

    }

    private void getdata() {
        String relativename = invoice.getRelationname();
        String relativecontact = invoice.getRelationcontact();
        String relativeaddress = invoice.getRelationaddress();

        if (relativename != null) {
            etrelationname.setText(relativename);
        }
        if (relativecontact != null) {
            etrelationcontact.setText(relativecontact);
        }
        if (relativeaddress != null) {
            etrelationaddress.setText(relativeaddress);
        }
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
                        etrelationname1.setText(R.string.relation_name);
                        etrelationaddress1.setText("पत्ता");
                        etrelationcontact1.setText(R.string.register_contact);
                        UpdateData.setText(R.string.register_btnupdate);
                    }else if (lang.equalsIgnoreCase("english")){
                        etrelationname.setHint(R.string.rel_name);
                        etrelationaddress.setHint("ADDRESS");
                        etrelationcontact.setHint(R.string.rel_contact);
                        relationform.setText(R.string.rel_form);
                        relationdetails.setText(R.string.rel_details);
                        etrelationname1.setText(R.string.rel_name);
                        etrelationaddress1.setText("ADDRESS");
                        etrelationcontact1.setText(R.string.rel_contact);
                        UpdateData.setText(R.string.mem_update);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {

        try {

            if (v == UpdateData) {

                Srelationname = etrelationname.getText().toString();
                Srelationcontact = etrelationcontact.getText().toString();
                Srelationaddress = etrelationaddress.getText().toString();
                Srelationid = invoice.getRelationid();

                updateData(invoice);
            }

        } catch (Exception e) {
        }


    }

    private void updateData(RelationVO invoice1) {

        invoice1.setRelationname(Srelationname);
        invoice1.setRelationcontact(Srelationcontact);
        invoice1.setRelationaddress(Srelationaddress);
        invoice1.setRelationid(Srelationid);


        updateLeed(invoice1.getLeedid(), invoice1.getLeedStatusMap());
    }

    private void updateLeed(String leedId, Map leedsMap) {

        leedRepository.updateRelative(leedId, leedsMap, new CallBack() {
            @Override
            public void onSuccess(Object object) {

                Toast.makeText(Update_Relative_Activity.this, "Relative Details Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Update_Relative_Activity.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
