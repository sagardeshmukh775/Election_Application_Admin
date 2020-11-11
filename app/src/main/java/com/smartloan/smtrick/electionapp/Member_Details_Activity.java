package com.smartloan.smtrick.electionapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.smartloan.smtrick.electionapp.Constants.CALANDER_DATE_FORMATE;


public class Member_Details_Activity extends AppCompatActivity implements View.OnClickListener, OnFragmentInteractionListener {

    private EditText etmemberward, etmembername, etmemberbirthdate, etmembereducation, etmembertemaddress, etmemberpermanentaddress,
            etmembercurrentaddress, etmembercontact, etmembercast, etmembervoteridnumber, etmemberage,spinneroccupation;
    private String Smemberward, Smembername, Smemberbirthdate, Smembereducation, Smemberoccupation, Smembertemaddress, Smemberpermanentaddress,
            Smembercurrentaddress, Smembercontact, Smembercast, Smembergender, SmemberZone, Smembervoteridnumber, Smemberrelation, Smemberid, Sleedid,
            Smemberimage, Smemberage;

    private TextView memregi, memdetails, gender,
            etmemberward1, etmembername1, etmemberbirthdate1, etmembereducation1, etmembertemaddress1, etmemberpermanentaddress1,
            etmembercurrentaddress1, etmembercontact1, etmembercast1, etmembervoteridnumber1, etmemberage1, etmemberoccupation1,
            txtGender, txtZone;
    private ImageView imgZone,imgEdit;
//    private Spinner  spinnercountrycode;

    private CheckBox CHmemberrelation;

//    private RadioGroup groupGender,groupZone;
//    private RadioButton Rmale, Rfemale, Rgender,ROrenge,RGreen,RRed,RZone;

    private List<String> listoccupation;

    int fromYear, fromMonth, fromDay;
    long fromDate, toDate;

    private DatabaseReference mDatabaseRefpatient;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    private Button AddsubData;
//    private Button AddmainData;
    private List<String> mainproductlist;
    private List<String> subproductlist;
    // MainProducts mainProducts;

    MemberVO invoice;
    LeedRepository leedRepository;

    ImageView MemberImage, Delete;
    private static final int REQUEST_PICK_IMAGE = 1002;
    String image;
    private Uri filePath;
    private String code, preImage;
    private FirebaseStorage mStorage;

    private List<String> listoccupation_Marathi;
    private List<String> listcountrycode;

    String Language;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser Fuser;
    private String uid;
    DatabaseReference databaseReference;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.toolbar)));


        databaseReference = FirebaseDatabase.getInstance().getReference();
        getCurrentuserdetails();
        code = "1";

        Intent intent = getIntent();
        invoice = (MemberVO) intent.getSerializableExtra("invoice");
        preImage = invoice.getMemberimage();
        leedRepository = new LeedRepositoryImpl();
        storageReference = FirebaseStorage.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        memregi = (TextView) findViewById(R.id.memberregistration);
        memdetails = (TextView) findViewById(R.id.memberdertails);
        gender = (TextView) findViewById(R.id.gender);

        etmemberward = (EditText) findViewById(R.id.wardnumber);
        etmembername = (EditText) findViewById(R.id.name);
        etmemberbirthdate = (EditText) findViewById(R.id.dob);
        etmembereducation = (EditText) findViewById(R.id.education);
        etmembertemaddress = (EditText) findViewById(R.id.temporaryaddress);
        etmemberpermanentaddress = (EditText) findViewById(R.id.permanentaddress);
        etmembercurrentaddress = (EditText) findViewById(R.id.currentaddress);
        etmembercontact = (EditText) findViewById(R.id.contactnumber);
        etmembercast = (EditText) findViewById(R.id.cast);
        etmembervoteridnumber = (EditText) findViewById(R.id.voteridnumber);
        etmemberage = (EditText) findViewById(R.id.memberage);

        etmemberward1 = (TextView) findViewById(R.id.wardnumber1);
        etmembername1 = (TextView) findViewById(R.id.name1);
        etmemberbirthdate1 = (TextView) findViewById(R.id.dob1);
        etmembereducation1 = (TextView) findViewById(R.id.education1);
        etmembertemaddress1 = (TextView) findViewById(R.id.temporaryaddress1);
        etmemberpermanentaddress1 = (TextView) findViewById(R.id.permanentaddress1);
        etmembercurrentaddress1 = (TextView) findViewById(R.id.currentaddress1);
        etmembercontact1 = (TextView) findViewById(R.id.contactnumber1);
        etmembercast1 = (TextView) findViewById(R.id.cast1);
        etmembervoteridnumber1 = (TextView) findViewById(R.id.voteridnumber1);
        etmemberage1 = (TextView) findViewById(R.id.memberage1);
        etmemberoccupation1 = (TextView) findViewById(R.id.occupation1);

