package com.smartloan.smtrick.electionapp;


public interface UserRepository {

    void readLoggedInUser(final CallBack callback);

    void readUser(final String userId, final CallBack callback);

    void readUserByUserId(final String regId, final CallBack callBack);

    void createUserData(final Users userModel, final CallBack callback);

    void createAdminData(final Users userModel, final CallBack callback);


}