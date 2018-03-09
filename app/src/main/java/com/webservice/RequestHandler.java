package com.webservice;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.utility.DialogUtility;
import com.webservice.interfaces.IRequestCaller;
import com.webservice.interfaces.IServerResponse;


// TODO: Auto-generated Javadoc

/**
 * The Class RequestHandler.
 */
public abstract class RequestHandler implements IRequestCaller, IServerResponse {

    /**
     * The m activity.
     */
    private Activity mActivity;

    /**
     * Instantiates a new request handler.
     *
     * @param activity the activity
     */
    public RequestHandler(Activity activity) {
        mActivity = activity;
    }

    /**
     * Instantiates a new request handler.
     *
     * @param activity the activity
     */
    public RequestHandler(Context activity) {
        /* The m context. */
        Context mContext = activity;
    }

    /**
     * Instantiates a new request handler.
     *
     * @param activity the activity
     */
    public RequestHandler(FragmentActivity activity) {
        mActivity = activity;
    }

    /**
     * Instantiates a new request handler.
     *
     * @param activity the activity
     * @param fragment the fragment
     */
    public RequestHandler(FragmentActivity activity, Fragment fragment) {
        mActivity = activity;
    }

    /* (non-Javadoc)
     * @see com.webservice.interfaces.IServerResponse#onFailure(java.lang.String, java.lang.String)
     */
    @Override
    public void onFailure(String message, String errorCode) {
        showMessage(message);
    }


    /**
     * Show message.
     *
     * @param message the message
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showMessage(String message) {
        if (mActivity != null && !mActivity.isDestroyed())
            DialogUtility.showMessageWithOk(message, mActivity);
    }

    /* (non-Javadoc)
     * @see com.webservice.interfaces.IServerResponse#onSuccess(java.lang.String, java.lang.String)
     */
    @Override
    public void onSuccess(String response) {

    }

}
