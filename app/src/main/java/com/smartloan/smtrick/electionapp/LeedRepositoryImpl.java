package com.smartloan.smtrick.electionapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Map;

public class LeedRepositoryImpl extends FirebaseTemplateRepository implements LeedRepository {

    @Override
    public void deleteLeed(String leedId, CallBack callback) {
        DatabaseReference databaseReference = Constants.LEEDS_TABLE_REF.child(leedId);
        fireBaseDelete(databaseReference, callback);
    }

    @Override
    public void updateLeed(String leedId, Map leedsMap, final CallBack callBack) {
        final DatabaseReference databaseReference = Constants.MEMBERS_TABLE_REF.child(leedId);
        fireBaseUpdateChildren(databaseReference, leedsMap, new CallBack() {
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
    public void updateRelative(String leedId, Map leedsMap, final CallBack callBack) {
        final DatabaseReference databaseReference = Constants.RELATIVES_TABLE_REF.child(leedId);
        fireBaseUpdateChildren(databaseReference, leedsMap, new CallBack() {
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
    public void updateUser(String leedId, Map leedsMap, final CallBack callBack) {
        final DatabaseReference databaseReference = Constants.USER_TABLE_REF.child(leedId);
        fireBaseUpdateChildren(databaseReference, leedsMap, new CallBack() {
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
    public void readRequestUser(String name, final CallBack callBack) {
        final Query query = Constants.USER_TABLE_REF.orderByChild("status").equalTo(name);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<Users> leedsModelArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Users leedsModel = suggestionSnapshot.getValue(Users.class);

                            leedsModelArrayList.add(leedsModel);
                        }
                        callBack.onSuccess(leedsModelArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }


    @Override
    public void readActiveUser(String name, final CallBack callBack) {
        final Query query = Constants.USER_TABLE_REF.orderByChild("status").equalTo(name);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<Users> leedsModelArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Users leedsModel = suggestionSnapshot.getValue(Users.class);

                            leedsModelArrayList.add(leedsModel);
                        }
                        callBack.onSuccess(leedsModelArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void addPost(PostVO postVO, final CallBack callBack) {
        DatabaseReference databaseReference = Constants.POST_TABLE_REF.child(postVO.getPostId());
        fireBaseCreate(databaseReference, postVO, new CallBack() {
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
    public void readMembers(final CallBack callBack) {
        final Query query = Constants.MEMBERS_TABLE_REF;
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<MemberVO> leedsModelArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            MemberVO member = suggestionSnapshot.getValue(MemberVO.class);
                            leedsModelArrayList.add(member);
                        }
                        callBack.onSuccess(leedsModelArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

}
