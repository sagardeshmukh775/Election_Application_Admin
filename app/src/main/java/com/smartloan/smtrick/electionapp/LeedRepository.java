package com.smartloan.smtrick.electionapp;

import java.util.Map;

public interface LeedRepository {


    void deleteLeed(final String leedId, final CallBack callback);

    void updateLeed(final String leedId, final Map leedsMap, final CallBack callBack);

    void updateUser(final String userid, final Map leedsMap, final CallBack callBack);


    void readRequestUser(final String userId, final CallBack callback);

    void readActiveUser(final String userId, final CallBack callback);

    void updateRelative(final String leedId, final Map leedsMap, final CallBack callBack);

    void addPost(final PostVO postVO, final CallBack callback);

    void readMembers(final CallBack callback);
}
