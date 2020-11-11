package com.smartloan.smtrick.electionapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.smartloan.smtrick.electionapp.Constants.CALANDER_DATE_FORMATE;


public class Fragment_Reports extends Fragment {
    int fromYear, fromMonth, fromDay;
    int toYear, toMonth, toDay;
    long fromDate, toDate;
    private RecyclerView catalogRecycler;
    private DatabaseReference mdataRefpatient;
    private List<MemberVO> catalogList;
    private ProgressBar catalogprogress;
    private RecyclerView.Adapter adapter;
    EditText edittextfromdate, edittexttodate;
    ReportAdapter reportadapter;
    MemberVO reportsmodel;
    ArrayList<MemberVO> leedsModelArrayList;
    Button total, Excel;
    // int[] animationList = {R.anim.layout_animation_up_to_down};
    int i = 0;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;

    DatabaseReference databaseReference;
    String Language;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser Fuser;
    private String uid;

    TextView heading;

    private OnFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Reports");
        }
        View view = inflater.inflate(R.layout.fragment_viewreports, container, false);

        // getActivity().getActionBar().setTitle("Products");
        //catalogprogress = (ProgressBar) view.findViewById(R.id.catalog_progress);
        catalogRecycler = (RecyclerView) view.findViewById(R.id.catalog_recycle);
        edittextfromdate = (EditText) view.findViewById(R.id.edittextfromdate);
        edittexttodate = (EditText) view.findViewById(R.id.edittexttodate);
        heading = (TextView) view.findViewById(R.id.texthint);

        total = (Button) view.findViewById(R.id.butonsubmit);
        Excel = (Button) view.findViewById(R.id.butonXL);
        catalogList = new ArrayList<>();
        setFromDateClickListner();
        setToDateClickListner();

        getCurrentuserdetails();

        if (isNetworkAvailable()){
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

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
                        heading.setText(R.string.rep_heading);
                        edittextfromdate.setHint(R.string.rep_fromdate);
                        edittexttodate.setHint(R.string.rep_todate);
                        Excel.setText(R.string.rep_pdf);
                        setLeeds();

                    } else if (lang.equalsIgnoreCase("english")) {
                        heading.setText(R.string.report_heading);
                        edittextfromdate.setHint(R.string.report_fromdate);
                        edittexttodate.setHint(R.string.report_todate);
                        Excel.setText(R.string.report_PDF);
                        setLeeds();
                    }
                }

                private void setLeeds() {

//                    catalogprogress.setVisibility(View.VISIBLE);
                    mdataRefpatient = FirebaseDatabase.getInstance().getReference("Members");
                    mdataRefpatient.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            catalogList.clear();
                            for (DataSnapshot mainproductSnapshot : dataSnapshot.getChildren()) {

                                MemberVO mainProducts = mainproductSnapshot.getValue(MemberVO.class);
                                catalogList.add(mainProducts);

                            }

                            total.setText(String.valueOf(catalogList.size()));

                            leedsModelArrayList = (ArrayList<MemberVO>) catalogList;
                            adapter = new ReportAdapter(getActivity(), catalogList, Language);
                            //adding adapter to recyclerview
                            catalogRecycler.setAdapter(adapter);
                            catalogRecycler.setHasFixedSize(true);
                            catalogRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
        }
    }


    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

            Document doc = new Document();
            try {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MEMBERS DATABASE/TOTAL REPORT/";

                File dir = new File(path);
                if (!dir.exists())
                    dir.mkdirs();

                Log.d("PDFCreator", "PDF Path: " + path);

                File file = new File(dir, "Member_Report" + ".pdf");
                FileOutputStream fOut = new FileOutputStream(file);

                PdfWriter.getInstance(doc, fOut);

                doc.open();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                Paragraph address = new Paragraph("details");
                Paragraph Date = new Paragraph("Date: "+formattedDate);
                /* You can also SET FONT and SIZE like this */
//            BaseFont kruti_Dev = BaseFont.createFont("c:/WINDOWS/Font/Kruti_Dev_010.ttf"
//                    ,BaseFont.CP1252,BaseFont.EMBEDDED);
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
                PdfPTable phraseTable5 = new PdfPTable(3);
                phraseTable5.setWidthPercentage(100);
                phraseTable5.setWidths(new int[]{30, 40, 30});
                phraseTable5.setHorizontalAlignment(Element.ALIGN_CENTER);

                phraseTable5.addCell("MEMBER NAME");
                phraseTable5.addCell("CONTACT");
                phraseTable5.addCell("WARD");

                phrase5.setFont(paraFont2);

                Phrase phraseTableWrapper5 = new Phrase();
                phraseTableWrapper5.add(phraseTable5);
                doc.add(phraseTableWrapper5);

                for (int i = 0; i < leedsModelArrayList.size(); i++) {

                    Phrase phrase = new Phrase();
                    PdfPCell phraseCell = new PdfPCell();
                    phraseCell.addElement(phrase);
                    PdfPTable phraseTable = new PdfPTable(3);
                    phraseTable.setWidthPercentage(100);
                    phraseTable.setWidths(new int[]{30, 40, 30});
                    phraseTable.setHorizontalAlignment(Element.ALIGN_CENTER);

                    phraseTable.addCell(leedsModelArrayList.get(i).getMembername());
                    phraseTable.addCell(leedsModelArrayList.get(i).getMembercontact());
                    phraseTable.addCell(leedsModelArrayList.get(i).getMemberward());

                    phrase.setFont(paraFont2);

                    Phrase phraseTableWrapper = new Phrase();
                    phraseTableWrapper.add(phraseTable);
                    doc.add(phraseTableWrapper);
                }

                Toast.makeText(getContext(), "PDF Generated", Toast.LENGTH_SHORT).show();

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
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MEMBERS DATABASE/TOTAL REPORT/";
        File file = new File(path, "Member_Report.pdf");
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        Intent j = Intent.createChooser(intent, "Choose an application to open with:");
        startActivity(j);
    }


    private void setFromCurrentDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        fromYear = mcurrentDate.get(Calendar.YEAR);
        fromMonth = mcurrentDate.get(Calendar.MONTH);
        fromDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    private void setFromDateClickListner() {
        setFromCurrentDate();
        edittextfromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat(CALANDER_DATE_FORMATE, Locale.FRANCE);
                        String formatedDate = sdf.format(myCalendar.getTime());
                        edittextfromdate.setText(formatedDate);
                        fromDay = selectedday;
                        fromMonth = selectedmonth;
                        fromYear = selectedyear;
                        fromDate = Utility.convertFormatedDateToMilliSeconds(formatedDate, CALANDER_DATE_FORMATE);
                        filterByDate(leedsModelArrayList);
                    }
                }, fromYear, fromMonth, fromDay);
                mDatePicker.show();
            }
        });
    }

    private void setToCurrentDate() {
        toDate = System.currentTimeMillis();
        Calendar mcurrentDate = Calendar.getInstance();
        toYear = mcurrentDate.get(Calendar.YEAR);
        toMonth = mcurrentDate.get(Calendar.MONTH);
        toDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    private void setToDateClickListner() {
        setToCurrentDate();
        edittexttodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat(CALANDER_DATE_FORMATE, Locale.FRANCE);
                        edittexttodate.setText(sdf.format(myCalendar.getTime()));
                        toDay = selectedday;
                        toMonth = selectedmonth;
                        toYear = selectedyear;
                        toDate = myCalendar.getTimeInMillis();
                        filterByDate(leedsModelArrayList);
                    }
                }, toYear, toMonth, toDay);
                mDatePicker.show();
            }
        });
    }


    private void filterByDate(ArrayList<MemberVO> leedsModelArrayList) {
        try {
            ArrayList<MemberVO> filterArrayList = new ArrayList<>();
            if (leedsModelArrayList != null) {
                if (fromDate > 0) {
                    for (MemberVO leedsModel : leedsModelArrayList) {
                        if (leedsModel.getCreatedDateTimeLong() >= fromDate && leedsModel.getCreatedDateTimeLong() <= toDate) {
                            filterArrayList.add(leedsModel);
                        }
                    }
                } else {
                    for (MemberVO leedsModel : leedsModelArrayList) {
                        if (leedsModel.getCreatedDateTimeLong() <= toDate) {
                            filterArrayList.add(leedsModel);
                        }
                    }
                }
            }
            serAdapter(filterArrayList);
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    private void serAdapter(ArrayList<MemberVO> reportmodels) {
        setReports(reportmodels);
        if (reportmodels != null) {
            if (reportadapter == null) {
                reportadapter = new ReportAdapter(getActivity(), reportmodels, Language);
                catalogRecycler.setAdapter(reportadapter);
                //onClickListner();
            } else {
                ArrayList<MemberVO> leedsModelArrayList = new ArrayList<>();
                leedsModelArrayList.addAll(reportmodels);
                reportadapter.reload(leedsModelArrayList);
                catalogRecycler.setAdapter(reportadapter);
            }
        }
    }

    private void setReports(ArrayList<MemberVO> leedsModelArrayList) {
        int approvedCount = 0, rejectedCount = 0;
        double totalPayout = 0;
        if (leedsModelArrayList != null) {
            for (MemberVO leedsModel :
                    leedsModelArrayList) {

                approvedCount++;
            }//end of for

            total.setText(String.valueOf(approvedCount));
            // fragmentReportBinding.textViewPayoutAmount.setText(String.valueOf(totalPayout));
        } else {

            total.setText("Total : 0");
            //fragmentReportBinding.textViewPayoutAmount.setText("0.0");
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