//        AddmainData = (Button) findViewById(R.id.submit);

        spinneroccupation = (EditText) findViewById(R.id.occupation);
//        spinnercountrycode = (Spinner) findViewById(R.id.countrycode);
//        groupGender = (RadioGroup) findViewById(R.id.radioSex);
//        groupZone = (RadioGroup) findViewById(R.id.radioGroupZone);
        CHmemberrelation = (CheckBox) findViewById(R.id.relation);

        MemberImage = (ImageView) findViewById(R.id.memberimage);
        Delete = (ImageView) findViewById(R.id.deletemember);

//        Rmale = (RadioButton) findViewById(R.id.radioMale);
//        Rfemale = (RadioButton) findViewById(R.id.radioFemale);
//
//        ROrenge = (RadioButton) findViewById(R.id.radioOrenge);
//        RGreen = (RadioButton) findViewById(R.id.radioGreen);
//        RRed = (RadioButton) findViewById(R.id.radioRed);

        imgZone = (ImageView) findViewById(R.id.imgzone);
        imgEdit = (ImageView) findViewById(R.id.editemember);
        txtGender = (TextView) findViewById(R.id.gendervalue);
        txtZone = (TextView) findViewById(R.id.zonevalue);

        mDatabaseRefpatient = FirebaseDatabase.getInstance().getReference("Members");
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_PATIENTS);

        mainproductlist = new ArrayList<>();
        subproductlist = new ArrayList<>();


        listoccupation = new ArrayList<>();
        listoccupation.add("Not Applicable");
        listoccupation.add("Not Occupied");
        listoccupation.add("Professional");
        listoccupation.add("Service");
        listoccupation.add("Business");
        listoccupation.add("H.W");
        listoccupation.add("Vendor");
        listoccupation.add("Farmer");
        listoccupation.add("Student");
        listoccupation.add("Unemployed");
        listoccupation.add("Retired");
        listoccupation.add("Disability");
        listoccupation.add("Pension");
        listoccupation.add("Housemaid");
        listoccupation.add("Bara");
        listoccupation.add("Baluteder");
        listoccupation.add("Others");


        listcountrycode = new ArrayList<>();
        listcountrycode.add("+91");
        ArrayAdapter<String> country = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listcountrycode);

        country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnercountrycode.setAdapter(country);
