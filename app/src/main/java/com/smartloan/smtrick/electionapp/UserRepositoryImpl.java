package com.smartloan.smtrick.electionapp;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserRepositoryImpl extends FirebaseTemplateRepository implements UserRepository {
    private Activity _activity;

    public UserRepositoryImpl(final Activity activity) {
        _activity = activity;
    }

    public UserRepositoryImpl() {

    }
    /********************************************** Firebase Call ***************************************************/
    /**
     * @param mobileNumber
     * @param callBack
     */


    /**
     * @param userId
     * @param callback
     */
    @Override
    public void readUser(final String userId, final CallBack callback) {
        final Query query = Constants.USER_TABLE_REF.orderByChild("userid").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                    try {
                        final DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                        callback.onSuccess(firstChild.getValue(Users.class));
                    } catch (Exception e) {
                        callback.onError(e);
                        ExceptionUtil.logException(e);
                    }
                } else
                    callback.onError(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    @Override
    public void readLoggedInUser(final CallBack callback) {
        final FirebaseUser firebaseUser = Constants.AUTH.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        final String userId = firebaseUser.getUid();
        readUser(userId, callback);
    }

    @Override
    public void readUserByUserId(String userId, final CallBack callBack) {
        final Query query = Constants.USER_TABLE_REF.child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            if (dataSnapshot.hasChildren()) {
                                callBack.onSuccess(dataSnapshot.getValue(Users.class));
                            } else {
                                callBack.onError(null);
                            }
                        } catch (Exception e) {
                            ExceptionUtil.logException(e);
                        }
                    } else
                        callBack.onError(null);
                } else
                    callBack.onError(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError(databaseError);
            }
        });
    }

    @Override
    public void createUserData(Users user, final CallBack callBack) {
        DatabaseReference databaseReference = Constants.USER_TABLE_REF.child(user.getUserId());
        fireBaseCreate(databaseReference, user, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void createAdminData(Users user, final CallBack callBack) {
        DatabaseReference databaseReference = Constants.Admin_TABLE_REF.child(user.getUserId());
        fireBaseCreate(databaseReference, user, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }


    @Override
    public void readAdmin(String userId, final CallBack callBack) {
        final Query query = Constants.Admin_TABLE_REF.child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            if (dataSnapshot.hasChildren()) {
                                callBack.onSuccess(dataSnapshot.getValue(Users.class));
                            } else {
                                callBack.onError(null);
                            }
                        } catch (Exception e) {
                            ExceptionUtil.logException(e);
                        }
                    } else
                        callBack.onError(null);
                } else
                    callBack.onError(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError(databaseError);
            }
        });
    }


}