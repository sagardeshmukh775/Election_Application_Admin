package com.smartloan.smtrick.electionapp;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.FirebaseDatabase;

import static com.smartloan.smtrick.electionapp.Constants.CAT_AGRICULTURE;
import static com.smartloan.smtrick.electionapp.Constants.CAT_BIRTHDAY;
import static com.smartloan.smtrick.electionapp.Constants.CAT_BUSINESS;
import static com.smartloan.smtrick.electionapp.Constants.CAT_EDUCATIONAL;
import static com.smartloan.smtrick.electionapp.Constants.CAT_FESTIVAL;
import static com.smartloan.smtrick.electionapp.Constants.CAT_POLYTICAL;
import static com.smartloan.smtrick.electionapp.Constants.CAT_SOCIAL;


public class AppSingleton {

    private static Context context;
    private static AppSingleton appSingleton;

    public AppSingleton(Context appContext) {
        initFirebasePersistence();
    }

    private void initFirebasePersistence() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                    FirebaseDatabase.getInstance().goOnline();
                } catch (Exception e) {
                    ExceptionUtil.logException(e);
                }
            }
        });
    }

    public static AppSingleton getInstance(Context appContext) {
        //if there is no instance available... create new one
        context = appContext;
        if (appSingleton == null) {
            appSingleton = new AppSingleton(appContext);
        }
        return appSingleton;
    }

    public String[] getCategories() {
        return new String[]{"Select Category",CAT_SOCIAL,CAT_AGRICULTURE, CAT_FESTIVAL, CAT_POLYTICAL, CAT_BIRTHDAY,CAT_BUSINESS,CAT_EDUCATIONAL};
    }

    public String[] getWards() {
        return new String[]{"Select ward","1","2","3","4","5","6","7","8","9","10","11","12","13","14"};
    }
}
