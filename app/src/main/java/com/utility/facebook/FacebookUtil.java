package com.utility.facebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FacebookUtil {

    private static final List<String> PUBLISH_PERMISSIONS = Arrays.asList("publish_actions");
    private static final List<String> FRIEND_LIST_PERMISSIONS = Arrays.asList("user_friends");
    private static final List<String> PHOTO_PERMISSIONS = Arrays.asList("user_photos");
    private static final List<String> VIDEO_PERMISSIONS = Arrays.asList("user_videos");
    private static final List<String> EMAIL_PERMISSIONS = Arrays.asList("email");

    public boolean isFBLogin = false;

    public static boolean isFetchEvents = false;

    public static boolean isPublishFeedDialog = false;

    public boolean isGetFriends = false;

    private static List<FBFriends_Model> app_user = null;

    private FacebookFriendListener mFacebookFriendListener = null;

    private FacebookAppRequestListener appRequestListener = null;

    private static Activity mActivity;

    private FBInterface mFbInterFace;

    private IFBlogIn iFBlogin;

    private static FacebookUtil facebookUtil;

    private static FacebookActivity facebook;

    private AccessToken accessToken;

    public CallbackManager callbackManager;

    private ShareDialog shareDialog;

    private FacebookLoginListener loginList = null;

    private AccessToken.AccessTokenCreationCallback statusCallback = new SessionStatusCallback();

    private String userID;

    private FacebookUtil(Activity activity) {
        mActivity = activity;
        initFacebook(activity);
    }

    private FacebookUtil(Activity activity, FBInterface fbInterFace) {
        mActivity = activity;
        mFbInterFace = fbInterFace;
        initFacebook(activity);
    }

    private FacebookUtil(Activity activity, IFBlogIn iFBlogin) {
        mActivity = activity;
        this.iFBlogin = iFBlogin;
        initFacebook(activity);
    }

    public static FacebookUtil getFBUtilInstance(Activity activity) {
        mActivity = activity;
        return new FacebookUtil(mActivity);
    }

    public static FacebookUtil getFBUtilInstance(Activity activity, FBInterface mFbInterFace) {
        mActivity = activity;
        return new FacebookUtil(mActivity, mFbInterFace);
    }

    public static FacebookUtil getFBUtilInstance(Activity activity, IFBlogIn iFBlogIn) {
        mActivity = activity;
        return new FacebookUtil(mActivity, iFBlogIn);
    }

    public void setFacebookLoginListener(FacebookLoginListener list) {
        this.loginList = list;
    }

    public void setFacebookFriendListener(FacebookFriendListener list) {
        this.mFacebookFriendListener = list;
    }

    public void setFacebookAppRequestListener(FacebookAppRequestListener list) {
        this.appRequestListener = list;
    }

    public void initFacebook(Activity activity) {
        Log.e("initFacebook", "initFacebook called");
        FacebookSdk.sdkInitialize(activity);
        mActivity = activity;
        callbackManager = CallbackManager.Factory.create();
        publishResult();
    }

    private void publishResult() {
        if (isFBLogin) {
            getUserDetails();
        } else if (isGetFriends) {
            showFriendList();
        }
    }


    public void sendMsgToFriend() {
        shareDialog = new ShareDialog(mActivity);
        // If the Facebook app is installed and we can present the share dialog
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Hello Facebook")
                    .setImageUrl(Uri.parse("http://i.imgur.com/g3Qc1HN.png"))
                    .setContentDescription("The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();

            shareDialog.show(linkContent);
        } else {
            showAlertDialog("Please install facebook messenger app to use this feature");
        }
    }

    public void showAlertDialog(String msg) {
        new AlertDialog.Builder(mActivity)
                .setTitle("Alert")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private ProgressDialog progressBar;

    private void initializeProgressBar() {
        if (progressBar == null) {
            progressBar = new ProgressDialog(mActivity);
            progressBar.setMessage("Loading...");
            progressBar.setCancelable(false);
        }
    }

    Handler handler;

    public void doLogin() {
        LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "user_friends"));
    }

    private class SessionStatusCallback implements AccessToken.AccessTokenCreationCallback {

        /**
         * The method called on a successful creation of an AccessToken.
         *
         * @param token the access token created from the native link intent.
         */
        @Override
        public void onSuccess(AccessToken token) {
            if (token != null) {
                loginList.onFailure();
            }
        }

        @Override
        public void onError(FacebookException error) {

        }
    }

    public void getFriends() {
        System.out.println("accessToken: " + accessToken);
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            new GraphRequest(accessToken, "/me/friends", null, HttpMethod.GET, new GraphRequest.Callback() {

                /**
                 * The method that will be called when a request completes.
                 *
                 * @param response the Response of this request, which may include error information if the
                 *                 request was unsuccessful
                 */
                @Override
                public void onCompleted(GraphResponse response) {
                    isGetFriends = false;
                    if (response != null) {
                        // mFbInterFace.receiveFBFriendsList(users, response);
                        System.out.println("Friends: " + response.toString());
                    }
                }
            }).executeAsync();

//			Bundle params = new Bundle();
//			params.putString("fields", "id, name, picture, email");
//			friendRequest.setParameters(params);
//			friendRequest.executeAsync();
        } else {
            initFacebook(mActivity);
        }
    }

    public void getUserDetails() {
        Log.e("getUserDetails", "getUserDetails called");
        LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "email", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("loginResult  ", loginResult.getAccessToken().getToken());
                accessToken = loginResult.getAccessToken();
                String token = accessToken.getToken();
                System.out.println("token " + token);
//				long token_expires = facebook.getAccessExpires();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
//				prefs.edit().putLong("access_expires", token_expires).commit();
                prefs.edit().putString("access_token", token).apply();

                if (accessToken != null && !accessToken.isExpired()) {
                    System.out.println("Granted Permissions" + accessToken.getPermissions());
                    System.out.println("Declined Permissions" + accessToken.getDeclinedPermissions());
                    getBasicInformation();
                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    switch (msg.what) {
                        case 1:
                            initializeProgressBar();
                            if (!progressBar.isShowing())
                                progressBar.show();
                            break;
                        case 2:
                            if (progressBar != null && progressBar.isShowing()) {
                                progressBar.dismiss();
                                progressBar = null;

                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Publish feed dialog.
     *
     * @param caption     String
     * @param description String
     * @param captionLink String
     * @param pictureLink String
     */
    public void publishFeedDialog(String caption, String description, String captionLink, String pictureLink) {
//		Bundle params = new Bundle();
//		params.putString("name", name);
//		params.putString("caption", caption);
//		params.putString("description", description);
//		params.putString("link", "http://www.foreveryogurt.com/");
//		params.putString("picture", "http://olo.mymobilerewards.net/store_image/1389880432FYCorpOffice.png");

        accessToken = AccessToken.getCurrentAccessToken();
        ShareDialog shareDialog = new ShareDialog(mActivity);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(caption)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse(captionLink))
                    .setImageUrl(Uri.parse(pictureLink))
                    .build();

            shareDialog.show(linkContent, ShareDialog.Mode.FEED);

        }
    }

    public void inviteFriends(String message, String title) {
//		Bundle params = new Bundle();
//		params.putString("title", title);
//		params.putString("message", message);

        String AppURl = "https://fb.me/848574125218203";

        String previewImageUrl = "http://vignette3.wikia.nocookie.net/ultra/images/c/ce/0301.jpg/revision/latest/scale-to-width/400?cb=20101014042803";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(AppURl).setPreviewImageUrl(previewImageUrl)
                    .build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(mActivity);
            appInviteDialog.registerCallback(callbackManager, new FacebookCallback<AppInviteDialog.Result>() {
                @Override
                public void onSuccess(AppInviteDialog.Result result) {
                    Log.d("Invitation", "Invitation Sent Successfully");
                    mActivity.finish();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException e) {
                    Log.d("Invitation", "Error Occurred");
                }
            });

            appInviteDialog.show(content);
        }

//		WebDialog requestsDialog = new WebDialog.Builder(mActivity, "apprequests", params, new ).
//				setOnCompleteListener(new WebDialog.OnCompleteListener() {
//					@Override
//					public void onComplete(Bundle values, FacebookException error) {
//						if (appRequestListener != null) {
//							if (error != null) {
//								if (error instanceof FacebookOperationCanceledException) {
//									appRequestListener.onAppRequestSuccess(false);
//									// Toast.makeText(act, "Request cancelled", Toast.LENGTH_SHORT).show();
//								} else {
//									// Toast.makeText(act, "Network Error",
//									// Toast.LENGTH_SHORT).show();
//									appRequestListener.onAppRequestSuccess(false);
//								}
//							} else {
//								final String requestId = values.getString("request");
//								if (requestId != null) {
//									// Toast.makeText(act, "Request sent", Toast.LENGTH_SHORT).show();
//									appRequestListener.onAppRequestSuccess(true);
//								} else {
//							// Toast.makeText(act, "Request cancelled", Toast.LENGTH_SHORT).show();
//							appRequestListener.onAppRequestSuccess(false);
//						}
//					}
//				}
//			}
//		}).build();
//		requestsDialog.show();
    }

    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

//	public void shareImage(final String message,final Bitmap imageBitmap) {
//		final ProgressDialog pd = new ProgressDialog(mActivity);
//		pd.setCancelable(false);
//		pd.setMessage("Loading..");
//		pd.show();
//		Bundle params = new Bundle();
//		params.putString("message", message);
//		if (accessToken != null && !accessToken.isExpired()) {
//			GraphRequest photoUploadRequest = ShareInternalUtility.newUploadPhotoRequest("me/photos", accessToken, imageBitmap, "hello", params, new GraphRequest.Callback() {
//				@Override
//				public void onCompleted(GraphResponse response) {
//					if (pd != null && pd.isShowing()) {
//						pd.dismiss();
//					}
//					System.out.println("shareImage : " + response);
//					Toast.makeText(mActivity, "Upload successful", Toast.LENGTH_SHORT).show();
//				}
//			});
//			photoUploadRequest.executeAsync();
//		} else {
//			if (pd != null && pd.isShowing()) {
//				pd.dismiss();
//			}
//			if (!accessToken.isExpired()) {
//				// make request to the /me API
//
//				GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
//					/**
//					 * The method that will be called when the request completes.
//					 *
//					 * @param user     the GraphObject representing the returned object, or null
//					 * @param response the Response of this request, which may include error information if the
//					 */
//					@Override
//					public void onCompleted(JSONObject user, GraphResponse response) {
//						if (user != null) {
//							System.out.println("shareImage : " + response);
//							System.out.println("getPermissions : " + accessToken.getPermissions().contains("publish_stream"));
//
//							shareImage(message, imageBitmap);
//						}
//					}
//
//				});
//			}
//		}
//
//	}

    private void getBasicInformation() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String access_token = prefs.getString("access_token", null);
        handler.sendEmptyMessage(1);
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            /**
             * The method that will be called when the request completes.
             *
             * @param object   the GraphObject representing the returned object, or null
             * @param response the Response of this request, which may include error information if the
             */
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                handler.sendEmptyMessage(2);
                Log.e("-- exception is ", response.toString());
                FacebookRequestError error = response.getError();
                if (error != null) {
                    System.out.println("-- error is " + error);
                    if (loginList != null)
                        loginList.onFailure();
                } else {
                    System.out.println("-- no error" + loginList);
                    if (loginList != null) {
                        JSONObject graphResponse = response.getJSONObject();
                        System.out.println("-- user response " + graphResponse.toString());
                        String userId = graphResponse.optString("id");
                        System.out.println("-- user id : " + userId);
                        loginList.onCompleted(getUserInfo(graphResponse));
                    }
                }
            }

        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void showFriendList() {
        System.out.println("-- showing");
        handler.sendEmptyMessage(1);
        Bundle postParams = new Bundle();
        postParams.putString("fields", "id, name, picture");

        final GraphRequestBatch requestBatch = new GraphRequestBatch(new GraphRequest(accessToken, "/me/friends", postParams,
                HttpMethod.GET, new GraphRequest.Callback() {
            /**
             * The method that will be called when a request completes.
             *
             * @param response the Response of this request, which may include error information if the request was unsuccessful
             */
            @Override
            public void onCompleted(GraphResponse response) {
                FacebookRequestError error = response.getError();
                if (error != null) {
                    System.out.println("-- error is " + error);
                    if (mFacebookFriendListener != null)
                        mFacebookFriendListener.onFailure();
                } else {
                    System.out.println("-- no error" + response.getRawResponse());
                    app_user = parseFriends(response.getJSONObject());
                }
            }
        }));

        requestBatch.addCallback(new GraphRequestBatch.Callback() {
            /**
             * The method that will be called when a batch completes.
             *
             * @param batch the RequestBatch containing the Requests which were executed
             */
            @Override
            public void onBatchCompleted(GraphRequestBatch batch) {
                handler.sendEmptyMessage(2);
                if (mFacebookFriendListener != null)
                    mFacebookFriendListener.onCompleted(app_user);

            }
        });
        requestBatch.executeAsync();
    }


    private List<FBFriends_Model> parseFriends(JSONObject graphResponse) {
        List<FBFriends_Model> app_user_friends = new ArrayList<FBFriends_Model>();

        try {
            JSONArray dataArray = graphResponse.optJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject data = dataArray.optJSONObject(i);
                FBFriends_Model model = new FBFriends_Model();
                model.set_AppUser(true);
                model.setFBF_id(data.optString("id"));
                model.setFBF_name(data.optString("name"));
                model.setFBF_image_url(data.optJSONObject("picture").optJSONObject("data").optString("url"));
                app_user_friends.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return app_user_friends;
    }


    private UserInfo getUserInfo(JSONObject graphResponse) {
        UserInfo info = new UserInfo();
        try {
            info.setAccessToken(accessToken.getToken());
            info.setFacebookUserId(graphResponse.optString("id"));
            info.setFirstName(graphResponse.optString("first_name"));
            info.setLastName(graphResponse.optString("last_name"));
//			info.setProfileImageUrl(getFacebookProfilePicture(info.facebookUserId));
            info.setProfileImageUrl("http://graph.facebook.com/" + info.facebookUserId + "/picture?type=large");
            info.setSex(graphResponse.optString("gender"));
            info.setLink(graphResponse.optString("link"));
            info.setEmailId(graphResponse.optString("email"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }
}