package com.utility.facebook;


public interface FacebookLoginListener {

    void onCompleted(UserInfo userInfo);

    void onFailure();
    // public void onAppRequestSendCompleted();
}
