package com.smartloan.smtrick.electionapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import static android.app.Activity.RESULT_OK;
import static com.smartloan.smtrick.electionapp.Constants.AGENT_PREFIX;
import static com.smartloan.smtrick.electionapp.Constants.CALANDER_DATE_FORMATE;

public class Fragment_Add_Product_names extends Fragment implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CROP_IMAGE = 2342;
    private List<Uri> fileDoneList;
    String image;
    private Uri filePath;
    private static final int REQUEST_PICK_IMAGE = 1002;

    private Spinner spinneroccupation;

    private CheckBox CHmemberrelation;

    private String Smemberward, Smembername, Smemberbirthdate, Smembereducation, Smemberoccupation, Smembertemaddress, Smemberpermanentaddress,
            Smembercurrentaddress, Smembercontact, Smembercast, Smembergender, Smembervoteridnumber, Smemberrelation, Smemberid, Sleedid,
    Smemberage;
    String Sdownloadurl;

    private RadioGroup groupGender;
    private RadioButton Rmale, Rfemale, Rgender;

    private DatabaseReference mDatabaseRefpatient;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;


    private Button AddmainData, btnchoose;
    private List<String> mainproductlist;
    private List<String> subproductlist;
    // MainProducts mainProducts;


    private List<String> listoccupation;


    int fromYear, fromMonth, fromDay;
    long fromDate, toDate;
    // String mainproduct;

    private EditText etmemberward, etmembername, etmemberbirthdate, etmembereducation, etmembertemaddress, etmemberpermanentaddress,
            etmembercurrentaddress, etmembercontact, etmembercast, etmembervoteridnumber,etmemberage;

    ImageView MemberImage;
//    Uri image1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product_names, container, false);


        etmemberward = (EditText) view.findViewById(R.id.wardnumber);
        etmembername = (EditText) view.findViewById(R.id.name);
        etmemberbirthdate = (EditText) view.findViewById(R.id.dob);
        etmembereducation = (EditText) view.findViewById(R.id.education);
        etmembertemaddress = (EditText) view.findViewById(R.id.temporaryaddress);
        etmemberpermanentaddress = (EditText) view.findViewById(R.id.permanentaddress);
        etmembercurrentaddress = (EditText) view.findViewById(R.id.currentaddress);
        etmembercontact = (EditText) view.findViewById(R.id.contactnumber);
        etmembercast = (EditText) view.findViewById(R.id.cast);
        etmembervoteridnumber = (EditText) view.findViewById(R.id.voteridnumber);

        AddmainData = (Button) view.findViewById(R.id.submit);
        btnchoose = (Button) view.findViewById(R.id.buttonChoose);

        spinneroccupation = (Spinner) view.findViewById(R.id.occupation);
        groupGender = (RadioGroup) view.findViewById(R.id.radioSex);
        CHmemberrelation = (CheckBox) view.findViewById(R.id.relation);

        MemberImage = (ImageView) view.findViewById(R.id.memberimage);

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

        try {

            ArrayAdapter<String> occupation = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, listoccupation);

            occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinneroccupation.setAdapter(occupation);

        } catch (Exception e) {
        }

        groupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Rgender = (RadioButton) view.findViewById(checkedId);
            }
        });

        AddmainData.setOnClickListener(this);

        setFromDateClickListner();

        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();

            }
        });
        MemberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        return view;

    }

    public void pickImage() {

//        startActivityForResult(new Intent(this, ImagePickerActivity.class), REQUEST_PICK_IMAGE);
        startActivityForResult(new Intent(getContext(), ImagePickerActivity.class), REQUEST_PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case REQUEST_PICK_IMAGE:

//                        String path = "file:///storage/emulated/0/Download/Image-5312.jpg";
//                        Bundle bundle = data.getExtras();
//                        String ima = bundle.getString("ima");
//                        Uri imagePath = Uri.parse(ima);
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
                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                Smembercontact = etmembercontact.getText().toString();
                Smembercast = etmembercast.getText().toString();
                Smembervoteridnumber = etmembervoteridnumber.getText().toString();

                if (groupGender.getCheckedRadioButtonId() != -1) {
                    RadioButton btn = (RadioButton) groupGender.getChildAt(groupGender.indexOfChild(groupGender.findViewById(groupGender.getCheckedRadioButtonId())));
                    Smembergender = btn.getText().toString();
                }
                if (CHmemberrelation.isChecked()) {
                    Smemberrelation = CHmemberrelation.getText().toString();
                }

                Smemberid = Utility.generateAgentId(AGENT_PREFIX);
                Sleedid = mDatabase.push().getKey();

                if (TextUtils.isEmpty(Smembername)) {
                    Toast.makeText(getContext(), "Enter Patient Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                final StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
                sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Sdownloadurl = uri.toString();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });

//                MemberVO memberdetails = new MemberVO(Smemberward, Smembername, Smemberbirthdate, Smembereducation, Smemberoccupation, Smembertemaddress, Smemberpermanentaddress,
//                        Smembercurrentaddress, Smembercontact, Smembercast, Smembergender, Smembervoteridnumber, Smemberrelation, Smemberid,
//                        Sleedid, Sdownloadurl,Smemberage);
//
//                mDatabaseRefpatient.child(Sleedid).setValue(memberdetails);

                Toast.makeText(getContext(), "Application Submited", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
        }


    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
