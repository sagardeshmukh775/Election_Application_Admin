package com.smartloan.smtrick.electionapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity
        implements OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {


    private NavigationView navigationView;
    private Fragment selectedFragement;
    private FirebaseAuth firebaseAuth;

    private String uid;
    private FirebaseUser Fuser;
    private DatabaseReference databaseReference;
    AppSharedPreference appSharedPreference;
    UserRepository userRepository;
    ProgressDialogClass progressDialog;

    private TextView userEmail;
    private TextView username;
    private ImageView imgProfile;
    String Language, Userid, acctemail, acctname;
    private Menu top_menu;
    Users user;
    LeedRepository leedRepository;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (isNetworkAvailable()) {
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        leedRepository = new LeedRepositoryImpl();
        appSharedPreference = new AppSharedPreference(this);
        userRepository = new UserRepositoryImpl();
        progressDialog = new ProgressDialogClass(this);
        user = new Users();
        // NOTE : Just remove the fab button
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Boolean per = isStoragePermissionGranted();
        if (per) {
            //   Toast.makeText(this, "Storage Premission Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Storage Premission Required", Toast.LENGTH_SHORT).show();
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //NOTE:  Checks first item in the navigation drawer initially
        navigationView.setCheckedItem(R.id.memberregistration);
        View headerview = navigationView.getHeaderView(0);
        username = (TextView) headerview.findViewById(R.id.textViewuserName);
        userEmail = (TextView) headerview.findViewById(R.id.textViewMobile);
        imgProfile = (ImageView) headerview.findViewById(R.id.memberImage);

        FirebaseMessaging.getInstance().subscribeToTopic("Products");

        getCurrentuserdetails();

        //NOTE:  Open fragment1 initially.
        selectedFragement = new Members_TabFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, selectedFragement);
        ft.commit();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //   Log.v(TAG,"Permission is granted");
                return true;
            } else {

                // Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    return false;
                }
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //  Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Permission Denied", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    private void getCurrentuserdetails() {

        progressDialog.showDialog(getString(R.string.loading), getString(R.string.PLEASE_WAIT));
        userRepository.readAdmin(appSharedPreference.getUserId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    user = (Users) object;

                    username.setText(user.getMobileNumber());
                    Picasso.with(MainActivity.this).load(user.getProfileImage()).placeholder(R.drawable.user1).into(imgProfile);

                    progressDialog.dismissDialog();
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        //NOTE: creating fragment object
        Fragment fragment = null;
        if (id == R.id.memberregistration) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,
//                    new Fragment_Add_Product_names()).commit();
            Intent intent = new Intent(MainActivity.this, Member_Registration_Activity.class);
            startActivity(intent);

        } else if (id == R.id.relationform) {

            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,
                    new Fragment_Relation_Form()).commit();

        } else if (id == R.id.viewmembers) {

            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,
                    new Members_TabFragment()).commit();

        } else if (id == R.id.reports) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,
                    new Fragment_Reports()).commit();

        } else if (id == R.id.addpost) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,
                    new Fragment_Add_Post()).commit();

        } else if (id == R.id.settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,
                    new Request_Doctors_Fragment()).commit();
        } else if (id == R.id.logout) {

            // clearDataWithSignOut();
            FirebaseAuth.getInstance().signOut();
            finish();
            appSharedPreference.clear();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        }

        //NOTE: Fragment changing code
        selectedFragement = fragment;
        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();

        }

        //NOTE:  Closing the drawer after selecting
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); //Ya you can also globalize this variable :P
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onFragmentInteraction(String title) {

        // NOTE:  Code to replace the toolbar title based current visible fragment
        getSupportActionBar().setTitle(title);
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