//
//        try {
//
//            ArrayAdapter<String> occupation = new ArrayAdapter<String>(Member_Details_Activity.this,
//                    android.R.layout.simple_spinner_item, listoccupation);
//
//            occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinneroccupation.setAdapter(occupation);
//
//        } catch (Exception e) {
//        }

        listoccupation_Marathi = new ArrayList<>();
        listoccupation_Marathi.add("लागू नाही");
        listoccupation_Marathi.add("व्यापलेले नाही");
        listoccupation_Marathi.add("व्यावसायिक");
        listoccupation_Marathi.add("सेवा");
        listoccupation_Marathi.add("व्यवसाय");
        listoccupation_Marathi.add("गृहिणी");
        listoccupation_Marathi.add("विक्रेता");
        listoccupation_Marathi.add("शेतकरी");
        listoccupation_Marathi.add("विद्यार्थी");
        listoccupation_Marathi.add("बेरोजगार");
        listoccupation_Marathi.add("निवृत्त");
        listoccupation_Marathi.add("अक्षमता");
        listoccupation_Marathi.add("पेंशन");
        listoccupation_Marathi.add("घरकाम");
        listoccupation_Marathi.add("इतर");

        getdata();

        if (isNetworkAvailable()) {
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
//        groupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                Rgender = (RadioButton) findViewById(checkedId);
//            }
//        });
//        groupZone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                RZone = (RadioButton) findViewById(checkedId);
//            }
//        });

        imgZone.setOnClickListener(this);
//        setFromDateClickListner();
        MemberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();

            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Update_Member_Activity.class);

                intent.putExtra("invoice", invoice);
                startActivity(intent);

            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Member_Details_Activity.this);
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
                        Query applesQuery = ref.child("Members").orderByChild("leedid").equalTo(invoice.getLeedid());
                        try {

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {

                                        appleSnapshot.getRef().removeValue();
                                        Toast.makeText(Member_Details_Activity.this, "Delete Product Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Member_Details_Activity.this, MainActivity.class);
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
        setFromDateClickListner();

    }

    public void pickImage() {

        startActivityForResult(new Intent(this, ImagePickerActivity.class), REQUEST_PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case REQUEST_PICK_IMAGE:

                        Uri imagePath = Uri.parse(data.getStringExtra("image_path"));

                        String str = imagePath.toString();
                        String whatyouaresearching = str.substring(0, str.lastIndexOf("/"));
                        image = whatyouaresearching.substring(whatyouaresearching.lastIndexOf("/") + 1, whatyouaresearching.length());
                        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                        File file = new File(root, image);

                        filePath = Uri.fromFile(file);
                        setImage(filePath);
                        code = "2";
                        break;
                }
            }
        } else {

            System.out.println("Failed to load image");
        }

    }

    private void setImage(Uri imagePath) {

        MemberImage.setImageURI(imagePath);
    }

    private void getdata() {

        try {


            String wardnumber = invoice.getMemberward();
            String membername = invoice.getMembername();
            String dateofbirth = invoice.getMemberbirthdate();
            String education = invoice.getMembereducation();
            String occupation = invoice.getMemberoccupation();
            String tempaddress = invoice.getMembertemaddress();
            String peraddress = invoice.getMemberpermanentaddress();
            String curraddress = invoice.getMembercurrentaddress();
            String contact = invoice.getMembercontact().substring(3);
            String cast = invoice.getMembercast();
            String gender = invoice.getMembergender();
            String Zone = invoice.getMemberzone();
            String voterid = invoice.getMembervoteridnumber();
            String relation = invoice.getMemberrelation();
            String memberimage = invoice.getMemberimage();
            String memberage = invoice.getMemberage();

            Glide.with(getApplicationContext()).load(invoice.getMemberimage()).placeholder(R.drawable.loading).into(MemberImage);

            if (wardnumber != null) {
                etmemberward.setText(wardnumber);
            }
            if (membername != null) {
                etmembername.setText(membername);
            }
            if (dateofbirth != null) {
                etmemberbirthdate.setText(dateofbirth);
            }
            if (education != null) {
                etmembereducation.setText(education);
            }

            if (occupation != null) {
//                ArrayAdapter myAdap = (ArrayAdapter) spinneroccupation.getAdapter();
////                int spinnerPosition = myAdap.getPosition(occupation);
//                spinneroccupation.setSelection(myAdap.getPosition(occupation));
                spinneroccupation.setText(occupation);
            }

            if (tempaddress != null) {
                etmembertemaddress.setText(tempaddress);
            }
            if (peraddress != null) {
                etmemberpermanentaddress.setText(peraddress);
            }
            if (curraddress != null) {
                etmembercurrentaddress.setText(curraddress);
            }
            if (contact != null) {
                etmembercontact.setText(contact);
            }
            if (cast != null) {
                etmembercast.setText(cast);
            }
            if (gender != null) {
                if (gender.equalsIgnoreCase("Male")) {
//                    Rmale.setChecked(true);
                    txtGender.setText(gender);
                } else if (gender.equalsIgnoreCase("Female")) {
//                    Rfemale.setChecked(true);
                    txtGender.setText(gender);
                }
            }
            if (Zone != null) {
                if (Zone.equalsIgnoreCase("Orenge")) {
//                    ROrenge.setChecked(true);
                    txtZone.setText(Zone);
                    imgZone.setImageResource(R.drawable.orenge);
                } else if (Zone.equalsIgnoreCase("Green")) {
//                    RGreen.setChecked(true);
                    txtZone.setText(Zone);
                    imgZone.setImageResource(R.drawable.green);
                } else if (Zone.equalsIgnoreCase("Red")) {
//                    RRed.setChecked(true);
                    txtZone.setText(Zone);
                    imgZone.setImageResource(R.drawable.red);
                }
            }else {
                txtZone.setText("null");
            }
            if (voterid != null) {
                etmembervoteridnumber.setText(voterid);
            }
            if (relation != null) {
                CHmemberrelation.setChecked(true);
            }
            if (memberage != null) {
                etmemberage.setText(memberage);
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View v) {
//        if (v == AddmainData) {
//            SetData();
//        }
    }

    private void SetData() {

        Smemberward = etmemberward.getText().toString();
        Smembername = etmembername.getText().toString();
        Smemberbirthdate = etmemberbirthdate.getText().toString();
        Smembereducation = etmembereducation.getText().toString();
        Smemberoccupation = spinneroccupation.getText().toString();

        Smembertemaddress = etmembertemaddress.getText().toString();
        Smemberpermanentaddress = etmemberpermanentaddress.getText().toString();
        Smembercurrentaddress = etmembercurrentaddress.getText().toString();
//        Smembercontact = spinnercountrycode.getSelectedItem().toString() + etmembercontact.getText().toString();
        Smembercast = etmembercast.getText().toString();
        Smemberage = etmemberage.getText().toString();
//        if (groupGender.getCheckedRadioButtonId() != -1) {
//            RadioButton btn = (RadioButton) groupGender.getChildAt(groupGender.indexOfChild(groupGender.findViewById(groupGender.getCheckedRadioButtonId())));
//            Smembergender = btn.getText().toString();
//        }
//        if (groupZone.getCheckedRadioButtonId() != -1) {
//            RadioButton btn = (RadioButton) groupZone.getChildAt(groupZone.indexOfChild(groupZone.findViewById(groupZone.getCheckedRadioButtonId())));
//            SmemberZone = btn.getText().toString();
//        }

        Smembervoteridnumber = etmembervoteridnumber.getText().toString();
        if (CHmemberrelation.isChecked()) {
            Smemberrelation = CHmemberrelation.getText().toString();
        }

        if (code.equalsIgnoreCase("2")) {

            final ProgressDialog progressDialog = new ProgressDialog(Member_Details_Activity.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Smemberimage = uri.toString();
                            if (preImage != null) {
                                StorageReference imageRef = mStorage.getReferenceFromUrl(preImage);
                                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            updateData(invoice);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(Member_Details_Activity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //displaying the upload progress
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });

        } else if (code.equalsIgnoreCase("1")) {
            Smemberimage = preImage;
            updateData(invoice);
        }


    }

    private void updateData(MemberVO invoice1) {

        invoice1.setMemberward(Smemberward);
        invoice1.setMembername(Smembername);
        invoice1.setMemberbirthdate(Smemberbirthdate);
        invoice1.setMembereducation(Smembereducation);
        invoice1.setMemberoccupation(Smemberoccupation);
        invoice1.setMembertemaddress(Smembertemaddress);
        invoice1.setMemberpermanentaddress(Smemberpermanentaddress);
        invoice1.setMembercurrentaddress(Smembercurrentaddress);
        invoice1.setMembercontact(Smembercontact);
        invoice1.setMembercast(Smembercast);
        invoice1.setMembergender(Smembergender);
        invoice1.setMemberzone(SmemberZone);
        invoice1.setMembervoteridnumber(Smembervoteridnumber);
        invoice1.setMemberrelation(Smemberrelation);
        invoice1.setMemberimage(Smemberimage);
        invoice1.setMemberage(Smemberage);

        updateLeed(invoice1.getLeedid(), invoice1.getLeedStatusMap());
    }


    private void updateLeed(String leedId, Map leedsMap) {

        leedRepository.updateLeed(leedId, leedsMap, new CallBack() {
            @Override
            public void onSuccess(Object object) {

                Toast.makeText(Member_Details_Activity.this, "Member Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Member_Details_Activity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }


    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
                        } else if (Language.equalsIgnoreCase("English")) {
                            setLanguage("english");
                        }
                    }
                }

                private void setLanguage(String lang) {
                    if (lang.equalsIgnoreCase("marathi")) {
                        memregi.setText(R.string.register_regi);
                        memdetails.setText(R.string.register_memdetails);
                        etmemberward.setHint(R.string.register_wardno);
                        etmembername.setHint(R.string.register_membername);
                        etmemberbirthdate.setHint(R.string.register_dob);
                        etmembereducation.setHint(R.string.register_education);
                        etmembertemaddress.setHint(R.string.register_tempaddress);
                        etmemberpermanentaddress.setHint(R.string.register_peraddress);
                        etmembercurrentaddress.setHint(R.string.register_curaddress);
                        etmembercontact.setHint(R.string.register_contact);
                        etmembercast.setHint(R.string.register_cast);
                        etmembervoteridnumber.setHint(R.string.register_voterno);
                        etmemberage.setHint(R.string.register_memberage);
                        CHmemberrelation.setText(R.string.register_relation);
//                        AddmainData.setText(R.string.register_btnupdate);
                        gender.setText(R.string.register_gender);

//                        Rmale.setText(R.string.register_male);
//                        Rfemale.setText(R.string.register_female);

                        etmemberward1.setText(R.string.register_wardno);
                        etmembername1.setText(R.string.register_membername);
                        etmemberbirthdate1.setText(R.string.register_dob);
                        etmembereducation1.setText(R.string.register_education);
                        etmembertemaddress1.setText(R.string.register_tempaddress);
                        etmemberpermanentaddress1.setText(R.string.register_peraddress);
                        etmembercurrentaddress1.setText(R.string.register_curaddress);
                        etmembercontact1.setText(R.string.register_contact);
                        etmembercast1.setText(R.string.register_cast);
                        etmembervoteridnumber1.setText(R.string.register_voterno);
                        etmemberage1.setText(R.string.register_memberage);
                        etmemberoccupation1.setText(R.string.register_occupation);

//                        ArrayAdapter<String> occupation = new ArrayAdapter<String>(Member_Details_Activity.this,
//                                android.R.layout.simple_spinner_item, listoccupation_Marathi);
//                        occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinneroccupation.setText(occupation);
                    } else if (lang.equalsIgnoreCase("english")) {

                        memregi.setText(R.string.reg_member);
                        memdetails.setText(R.string.mem_details);
                        etmemberward.setHint(R.string.mem_ward);
                        etmembername.setHint(R.string.mem_name);
                        etmemberbirthdate.setHint(R.string.mem_dob);
                        etmembereducation.setHint(R.string.mem_education);
                        etmembertemaddress.setHint(R.string.mem_tempaddress);
                        etmemberpermanentaddress.setHint(R.string.mem_peraddress);
                        etmembercurrentaddress.setHint(R.string.mem_curaddress);
                        etmembercontact.setHint(R.string.mem_contact);
                        etmembercast.setHint(R.string.mem_cast);
                        etmembervoteridnumber.setHint(R.string.mem_voteridnumber);
                        etmemberage.setHint(R.string.mem_age);
                        CHmemberrelation.setText(R.string.mem_relative);
//                        AddmainData.setText(R.string.mem_update);
                        gender.setText(R.string.mem_gender);
//                        Rmale.setText(R.string.mem_male);
//                        Rfemale.setText(R.string.mem_female);

                        etmemberward1.setText(R.string.mem_ward);
                        etmembername1.setText(R.string.mem_name);
                        etmemberbirthdate1.setText(R.string.mem_dob);
                        etmembereducation1.setText(R.string.mem_education);
                        etmembertemaddress1.setText(R.string.mem_tempaddress);
                        etmemberpermanentaddress1.setText(R.string.mem_peraddress);
                        etmembercurrentaddress1.setText(R.string.mem_curaddress);
                        etmembercontact1.setText(R.string.mem_contact);
                        etmembercast1.setText(R.string.mem_cast);
                        etmembervoteridnumber1.setText(R.string.mem_voteridnumber);
                        etmemberage1.setText(R.string.mem_age);
                        etmemberoccupation1.setText(R.string.mem_occupation);
//                        ArrayAdapter<String> occupation = new ArrayAdapter<String>(Member_Details_Activity.this,
//                                android.R.layout.simple_spinner_item, listoccupation);
//                        occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinneroccupation.setAdapter(occupation);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(Member_Details_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
        }
    }

    private void setFromCurrentDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        fromYear = mcurrentDate.get(Calendar.YEAR);
        fromMonth = mcurrentDate.get(Calendar.MONTH);
        fromDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    private void setFromDateClickListner() {
        setFromCurrentDate();

        etmemberbirthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                DatePickerDialog mDatePicker = new DatePickerDialog(Member_Details_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat(CALANDER_DATE_FORMATE, Locale.FRANCE);
                        String formatedDate = sdf.format(myCalendar.getTime());
                        etmemberbirthdate.setText(formatedDate);
                        fromDay = selectedday;
                        fromMonth = selectedmonth;
                        fromYear = selectedyear;
                        fromDate = Utility.convertFormatedDateToMilliSeconds(formatedDate, CALANDER_DATE_FORMATE);
                    }
                }, fromYear, fromMonth, fromDay);
                mDatePicker.show();
            }
        });

    }

    @Override
    public void onFragmentInteraction(String title) {

    }

    @Override
    public void changeFragement(Fragment fragment) {

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }
    }

    @Override
    public void changeFragement(Fragment5 fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}