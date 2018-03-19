package com.perfect11.requestHandler;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.perfect11.base.MyApplication;
import com.perfect11.contest.dto.LiveScoreDto;
import com.perfect11.login_signup.ForgotPasswordActivity;
import com.perfect11.login_signup.LoginActivity;
import com.perfect11.login_signup.RegisterActivity;
import com.perfect11.login_signup.wrapper.UserDetailsWrapper;
import com.perfect11.myprofile.ChangePasswordFragment;
import com.perfect11.myprofile.MyProfileFragment;
import com.utility.Constants;
import com.utility.PreferenceUtility;
import com.webservice.RequestHandler;
import com.webservice.TaskManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ApplicationServiceRequestHandler extends RequestHandler {

    public Activity mActivity;
    public Context mContext;
    private Fragment mFragment;
    public String message, emailID, password;
    public Object[] values;
    public String[] keys;
    public int index;
    public boolean isRemember;
    private boolean isLandingScreen;
    public String url;
    public String baseURL;
    public static final int GET_USER_LOGIN_DETAIL = 1;
    public static final int FORGET_PASSWORD = 2;
    public static final int SIGN_UP = 3;
    public static final int UPCOMING_MATCHES = 4;
    public static final int CHANGE_MY_PICTURE = 5;
    public static final int CHANGE_PASSWORD = 6;
    public static final int EDIT_PROFILE = 7;
    public static final int GET_LIVE_SCORE = 8;
    public static final int SOCIAL_LOGIN = 9;

    public ApplicationServiceRequestHandler(Activity activity, Fragment fragment, String[] keys, Object[] values,
                                            String loadingMessage, int index, String baseURL) {
        super(activity);
        this.mActivity = activity;
        this.mFragment = fragment;
        this.message = loadingMessage;
        this.values = values;
        this.index = index;
        this.keys = keys;
        this.baseURL = baseURL;
        callService();
    }

    public ApplicationServiceRequestHandler(Activity activity, Fragment fragment, String[] keys, Object[] values,
                                            String loadingMessage, int index, boolean isLandingScreen, String baseURL) {
        super(activity);
        this.mActivity = activity;
        this.mFragment = fragment;
        this.message = loadingMessage;
        this.values = values;
        this.index = index;
        this.keys = keys;
        this.isLandingScreen = isLandingScreen;
        this.baseURL = baseURL;
        callService();
    }


    public ApplicationServiceRequestHandler(Activity activity, String[] keys, Object[] values, String loadingMessage, int index, String baseURL) {
        super(activity);
        this.mActivity = activity;
        this.message = loadingMessage;
        this.values = values;
        this.index = index;
        this.keys = keys;
        this.baseURL = baseURL;
        callService();
    }


    public ApplicationServiceRequestHandler(Activity activity, String[] keys, Object[] values, String loadingMessage, int index,
                                            boolean isLandingScreen) {
        super(activity);
        this.mActivity = activity;
        this.message = loadingMessage;
        this.values = values;
        this.index = index;
        this.keys = keys;
        this.isLandingScreen = isLandingScreen;
        callService();
    }

    public ApplicationServiceRequestHandler(Activity activity, String[] keys, Object[] values, String loadingMessage, int index) {
        super(activity);
        this.mActivity = activity;
        this.message = loadingMessage;
        this.values = values;
        this.index = index;
        this.keys = keys;
        callService();
    }


    public ApplicationServiceRequestHandler(Activity activity, String[] keys, Object[] values, boolean isRemember, String emailID,
                                            String password, String loadingMessage, int index) {
        super(activity);
        this.mActivity = activity;
        this.message = loadingMessage;
        this.values = values;
        this.index = index;
        this.keys = keys;
        this.isRemember = isRemember;
        this.emailID = emailID;
        this.password = password;
        callService();
    }

    //Signup
   /* public ApplicationServiceRequestHandler(Activity activity, String[] keys, Object[] values, boolean isRemember, String loadingMessage, int index) {
        super(activity);
        this.mActivity = activity;
        this.message = loadingMessage;
        this.values = values;
        this.index = index;
        this.keys = keys;
        this.isRemember = isRemember;
        callService();
    }
*/
    public ApplicationServiceRequestHandler(Activity activity, Fragment fragment, String baseURL, String url, String loadingMessage, int index) {
        super(activity);
        this.mActivity = activity;
        this.mFragment = fragment;
        this.message = loadingMessage;
        this.baseURL = baseURL;
        this.url = url;
        this.index = index;
        callServiceForGet();
    }

    private void callServiceForGet() {
        TaskManager taskManager = new TaskManager(this, this, mActivity);
        if (!isLandingScreen)
            taskManager.callServiceForGet(message, baseURL);
        else
            taskManager.callServiceForGet(message, true, baseURL);
    }

    @Override
    public String getWebServiceMethod() {

        switch (index) {
            case GET_USER_LOGIN_DETAIL:
                return "login";
            case FORGET_PASSWORD:
                return "forgot_password";
            case SIGN_UP:
                return "register";
            case UPCOMING_MATCHES:
                return url;
            case CHANGE_MY_PICTURE:
                return "changeMyPicture";
            case CHANGE_PASSWORD:
                return "update_password";
            case EDIT_PROFILE:
                return "editProfile";
            case GET_LIVE_SCORE:
                return "players/playerspoint/" + url;
            case SOCIAL_LOGIN:
                return "socialLoginApi";
        }
        return "";
    }

    @Override
    public String[] getKeys() {
        return keys;
    }

    @Override
    public Object[] getValues() {
        return values;
    }

    /**
     * Call service.
     */

    public void callService() {
        TaskManager taskManager = new TaskManager(this, this, mActivity);
        if (!isLandingScreen)
            taskManager.callService(message, false, baseURL);
        else
            taskManager.callService(message, true, baseURL);
    }

    public void callServiceForParfect11() {
        TaskManager taskManager = new TaskManager(this, this, mActivity);
        if (!isLandingScreen)
            taskManager.callService(message, false, Constants.BASE_URL1);
        else
            taskManager.callService(message, true, Constants.BASE_URL1);
    }

    public void callService(boolean isArray) {
        TaskManager taskManager = new TaskManager(this, this, mActivity);
        taskManager.callService(message, isArray, baseURL);
    }


    @Override
    public void onSuccess(String response) {
        String message = "";
        UserDetailsWrapper userWrapper;
        switch (index) {
            case GET_USER_LOGIN_DETAIL:
                userWrapper = MyApplication.gson.fromJson(response, UserDetailsWrapper.class);
                PreferenceUtility.saveObjectInAppPreference(mActivity, userWrapper.data, PreferenceUtility.APP_PREFERENCE_NAME);
                if (mActivity instanceof LoginActivity) {
                    ((LoginActivity) mActivity).serviceCallbackLogin();
                }
                break;
            case FORGET_PASSWORD:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    message = jsonObject.optString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mActivity instanceof ForgotPasswordActivity) {
                    ((ForgotPasswordActivity) mActivity).serviceCallback(message);
                }
                break;
            case SIGN_UP:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    message = jsonObject.optString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userWrapper = MyApplication.gson.fromJson(response, UserDetailsWrapper.class);
                PreferenceUtility.saveObjectInAppPreference(mActivity, userWrapper.data, PreferenceUtility.APP_PREFERENCE_NAME);
                if (mActivity instanceof RegisterActivity) {
                    ((RegisterActivity) mActivity).serviceCallbackSignUP(message);
                }
                break;
            case CHANGE_MY_PICTURE:
                userWrapper = MyApplication.gson.fromJson(response, UserDetailsWrapper.class);
                PreferenceUtility.saveObjectInAppPreference(mActivity, userWrapper.data, PreferenceUtility.APP_PREFERENCE_NAME);

                if (mFragment instanceof MyProfileFragment) {
                    ((MyProfileFragment) mFragment).serviceCallback(userWrapper.message);
                }
                break;
            case EDIT_PROFILE:
                userWrapper = MyApplication.gson.fromJson(response, UserDetailsWrapper.class);
                PreferenceUtility.saveObjectInAppPreference(mActivity, userWrapper.data, PreferenceUtility.APP_PREFERENCE_NAME);

                if (mFragment instanceof MyProfileFragment) {
                    ((MyProfileFragment) mFragment).serviceChangeProfileCallback(userWrapper.message);
                }
                break;
            case CHANGE_PASSWORD:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    message = jsonObject.optString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mFragment instanceof ChangePasswordFragment) {
                    ((ChangePasswordFragment) mFragment).serviceCallback(message);
                }
                break;
            case GET_LIVE_SCORE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    LiveScoreDto liveScoreDto;
                    ArrayList<LiveScoreDto> liveScoreDtoArrayList = new ArrayList<>();
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        liveScoreDto = MyApplication.gson.fromJson(jsonObject.optString(key), LiveScoreDto.class);
                        liveScoreDto.player_short_name = key;
                        liveScoreDtoArrayList.add(liveScoreDto);
                        Log.e("TAG", "key:" + key + "--Value::" + jsonObject.optString(key) + "live score" + liveScoreDtoArrayList.size());
                        for (LiveScoreDto liveScoreDto1 : liveScoreDtoArrayList) {
                            System.out.println("liveScoreDto1 " + liveScoreDto1.toString());

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SOCIAL_LOGIN:
                userWrapper = MyApplication.gson.fromJson(response, UserDetailsWrapper.class);
                PreferenceUtility.saveObjectInAppPreference(mActivity, userWrapper.data, PreferenceUtility.APP_PREFERENCE_NAME);
                if (mActivity instanceof LoginActivity) {
                    ((LoginActivity) mActivity).serviceCallbackLogin();
                }
                break;
        }
    }

    private boolean isIgnoreKey(String key) {
        if (key.equalsIgnoreCase("error") || key.equalsIgnoreCase("message"))
            return false;
        else
            return true;
    }

    @Override
    public void onFailure(String message, String errorCode) {
        switch (index) {
//            case GET_USER_LOGIN_DETAIL:
//                if (mActivity instanceof LoginActivity) {
//                    ((LoginActivity) mActivity).serviceCallbackLoginFail();
//                }
//                break;
            default:
                super.onFailure(message, errorCode);
        }
    }

}

