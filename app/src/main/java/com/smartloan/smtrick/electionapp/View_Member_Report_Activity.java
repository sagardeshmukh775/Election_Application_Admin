package com.smartloan.smtrick.electionapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class View_Member_Report_Activity extends AppCompatActivity implements View.OnClickListener {


    private Button AddmainData;

    TextView txtmemberward, txtmembername, txtmemberbirthdate, txtmembereducation, txtmemberoccupation, txtmembertemaddress,
            txtmemberpermanentaddress,
            txtmembercurrentaddress, txtmembercontact, txtmembercast, txtmembergender, txtmembervoteridnumber, txtmemberrelation,
            txtmemberage;
    private String Smemberward, Smembername, Smemberbirthdate, Smembereducation, Smemberoccupation, Smembertemaddress, Smemberpermanentaddress,
            Smembercurrentaddress, Smembercontact, Smembercast, Smembergender, Smembervoteridnumber, Smemberrelation, Smemberage;

    TextView wardnumber, name, age, dob, education, occupation, tempAddress, perAddress, curAddress, contact, cast, gender, voterid, relation,
            heading, details;

    MemberVO invoice;
    LeedRepository leedRepository;

    DatabaseReference databaseReference;
    String Language;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser Fuser;
    private String uid;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_patient_report_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        getSupportActionBar().setHomeButtonEnabled(true);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        invoice = (MemberVO) intent.getSerializableExtra("report");
        leedRepository = new LeedRepositoryImpl();

        // AddsubData = (Button)findViewById(R.id.submit);
        AddmainData = (Button) findViewById(R.id.submit);
        //spinneroccu = (Spinner)findViewById(R.id.occupation);
        txtmemberward = (TextView) findViewById(R.id.wardnumber);
        txtmembername = (TextView) findViewById(R.id.membername);
        txtmemberbirthdate = (TextView) findViewById(R.id.dateofbirth);
        txtmembereducation = (TextView) findViewById(R.id.education);
        txtmemberoccupation = (TextView) findViewById(R.id.occupation);
        txtmembertemaddress = (TextView) findViewById(R.id.tempararyaddress);
        txtmemberpermanentaddress = (TextView) findViewById(R.id.peraddress);
        txtmembercurrentaddress = (TextView) findViewById(R.id.curraddress);
        txtmembercontact = (TextView) findViewById(R.id.contact);
        txtmembercast = (TextView) findViewById(R.id.cast);
        txtmembergender = (TextView) findViewById(R.id.gender);
        txtmembervoteridnumber = (TextView) findViewById(R.id.voterid);
        txtmemberrelation = (TextView) findViewById(R.id.relation);
        txtmemberage = (TextView) findViewById(R.id.memberage);

        heading = (TextView) findViewById(R.id.reportheading);
        details = (TextView) findViewById(R.id.reportdetails);
        wardnumber = (TextView) findViewById(R.id.doctor);
        name = (TextView) findViewById(R.id.patient);
        age = (TextView) findViewById(R.id.age);
        dob = (TextView) findViewById(R.id.memberdob);
        education = (TextView) findViewById(R.id.membereducation);
        occupation = (TextView) findViewById(R.id.memberoccupation);
        tempAddress = (TextView) findViewById(R.id.tempaddress);
        perAddress = (TextView) findViewById(R.id.txtperaddress);
        curAddress = (TextView) findViewById(R.id.curaddress);
        contact = (TextView) findViewById(R.id.memcontact);
        cast = (TextView) findViewById(R.id.memcast);
        gender = (TextView) findViewById(R.id.memgender);
        voterid = (TextView) findViewById(R.id.memvoterid);
        relation = (TextView) findViewById(R.id.memrelation);

        getCurrentuserdetails();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        getdata();
        AddmainData.setOnClickListener(this);
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
//
                        Language = usersnapshot.child("language").getValue(String.class);
//
                        if (Language.equalsIgnoreCase("Marathi")) {
                            setLanguage("marathi");
                        } else if (Language.equalsIgnoreCase("English")) {
                            setLanguage("english");

                        }
                    }
                }

                private void setLanguage(String lang) {
                    if (lang.equalsIgnoreCase("marathi")) {

                        heading.setText(R.string.mem_report_details);
                        details.setText(R.string.register_memdetails);
                        wardnumber.setText(R.string.register_wardno);
                        name.setText(R.string.register_membername);
                        age.setText(R.string.register_memberage);
                        dob.setText(R.string.register_dob);
                        education.setText(R.string.register_education);
                        occupation.setText(R.string.register_occupation);
                        tempAddress.setText(R.string.register_tempaddress);
                        perAddress.setText(R.string.register_peraddress);
                        curAddress.setText(R.string.register_curaddress);
                        contact.setText(R.string.register_contact);
                        cast.setText(R.string.register_cast);
                        gender.setText(R.string.register_gender);
                        voterid.setText(R.string.register_voterno);
                        relation.setText(R.string.register_relation);
                        AddmainData.setText(R.string.generate_pdf);

                    } else if (lang.equalsIgnoreCase("english")) {
                        heading.setText(R.string.mem_report_details_english);
                        details.setText(R.string.mem_details);
                        wardnumber.setText(R.string.mem_ward);
                        name.setText(R.string.mem_name);
                        age.setText(R.string.mem_age);
                        dob.setText(R.string.mem_dob);
                        education.setText(R.string.mem_education);
                        occupation.setText(R.string.mem_occupation);
                        tempAddress.setText(R.string.mem_tempaddress);
                        perAddress.setText(R.string.mem_peraddress);
                        curAddress.setText(R.string.mem_curaddress);
                        contact.setText(R.string.mem_contact);
                        cast.setText(R.string.mem_cast);
                        gender.setText(R.string.mem_gender);
                        voterid.setText(R.string.mem_voteridnumber);
                        relation.setText(R.string.mem_relative);
                        AddmainData.setText(R.string.generate_pdf_english);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(View_Member_Report_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
        }
    }

    private void getdata() {

        Smemberward = invoice.getMemberward();
        Smembername = invoice.getMembername();
        Smemberbirthdate = invoice.getMemberbirthdate();
        Smembereducation = invoice.getMembereducation();
        Smemberoccupation = invoice.getMemberoccupation();
        Smembertemaddress = invoice.getMembertemaddress();
        Smemberpermanentaddress = invoice.getMemberpermanentaddress();
        Smembercurrentaddress = invoice.getMembercurrentaddress();
        Smembercontact = invoice.getMembercontact();
        Smembercast = invoice.getMembercast();
        Smembergender = invoice.getMembergender();
        Smembervoteridnumber = invoice.getMembervoteridnumber();
        Smemberrelation = invoice.getMemberrelation();
        Smemberage = invoice.getMemberage();

        if (Smemberward != null) {
            txtmemberward.setText(Smemberward);
        }
        if (Smembername != null) {
            txtmembername.setText(Smembername);
        }
        if (Smemberbirthdate != null) {
            txtmemberbirthdate.setText(Smemberbirthdate);
        }
        if (Smembereducation != null) {
            txtmembereducation.setText(Smembereducation);
        }
        if (Smemberoccupation != null) {
            txtmemberoccupation.setText(Smemberoccupation);
        }
        if (Smembertemaddress != null) {
            txtmembertemaddress.setText(Smembertemaddress);
        }
        if (Smemberpermanentaddress != null) {
            txtmemberpermanentaddress.setText(Smemberpermanentaddress);
        }
        if (Smembercurrentaddress != null) {
            txtmembercurrentaddress.setText(Smembercurrentaddress);
        }
        if (Smembercontact != null) {
            txtmembercontact.setText(Smembercontact);
        }
        if (Smembercast != null) {
            txtmembercast.setText(Smembercast);
        }
        if (Smembergender != null) {
            txtmembergender.setText(Smembergender);
        }
        if (Smembervoteridnumber != null) {
            txtmembervoteridnumber.setText(Smembervoteridnumber);
        }
        if (Smemberrelation != null) {
            txtmemberrelation.setText("Yes");
        }
        if (Smemberage != null) {
            txtmemberage.setText(Smemberage);
        }


    }


    @Override
    public void onClick(View v) {
        if (v == AddmainData) {
            try {
                createPdfWrapper();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(View_Member_Report_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }


                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(View_Member_Report_Activity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException {

        Document doc = new Document();
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MEMBERS DATABASE/MEMBER REPORTS/";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            File file = new File(dir, invoice.getMembername() + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            doc.open();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);

            Paragraph address = new Paragraph("details");
            Paragraph Date = new Paragraph("Date: "+formattedDate);
            /* You can also SET FONT and SIZE like this */
            Font paraFont2 = new Font(Font.FontFamily.HELVETICA);
            paraFont2.setSize(11);
            address.setAlignment(Paragraph.ALIGN_CENTER);
            address.setFont(paraFont2);
            doc.add(address);

            Paragraph blankspace = new Paragraph("\n");
            doc.add(blankspace);


            Font paraFonto = new Font(Font.FontFamily.HELVETICA);
            paraFonto.setSize(11);
            Date.setAlignment(Paragraph.ALIGN_RIGHT);
            Date.setFont(paraFonto);
            doc.add(Date);

            Paragraph blankspace0 = new Paragraph("\n");
            doc.add(blankspace0);
            doc.add(blankspace0);
            Phrase phrase5 = new Phrase();
            PdfPCell phraseCell5 = new PdfPCell();
            phraseCell5.addElement(phrase5);
            PdfPTable phraseTable5 = new PdfPTable(2);
            phraseTable5.setWidthPercentage(100);
            phraseTable5.setWidths(new int[]{50, 50});
            phraseTable5.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable5.addCell("WARD");
            phraseTable5.addCell(invoice.getMemberward());

            phrase5.setFont(paraFont2);

            Phrase phraseTableWrapper5 = new Phrase();
            phraseTableWrapper5.add(phraseTable5);
            doc.add(phraseTableWrapper5);
/////////////////////////////////////////////////////////////////////////
            Phrase phrase6 = new Phrase();
            PdfPCell phraseCell6 = new PdfPCell();
            phraseCell6.addElement(phrase6);
            PdfPTable phraseTable6 = new PdfPTable(2);
            phraseTable6.setWidthPercentage(100);
            phraseTable6.setWidths(new int[]{50, 50});
            phraseTable6.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable6.addCell("MEMBER NAME");
            phraseTable6.addCell(invoice.getMembername());

            phrase6.setFont(paraFont2);

            Phrase phraseTableWrapper6 = new Phrase();
            phraseTableWrapper6.add(phraseTable6);
            doc.add(phraseTableWrapper6);
/////////////////////////////////////////////////////////////////////////
            Phrase phrase7 = new Phrase();
            PdfPCell phraseCell7 = new PdfPCell();
            phraseCell7.addElement(phrase7);
            PdfPTable phraseTable7 = new PdfPTable(2);
            phraseTable7.setWidthPercentage(100);
            phraseTable7.setWidths(new int[]{50, 50});
            phraseTable7.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable7.addCell("AGE");
            phraseTable7.addCell(invoice.getMemberage());

            phrase7.setFont(paraFont2);

            Phrase phraseTableWrapper7 = new Phrase();
            phraseTableWrapper7.add(phraseTable7);
            doc.add(phraseTableWrapper7);
/////////////////////////////////////////////////////////////////////////
            Phrase phrase8 = new Phrase();
            PdfPCell phraseCell8 = new PdfPCell();
            phraseCell8.addElement(phrase8);
            PdfPTable phraseTable8 = new PdfPTable(2);
            phraseTable8.setWidthPercentage(100);
            phraseTable8.setWidths(new int[]{50, 50});
            phraseTable8.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable8.addCell("D.O.B.");
            phraseTable8.addCell(invoice.getMemberbirthdate());

            phrase8.setFont(paraFont2);

            Phrase phraseTableWrapper8 = new Phrase();
            phraseTableWrapper8.add(phraseTable8);
            doc.add(phraseTableWrapper8);
/////////////////////////////////////////////////////////////////////////
            Phrase phrase9 = new Phrase();
            PdfPCell phraseCell9 = new PdfPCell();
            phraseCell9.addElement(phrase9);
            PdfPTable phraseTable9 = new PdfPTable(2);
            phraseTable9.setWidthPercentage(100);
            phraseTable9.setWidths(new int[]{50, 50});
            phraseTable9.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable9.addCell("EDUCATION");
            phraseTable9.addCell(invoice.getMembereducation());

            phrase9.setFont(paraFont2);

            Phrase phraseTableWrapper9 = new Phrase();
            phraseTableWrapper9.add(phraseTable9);
            doc.add(phraseTableWrapper9);
/////////////////////////////////////////////////////////////////////////
            Phrase phrase1 = new Phrase();
            PdfPCell phraseCell1 = new PdfPCell();
            phraseCell1.addElement(phrase1);
            PdfPTable phraseTable1 = new PdfPTable(2);
            phraseTable1.setWidthPercentage(100);
            phraseTable1.setWidths(new int[]{50, 50});
            phraseTable1.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable1.addCell("OCCUPATION");
            phraseTable1.addCell(invoice.getMemberoccupation());

            phrase1.setFont(paraFont2);

            Phrase phraseTableWrapper1 = new Phrase();
            phraseTableWrapper1.add(phraseTable1);
            doc.add(phraseTableWrapper1);
/////////////////////////////////////////////////////////////////////////
            Phrase phrase2 = new Phrase();
            PdfPCell phraseCell2 = new PdfPCell();
            phraseCell2.addElement(phrase2);
            PdfPTable phraseTable2 = new PdfPTable(2);
            phraseTable2.setWidthPercentage(100);
            phraseTable2.setWidths(new int[]{50, 50});
            phraseTable2.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable2.addCell("TEMPORARY ADDRESS");
            phraseTable2.addCell(invoice.getMembertemaddress());

            phrase2.setFont(paraFont2);

            Phrase phraseTableWrapper2 = new Phrase();
            phraseTableWrapper2.add(phraseTable2);
            doc.add(phraseTableWrapper2);
/////////////////////////////////////////////////////////////////////////
            Phrase phrase3 = new Phrase();
            PdfPCell phraseCell3 = new PdfPCell();
            phraseCell3.addElement(phrase3);
            PdfPTable phraseTable3 = new PdfPTable(2);
            phraseTable3.setWidthPercentage(100);
            phraseTable3.setWidths(new int[]{50, 50});
            phraseTable3.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable3.addCell("PERMANENT ADDRESS");
            phraseTable3.addCell(invoice.getMemberpermanentaddress());

            phrase3.setFont(paraFont2);

            Phrase phraseTableWrapper3 = new Phrase();
            phraseTableWrapper3.add(phraseTable3);
            doc.add(phraseTableWrapper3);
//////////////////////////////////////////////////////////////////////////////////
            Phrase phrase4 = new Phrase();
            PdfPCell phraseCell4 = new PdfPCell();
            phraseCell4.addElement(phrase4);
            PdfPTable phraseTable4 = new PdfPTable(2);
            phraseTable4.setWidthPercentage(100);
            phraseTable4.setWidths(new int[]{50, 50});
            phraseTable4.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable4.addCell("CURRENT ADDRESS");
            phraseTable4.addCell(invoice.getMembercurrentaddress());

            phrase4.setFont(paraFont2);

            Phrase phraseTableWrapper4 = new Phrase();
            phraseTableWrapper4.add(phraseTable4);
            doc.add(phraseTableWrapper4);
//////////////////////////////////////////////////////////////////////////////////
            Phrase phrase10 = new Phrase();
            PdfPCell phraseCell10 = new PdfPCell();
            phraseCell10.addElement(phrase10);
            PdfPTable phraseTable10 = new PdfPTable(2);
            phraseTable10.setWidthPercentage(100);
            phraseTable10.setWidths(new int[]{50, 50});
            phraseTable10.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable10.addCell("CONTACT NUMBER");
            phraseTable10.addCell(invoice.getMembercontact());

            phrase10.setFont(paraFont2);

            Phrase phraseTableWrapper10 = new Phrase();
            phraseTableWrapper10.add(phraseTable10);
            doc.add(phraseTableWrapper10);
//////////////////////////////////////////////////////////////////////////////////
            Phrase phrase11 = new Phrase();
            PdfPCell phraseCell11 = new PdfPCell();
            phraseCell11.addElement(phrase11);
            PdfPTable phraseTable11 = new PdfPTable(2);
            phraseTable11.setWidthPercentage(100);
            phraseTable11.setWidths(new int[]{50, 50});
            phraseTable11.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable11.addCell("CAST");
            phraseTable11.addCell(invoice.getMembercast());

            phrase11.setFont(paraFont2);

            Phrase phraseTableWrapper11 = new Phrase();
            phraseTableWrapper11.add(phraseTable11);
            doc.add(phraseTableWrapper11);
//////////////////////////////////////////////////////////////////////////////////
            Phrase phrase12 = new Phrase();
            PdfPCell phraseCell12 = new PdfPCell();
            phraseCell12.addElement(phrase12);
            PdfPTable phraseTable12 = new PdfPTable(2);
            phraseTable12.setWidthPercentage(100);
            phraseTable12.setWidths(new int[]{50, 50});
            phraseTable12.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable12.addCell("GENDER");
            phraseTable12.addCell(invoice.getMembergender());

            phrase4.setFont(paraFont2);

            Phrase phraseTableWrapper12 = new Phrase();
            phraseTableWrapper12.add(phraseTable12);
            doc.add(phraseTableWrapper12);
//////////////////////////////////////////////////////////////////////////////////
            Phrase phrase13 = new Phrase();
            PdfPCell phraseCell13 = new PdfPCell();
            phraseCell13.addElement(phrase13);
            PdfPTable phraseTable13 = new PdfPTable(2);
            phraseTable13.setWidthPercentage(100);
            phraseTable13.setWidths(new int[]{50, 50});
            phraseTable13.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable13.addCell("VOTER ID NUMBER");
            phraseTable13.addCell(invoice.getMemberbirthdate());

            phrase13.setFont(paraFont2);

            Phrase phraseTableWrapper13 = new Phrase();
            phraseTableWrapper13.add(phraseTable13);
            doc.add(phraseTableWrapper13);
//////////////////////////////////////////////////////////////////////////////////
            Phrase phrase = new Phrase();
            PdfPCell phraseCell = new PdfPCell();
            phraseCell4.addElement(phrase);
            PdfPTable phraseTable = new PdfPTable(2);
            phraseTable.setWidthPercentage(100);
            phraseTable.setWidths(new int[]{50, 50});
            phraseTable.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable.addCell("GENDER");
            if (invoice.getMemberbirthdate() != null) {
                phraseTable.addCell("Yes");
            }else {
                phraseTable.addCell("No");
            }

            phrase4.setFont(paraFont2);

            Phrase phraseTableWrapper = new Phrase();
            phraseTableWrapper.add(phraseTable);
            doc.add(phraseTableWrapper);
//////////////////////////////////////////////////////////////////////////////////
            Toast.makeText(View_Member_Report_Activity.this, "PDF Generated", Toast.LENGTH_SHORT).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        openPdf1();
    }


    void openPdf1() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MEMBERS DATABASE/MEMBER REPORTS/";
        File file = new File(path, invoice.getMembername() + ".pdf");
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        Intent j = Intent.createChooser(intent, "Choose an application to open with:");
        startActivity(j);
    }
}