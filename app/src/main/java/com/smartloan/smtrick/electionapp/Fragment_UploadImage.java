package com.smartloan.smtrick.electionapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_UploadImage extends Fragment implements View.OnClickListener {

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //view objects
    private Button buttonChoose;
    private Button buttonUpload;
    private EditText editTextName;
    private Spinner mainspinner;
    private ImageView imageView;
    private Spinner Subspinner;
    private EditText Idescription;
    private ProgressBar spinnerprogress;

    Bitmap bitmap;
    final int PIC_CROP = 1;

    //uri to store file
    private Uri filePath;
    Uri img;
    private List<String> mainproductList;
    private List<String> subproductList;
    //  String mainspinnerValue;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseMainProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imageupload, container, false);

        buttonChoose = (Button) view.findViewById(R.id.buttonChoose);
        buttonUpload = (Button) view.findViewById(R.id.buttonUpload);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        editTextName = (EditText) view.findViewById(R.id.editText);
        mainspinner = (Spinner) view.findViewById(R.id.spinner_mainProduct);
        Subspinner = (Spinner) view.findViewById(R.id.spinner_Subproduct);
        Idescription = (EditText) view.findViewById(R.id.description);
        spinnerprogress = (ProgressBar) view.findViewById(R.id.spinner_progress);
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        mainproductList = new ArrayList<>();
        subproductList = new ArrayList<>();

        spinnervalue();
        subspinnervalue();

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        return view;
    }




    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
               bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
               imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

           // CropImage.activity(filePath).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(getActivity());
        }


//        try {
//
//            if (requestCode != CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == RESULT_OK) {
//                    img = result.getUri();
//                    Bitmap bitmap = null;
//
//                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), img);
//
//                    imageView.setImageURI(img);
//
//
//                }else if (resultCode == CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
//                    Exception error = result.getError();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



        }


    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }





    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadurl = uri.toString();

                                    String mainitem = mainspinner.getSelectedItem().toString();
                                    String subnitem = Subspinner.getSelectedItem().toString();
                                    Upload upload = new Upload(mainitem, subnitem,
                                            Idescription.getText().toString().trim(), editTextName.getText().toString().trim(), downloadurl);

                                    String uploadId = mDatabase.push().getKey();
                                    mDatabase.child(uploadId).setValue(upload);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
            Toast.makeText(getContext(), "Please Select a file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonChoose) {
            showFileChooser();
        } else if (view == buttonUpload) {
            if (mainspinner.getSelectedItem().toString().trim().equals("")) {
                Toast.makeText(getContext(), "Main Product required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Subspinner.getSelectedItem().toString().trim().equals("")) {
                Toast.makeText(getContext(), "Sub Product required", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = editTextName.getText().toString().trim();
            String DESC = Idescription.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(DESC)) {
                Toast.makeText(getContext(), "Enter Description!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(imageView.getDrawable() == null){
                Toast.makeText(getContext(), "Image Required!", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadFile();
        }
    }

    public void spinnervalue() {

        mDatabaseMainProduct = FirebaseDatabase.getInstance().getReference("MainProducts");
        mDatabaseMainProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot mainproduct2Snapshot : dataSnapshot.getChildren()) {

                    MainProducts mainProducts2 = mainproduct2Snapshot.getValue(MainProducts.class);
                    mainproductList.add(mainProducts2.getMainpro());
                }
                ArrayAdapter<String> mainadapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, mainproductList);
                mainadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mainspinner.setAdapter(mainadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void subspinnervalue() {

        mainspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerprogress.setVisibility(View.VISIBLE);
                String mainspinnerValue = mainspinner.getSelectedItem().toString();

                Query query = FirebaseDatabase.getInstance().getReference("SubProducts")
                        .orderByChild("mainproduct")
                        .equalTo(mainspinnerValue);

                query.addValueEventListener(valueEventListener);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                subproductList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MainSubProducts subproducts2 = snapshot.getValue(MainSubProducts.class);
                    subproductList.add(subproducts2.getSubproduct());
                }
                // subCatalogAdapter.notifyDataSetChanged();
            }

            ArrayAdapter<String> subadapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, subproductList);
            subadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Subspinner.setAdapter(subadapter);
            spinnerprogress.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
