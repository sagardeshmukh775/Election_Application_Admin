package com.smartloan.smtrick.electionapp;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.smartloan.smtrick.electionapp.Constants.AGENT_PREFIX;


public class Fragment_Add_Post extends Fragment implements View.OnClickListener {

    private EditText edtName, edtDetails;
    private Button btnAdd;
    private Spinner spinnerCategory, spinnerWardNumber;
    private ImageView imgPost;
    private static final int REQUEST_PICK_IMAGE = 1002;
    String image, Sdownloadurl, SpostName, SpostCategory, Spostdetails, SpostId;
    private Uri filePath;

    private StorageReference storageReference;
    DatabaseReference databaseReference;

    LeedRepository leedRepository;

    AppSingleton appSingleton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity__add__post, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        leedRepository = new LeedRepositoryImpl();
        appSingleton = AppSingleton.getInstance(getContext());

        edtName = view.findViewById(R.id.postname);
        edtDetails = view.findViewById(R.id.description);
        spinnerCategory = view.findViewById(R.id.category);
        spinnerWardNumber = view.findViewById(R.id.wardnumber);
        imgPost = view.findViewById(R.id.imgPost);
        btnAdd = view.findViewById(R.id.submit);

        String[] listcategory = appSingleton.getCategories();

        ArrayAdapter<String> occupation = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listcategory);
        occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(occupation);

        setWard();

        imgPost.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        return view;
    }

    private void setWard() {

        String[] listward = appSingleton.getWards();

        ArrayAdapter<String> ward = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listward);
        ward.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWardNumber.setAdapter(ward);
    }

    @Override
    public void onClick(View v) {
        if (v == imgPost) {
            pickImage();
        }
        if (v == btnAdd) {

            SpostName = edtName.getText().toString();
            Spostdetails = edtDetails.getText().toString();
            SpostCategory = spinnerCategory.getSelectedItem().toString();

            if (TextUtils.isEmpty(SpostName)) {
                Toast.makeText(getContext(), "Enter post name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(Spostdetails)) {
                Toast.makeText(getContext(), "Enter post details", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(SpostCategory)) {
                Toast.makeText(getContext(), "Select post category", Toast.LENGTH_SHORT).show();
                return;
            }
            if (SpostCategory.equalsIgnoreCase("Select Category")) {
                Toast.makeText(getContext(), "Select post category", Toast.LENGTH_SHORT).show();
                return;
            }

            UploadData();
        }
    }

    public void pickImage() {

        startActivityForResult(new Intent(getContext(), ImagePickerActivity.class), REQUEST_PICK_IMAGE);

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

        imgPost.setImageURI(imagePath);
    }

    private void UploadData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading");
        progressDialog.show();


        final StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_POSTS + System.currentTimeMillis() + "." + getFileExtension(filePath));
        sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();

                sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Sdownloadurl = uri.toString();

                        PostVO postVO = new PostVO();
                        postVO.setPostName(edtName.getText().toString());
                        postVO.setPostCategory(spinnerCategory.getSelectedItem().toString());
                        postVO.setPostDetails(edtDetails.getText().toString());
                        postVO.setPostId(Constants.POST_TABLE_REF.push().getKey());
                        postVO.setPostImage(Sdownloadurl);

                        leedRepository.addPost(postVO, new CallBack() {
                            @Override
                            public void onSuccess(Object object) {
                                if (object != null) {
                                    Toast.makeText(getContext(), "Post added Successfully", Toast.LENGTH_SHORT).show();
                                    CleareFields();
                                }
                            }

                            @Override
                            public void onError(Object object) {

                            }
                        });

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
    }

    private void CleareFields() {
        edtDetails.setText("");
        edtName.setText("");
        imgPost.setImageResource(0);
        ArrayAdapter myAdap = (ArrayAdapter) spinnerCategory.getAdapter();
//                int spinnerPosition = myAdap.getPosition(occupation);
        spinnerCategory.setSelection(myAdap.getPosition("Select Category"));
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
