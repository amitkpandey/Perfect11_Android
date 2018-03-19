package com.perfect11.login_signup;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.perfect11.R;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.requestHandler.ApplicationServiceRequestHandler;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.utility.ActivityController;
import com.utility.CommonUtility;
import com.utility.Constants;
import com.utility.facebook.FacebookLoginListener;
import com.utility.facebook.FacebookUtil;
import com.utility.facebook.UserInfo;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, FacebookLoginListener, GoogleApiClient.OnConnectionFailedListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private boolean flag;
    private ContestDto contestDto;

    private ArrayList<PlayerDto> selectedTeam;
    private UpComingMatchesDto upComingMatchesDto;
    private FacebookUtil facebookUtil;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_SIGN_IN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        readFromBundle();
        initSocialLogin();
    }

    private void initSocialLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).
                addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        facebookUtil = FacebookUtil.getFBUtilInstance(this);
    }

    private void readFromBundle() {
        try {
            flag = getIntent().getExtras().getBoolean("flag");
            contestDto = (ContestDto) getIntent().getExtras().getSerializable("contestDto");
            selectedTeam = (ArrayList<PlayerDto>) getIntent().getExtras().getSerializable("selectedTeam");
            upComingMatchesDto = (UpComingMatchesDto) getIntent().getExtras().getSerializable("upComingMatchesDto");
//            Log.e("Login:", contestDto.toString() + upComingMatchesDto.toString() + selectedTeam.size() + "" + flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            String email = googleSignInAccount.getEmail();
            System.out.println(" email: " + email);
            callServiceForSocialLogin(googleSignInAccount);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
//        System.out.println("onActivityResult Request Code: " + requestCode + " Result Code: " + responseCode + " Data: " + data);
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            facebookUtil.callbackManager.onActivityResult(requestCode, responseCode, data);
        } else if (requestCode == GOOGLE_SIGN_IN) {
            System.out.println("is Google here");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        System.out.println("password Size:" + password.length());
        if (password.length() == 0) {
            mPasswordView.setError(getString(R.string.error_empty_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            /*showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
            // showProgress(true);
            callLoginAPI();
        }
    }

    private boolean isEmailValid(String email) {
        return !CommonUtility.validateEmail(email);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mEmailView.setAdapter(adapter);
    }

    private void doGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    private void doFaceBookLogin() {
        facebookUtil.isFBLogin = true;
        facebookUtil.initFacebook(this);
        facebookUtil.setFacebookLoginListener(this);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.ll_register:
                ActivityController.startNextActivity(this, RegisterActivity.class, false);
                finish();
                break;
            case R.id.ctv_forgot_password:
                ActivityController.startNextActivity(this, ForgotPasswordActivity.class, false);
                finish();
                break;
            case R.id.img_fb:
                doFaceBookLogin();
                break;
            case R.id.img_google:
                doGoogleLogin();
                break;
        }
    }


    private void callLoginAPI() {
        //API
        new ApplicationServiceRequestHandler(this, new String[]{"username", "password"},
                new Object[]{mEmailView.getText().toString().trim(), mPasswordView.getText().toString().trim()},
                "Loading...", ApplicationServiceRequestHandler.GET_USER_LOGIN_DETAIL, Constants.BASE_URL);
    }

    public void serviceCallbackLogin() {
        //showProgress(false);
        Bundle bundle = new Bundle();
        if (flag) {
            bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
            bundle.putSerializable("selectedTeam", selectedTeam);
            bundle.putSerializable("contestDto", contestDto);
        }
        bundle.putBoolean("flag", flag);

        ActivityController.startNextActivity(this, BaseHeaderActivity.class, bundle, true);
    }

    public void serviceCallbackLoginFail() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCompleted(UserInfo userInfo) {
        callServiceForSocialLogin(userInfo);
    }

    @Override
    public void onFailure() {

    }

    private void callServiceForSocialLogin(Object object) {
//        String device_token = PreferenceUtility.getStringFromPreference(this, PreferenceUtility.DEVICE_TOKEN);
        if (object instanceof UserInfo) {
            new ApplicationServiceRequestHandler(this, new String[]{"oauth_type", "oauth_uid", "first_name", "email", "device_type", "device_token",
                    "weblink"}, new Object[]{"facebook", ((UserInfo) object).getFacebookUserId(), ((UserInfo) object).getFirstName() + " " +
                    ((UserInfo) object).getLastName(), ((UserInfo) object).getEmailId(), "Android", "test", "perfect11"}, Constants.LOADING_MESSAGE,
                    ApplicationServiceRequestHandler.SOCIAL_LOGIN, Constants.BASE_URL);
        } else if (object instanceof GoogleSignInAccount) {
            new ApplicationServiceRequestHandler(this, new String[]{"oauth_type", "oauth_uid", "first_name", "email", "device_type", "device_token",
                    "weblink"}, new Object[]{"google", ((GoogleSignInAccount) object).getId(), ((GoogleSignInAccount) object).getGivenName() + " " +
                    ((GoogleSignInAccount) object).getFamilyName(), ((GoogleSignInAccount) object).getEmail(), "Android", "test", "perfect11"},
                    Constants.LOADING_MESSAGE, ApplicationServiceRequestHandler.SOCIAL_LOGIN, Constants.BASE_URL);
        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

