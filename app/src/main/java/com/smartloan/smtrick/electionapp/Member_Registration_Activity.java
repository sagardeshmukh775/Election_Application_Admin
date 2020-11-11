package com.smartloan.smtrick.electionapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import static com.smartloan.smtrick.electionapp.Constants.AGENT_PREFIX;
import static com.smartloan.smtrick.electionapp.Constants.CALANDER_DATE_FORMATE;

public class Member_Registration_Activity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CROP_IMAGE = 2342;
    private static final int REQUEST_PICK_IMAGE = 1002;
    private List<Uri> fileDoneList;
    String image;
    private Uri filePath;

    private Spinner spinneroccupation, spinnercountrycode;

    private CheckBox CHmemberrelation;

    private String Smemberward, Smembername, Smemberbirthdate, Smembereducation, Smemberoccupation, Smembertemaddress, Smemberpermanentaddress,
            Smembercurrentaddress, Smembercontact, Scountrycode, Smembercast, Smembergender,SmemberZone, Smembervoteridnumber, Smemberrelation, Smemberid, Sleedid,
            Smemberage;
    String Sdownloadurl;

    private RadioGroup groupGender,groupZone;
    private RadioButton Rmale, Rfemale, Rgender,ROrenge,RGreen,RRed,RZone;

    private DatabaseReference mDatabaseRefpatient;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    private Button AddmainData;
    private List<String> mainproductlist;
    private List<String> subproductlist;
    // MainProducts mainProducts;

    private List<String> listoccupation;
    private List<String> listoccupation_Marathi;
    private List<String> listcountrycode;

    int fromYear, fromMonth, fromDay;
    long fromDate, toDate;
    // String mainproduct;

    TextView memregi, memdetails, gender;

    private EditText etmemberward, etmembername, etmemberbirthdate, etmembereducation, etmembertemaddress, etmemberpermanentaddress,
            etmembercurrentaddress, etmembercontact, etmembercast, etmembervoteridnumber, etmemberage;

    ImageView MemberImage;
    //    Uri image1;
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
        setContentView(R.layout.fragment_add_product_names);

        getSupportActionBar().setTitle("Election");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.toolbar)));
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getCurrentuserdetails();

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

        AddmainData = (Button) findViewById(R.id.submit);

        Rmale = (RadioButton) findViewById(R.id.radioMale);
        Rfemale = (RadioButton) findViewById(R.id.radioFemale);

        ROrenge = (RadioButton) findViewById(R.id.radioOrenge);
        RGreen = (RadioButton) findViewById(R.id.radioGreen);
        RRed = (RadioButton) findViewById(R.id.radioRed);


        spinneroccupation = (Spinner) findViewById(R.id.occupation);
        spinnercountrycode = (Spinner) findViewById(R.id.countrycode);
        groupGender = (RadioGroup) findViewById(R.id.radioSex);
        groupZone = (RadioGroup) findViewById(R.id.radioGroupZone);
        CHmemberrelation = (CheckBox) findViewById(R.id.relation);

        MemberImage = (ImageView) findViewById(R.id.memberimage);
        MemberImage.setImageDrawable(getResources().getDrawable(R.drawable.add_person));

        mDatabaseRefpatient = FirebaseDatabase.getInstance().getReference("Members");
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_PATIENTS);
        storageReference = FirebaseStorage.getInstance().getReference();

        mainproductlist = new ArrayList<>();
        subproductlist = new ArrayList<>();
        fileDoneList = new ArrayList<>();

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
        spinnercountrycode.setAdapter(country);

        try {

            ArrayAdapter<String> occupation = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listoccupation);

            occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinneroccupation.setAdapter(occupation);

        } catch (Exception e) {
        }

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
//        listoccupation_Marathi.add("Bara");
//        listoccupation_Marathi.add("Baluteder");
        listoccupation_Marathi.add("इतर");

        groupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Rgender = (RadioButton) findViewById(checkedId);
            }
        });

        groupZone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RZone = (RadioButton) findViewById(checkedId);
            }
        });

        AddmainData.setOnClickListener(this);

        setFromDateClickListner();

        MemberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();

            }
        });

        if (isNetworkAvailable()) {
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

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
                DatePickerDialog mDatePicker = new DatePickerDialog(Member_Registration_Activity.this, new DatePickerDialog.OnDateSetListener() {
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
    public void onClick(View v) {

        try {


            if (v == AddmainData) {


                final ProgressDialog progressDialog = new ProgressDialog(Member_Registration_Activity.this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                Smemberward = etmemberward.getText().toString();
                Smembername = etmembername.getText().toString();
                Smemberbirthdate = etmemberbirthdate.getText().toString();
                Smembereducation = etmembereducation.getText().toString();
                Smemberoccupation = spinneroccupation.getSelectedItem().toString();
                Smembertemaddress = etmembertemaddress.getText().toString();
                Smemberpermanentaddress = etmemberpermanentaddress.getText().toString();
                Smembercurrentaddress = etmembercurrentaddress.getText().toString();
                Smembercontact = spinnercountrycode.getSelectedItem().toString() + etmembercontact.getText().toString();
                Smembercast = etmembercast.getText().toString();
                Smembervoteridnumber = etmembervoteridnumber.getText().toString();
                Smemberage = etmemberage.getText().toString();

                if (groupGender.getCheckedRadioButtonId() != -1) {
                    RadioButton btn = (RadioButton) groupGender.getChildAt(groupGender.indexOfChild(groupGender.findViewById(groupGender.getCheckedRadioButtonId())));
                    Smembergender = btn.getText().toString();
                }
                if (groupZone.getCheckedRadioButtonId() != -1) {
                    RadioButton btn = (RadioButton) groupZone.getChildAt(groupZone.indexOfChild(groupZone.findViewById(groupZone.getCheckedRadioButtonId())));
                    SmemberZone = btn.getText().toString();
                }
                if (CHmemberrelation.isChecked()) {
                    Smemberrelation = CHmemberrelation.getText().toString();
                }

                Smemberid = Utility.generateAgentId(AGENT_PREFIX);
                Sleedid = mDatabase.push().getKey();

//                if (TextUtils.isEmpty(Smembername)) {
//                    Toast.makeText(dummyActivity.this, "Enter Patient Name", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                final StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
                sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Sdownloadurl = uri.toString();

                                MemberVO memberdetails = new MemberVO(Smemberward, Smembername, Smemberbirthdate, Smembereducation, Smemberoccupation, Smembertemaddress, Smemberpermanentaddress,
                                        Smembercurrentaddress, Smembercontact, Smembercast, Smembergender, Smembervoteridnumber, Smemberrelation, Smemberid,
                                        Sleedid, Sdownloadurl, Smemberage,SmemberZone);

                                mDatabaseRefpatient.child(Sleedid).setValue(memberdetails);

                                Toast.makeText(getApplicationContext(), "Application Submited", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(Member_Registration_Activity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
            }


        } catch (Exception e) {
        }


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
                        AddmainData.setText(R.string.register_btnsubmit);
                        gender.setText(R.string.register_gender);
                        Rmale.setText(R.string.register_male);
                        Rfemale.setText(R.string.register_female);

                        ArrayAdapter<String> occupation = new ArrayAdapter<String>(Member_Registration_Activity.this,
                                android.R.layout.simple_spinner_item, listoccupation_Marathi);
                        occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinneroccupation.setAdapter(occupation);
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
                        AddmainData.setText(R.string.mem_submit);
                        gender.setText(R.string.mem_gender);
                        Rmale.setText(R.string.mem_male);
                        Rfemale.setText(R.string.mem_female);

                        ArrayAdapter<String> occupation = new ArrayAdapter<String>(Member_Registration_Activity.this,
                                android.R.layout.simple_spinner_item, listoccupation);
                        occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinneroccupation.setAdapter(occupation);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(Member_Registration_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
