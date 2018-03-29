package com.perfect11.base;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.perfect11.R;
import com.perfect11.account.MyAccountFragment;
import com.perfect11.contest.InviteCodeFragment;
import com.perfect11.contest.MyContestFragmentNotUses;
import com.perfect11.help.HelpFragment;
import com.perfect11.home.HomeFragment;
import com.perfect11.login_signup.IntroScreen;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.myprofile.MyContestFragment;
import com.perfect11.myprofile.MyProfileFragment;
import com.perfect11.myprofile.TicketSystemFragment;
import com.perfect11.othersMenuItem.InviteFriendsFragment;
import com.perfect11.point_system.PointSystemFragment;
import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.special.ResideMenu.ResideMenu;
import com.squareup.picasso.Picasso;
import com.utility.ActivityController;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseHeaderActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {// .base.BaseHeaderActivity

    public ResideMenu slideMenu;

    private FragmentManager mFragmentManager;

    public FragmentTransaction mFragmentTransaction;

    private CircleImageView profileImage;

    private UserDto userDto;

    private CustomTextView tv_name, ctv_email;

    private GoogleApiClient mGoogleApiClient;

    //Menu Items
    private CustomTextView ctv_ticket_system,ctv_home, ctv_profile, ctv_account, ctv_my_contests, ctv_leader_board, ctv_invite_friends, ctv_point_system, ctv_help, ctv_contest_invited_code, ctv_logout;

    public String activityName = "";

    private static boolean isSlidingMenuOPen = false;

    public static boolean isOpenSlide = true;

    protected Fragment mFragment;

    private HomeFragment mHomeScreen;

    private boolean flag;
    private ContestDto contestDto;

    private ArrayList<PlayerDto> selectedTeam;
    private UpComingMatchesDto upComingMatchesDto;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(this, PreferenceUtility.APP_PREFERENCE_NAME);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        setContentView(R.layout.common_base_header);
        readFromBundle();

        if (bundle == null) {
            Bundle bundle1 = new Bundle();
            if (flag) {
                bundle1.putSerializable("upComingMatchesDto", upComingMatchesDto);
                bundle1.putSerializable("selectedTeam", selectedTeam);
                bundle1.putSerializable("contestDto", contestDto);
            }
            bundle1.putBoolean("flag", flag);
            mHomeScreen = new HomeFragment();
            mHomeScreen.setArguments(bundle1);
            mFragmentTransaction.replace(R.id.fl_container, mHomeScreen);
            mFragmentTransaction.commit();
        } else {
            mHomeScreen = (HomeFragment) this.getSupportFragmentManager().findFragmentById(R.id.fl_container);
        }
        setValueOnSideMenu();
        readFromBundle();
    }

    private void readFromBundle() {
        try {
            flag = getIntent().getExtras().getBoolean("flag");
            contestDto = (ContestDto) getIntent().getExtras().getSerializable("contestDto");
            selectedTeam = (ArrayList<PlayerDto>) getIntent().getExtras().getSerializable("selectedTeam");
            upComingMatchesDto = (UpComingMatchesDto) getIntent().getExtras().getSerializable("upComingMatchesDto");
            System.out.println(contestDto.toString());
//            Log.e("BaseHeaderActivity:", contestDto.toString() + upComingMatchesDto.toString() + selectedTeam.size() + "" + flag);
        } catch (Exception e) {
        }


    }

    private void initView() {
        View leftMenu = slideMenu.getLeftMenuView();
        Log.i("Init View:", "Side Menu Content");
        ctv_home = leftMenu.findViewById(R.id.ctv_home);
        ctv_profile = leftMenu.findViewById(R.id.ctv_profile);
        ctv_account = leftMenu.findViewById(R.id.ctv_account);
        ctv_my_contests = leftMenu.findViewById(R.id.ctv_my_contests);
        ctv_leader_board = leftMenu.findViewById(R.id.ctv_leader_board);
        ctv_invite_friends = leftMenu.findViewById(R.id.ctv_invite_friends);
        ctv_point_system = leftMenu.findViewById(R.id.ctv_point_system);
        ctv_help = leftMenu.findViewById(R.id.ctv_help);
        ctv_contest_invited_code = leftMenu.findViewById(R.id.ctv_contest_invited_code);
        ctv_logout = leftMenu.findViewById(R.id.ctv_logout);
        ctv_ticket_system=leftMenu.findViewById(R.id.ctv_ticket_system);
    }

    private void setDefaultBG() {
        ctv_home.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_profile.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_account.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_my_contests.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_leader_board.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_invite_friends.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_point_system.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_help.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_contest_invited_code.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_logout.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
        ctv_ticket_system.setBackground(getResources().getDrawable(R.drawable.shaddo_black));
    }


    private void setValueOnSideMenu() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(this, PreferenceUtility.APP_PREFERENCE_NAME);
        tv_name.setText(userDto.first_name + " " + userDto.last_name);

        ctv_email.setText(userDto.email);
        if (!userDto.picture.equals("")) {
            Picasso.with(this).load(userDto.picture).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(profileImage);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return slideMenu.dispatchTouchEvent(ev);

    }

    public ResideMenu getResideMenu() {
        return slideMenu;
    }

    /**
     * Sets the activity name.
     *
     * @param activityName the new activity name
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    /**
     * setContentView
     *
     * @param layoutResID is the resource layout Id
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setUpSlideMenu();
        initializer();
        setValueOnSideMenu();
    }

    /**
     * Making notification bar transparent
     */

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
    }


    /**
     * Sets the slide menu.
     */
    private void setUpSlideMenu() {
        slideMenu = new ResideMenu(this, R.layout.slide_menu_layout, -1);
        slideMenu.setUse3D(true);
        slideMenu.setBackground(R.drawable.intro_screenbg);
        slideMenu.attachToActivity(this);
        slideMenu.setMenuListener(menuListener);
        slideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        //valid scale factor is between 0.0f and 1.0f. leftMenu width is 150dip.
        slideMenu.setScaleValue(0.8f);
        initView();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            isSlidingMenuOPen = true;
            if (mFragmentManager.findFragmentById(R.id.fl_container) != null) {
                ((BaseFragment) mFragmentManager.findFragmentById(R.id.fl_container)).onOpen();
            }
        }

        @Override
        public void closeMenu() {
            isSlidingMenuOPen = false;
            if (mFragmentManager.findFragmentById(R.id.fl_container) != null) {
                ((BaseFragment) mFragmentManager.findFragmentById(R.id.fl_container)).onClose();
            }
        }
    };

    /**
     * Initialize the instance.
     */
    @SuppressLint("CommitTransaction")
    private void initializer() {
        View leftMenu = slideMenu.getLeftMenuView();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        profileImage = leftMenu.findViewById(R.id.iv_profileImage);
        tv_name = leftMenu.findViewById(R.id.tv_name);
        ctv_email = leftMenu.findViewById(R.id.ctv_email);
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        mFragmentTransaction.commitAllowingStateLoss();
//    }

    /**
     * Hide the keyboard.
     */
    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
            if (imm != null && getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null)
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("requestCode  " + requestCode + " resultCode " + resultCode);
        super.onActivityResult(requestCode, resultCode, intent);
        if (mFragmentManager.findFragmentById(R.id.fl_container) != null) {
            (mFragmentManager.findFragmentById(R.id.fl_container)).onActivityResult(requestCode, resultCode, intent);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (mFragmentManager.findFragmentById(R.id.fl_container) != null) {
                (mFragmentManager.findFragmentById(R.id.fl_container)).onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
                // permission was granted, yay! Do the contacts-related task you need to do.
            }
        }
    }

    /**
     * On button click.
     *
     * @param view the View id of the button clicked
     */
    public void onButtonClick(View view) {
        setValueOnSideMenu();
        hideKeyboard();
        switch (view.getId()) {
            case R.id.btn_menu:
                System.out.println("is here");
                if (isOpenSlide) {
                    slideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                    ((BaseFragment) mFragmentManager.findFragmentById(R.id.fl_container)).slidingMenuToggle();
                }
                break;
            case R.id.ctv_home:
                if (isOpenSlide) {
                    if (!AppConstant.HOME_SCREEN.equals(activityName)) {
                        setDefaultBG();
                        ctv_home.setBackground(getResources().getDrawable(R.drawable.shaddo));
                        removeAllFragment();
                        replaceFragment(HomeFragment.newInstance(), false, HomeFragment.class.getName());
                    }
                    slideMenu.closeMenu();
                }
                break;
            case R.id.ctv_profile:
                if (isOpenSlide) {
                    setDefaultBG();
                    ctv_profile.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    removeAllFragment();
                    replaceFragment(MyProfileFragment.newInstance(), false, MyProfileFragment.class.getName());
                    // replaceFragment(PaymentFragment.newInstance(), false, PaymentFragment.class.getName());
                    slideMenu.closeMenu();
                }
                break;
            case R.id.ctv_help:
                if (isOpenSlide) {
                    setDefaultBG();
                    ctv_help.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    removeAllFragment();
                    replaceFragment(HelpFragment.newInstance(), false, HelpFragment.class.getName());
                    slideMenu.closeMenu();
                }
                break;
            case R.id.ctv_point_system:
                if (isOpenSlide) {
                    setDefaultBG();
                    ctv_point_system.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    removeAllFragment();
                    replaceFragment(PointSystemFragment.newInstance(), false, PointSystemFragment.class.getName());
                    slideMenu.closeMenu();
                }
                break;
            case R.id.ctv_invite_friends:
                if (isOpenSlide) {
                    setDefaultBG();
                    ctv_invite_friends.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    removeAllFragment();
                    replaceFragment(InviteFriendsFragment.newInstance(), false, InviteFriendsFragment.class.getName());
                    slideMenu.closeMenu();
                }
                break;
            case R.id.ctv_contest_invited_code:
                if (isOpenSlide) {
                    setDefaultBG();
                    ctv_contest_invited_code.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    removeAllFragment();
                    replaceFragment(InviteCodeFragment.newInstance(), false, InviteCodeFragment.class.getName());
                    slideMenu.closeMenu();
                }
                break;
            case R.id.ctv_account:
                if (isOpenSlide) {
                    setDefaultBG();
                    ctv_account.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    removeAllFragment();
                    replaceFragment(MyAccountFragment.newInstance(), false, MyAccountFragment.class.getName());
                    slideMenu.closeMenu();
                }
                break;
            case R.id.ctv_leader_board:
                if (isOpenSlide) {
                    setDefaultBG();
                    ctv_leader_board.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    removeAllFragment();
                    replaceFragment(InviteCodeFragment.newInstance(), false, InviteCodeFragment.class.getName());
                    slideMenu.closeMenu();
                }
                break;
            case R.id.ctv_logout:
                if (isOpenSlide) {
                    setDefaultBG();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    LoginManager.getInstance().logOut();
                    ctv_logout.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    slideMenu.closeMenu();
                    PreferenceUtility.saveObjectInAppPreference(this, null, PreferenceUtility.APP_PREFERENCE_NAME);
                    ActivityController.startNextActivity(this, IntroScreen.class, true);
                }
                break;
            case R.id.ctv_ticket_system:
                if (isOpenSlide) {
                    setDefaultBG();
                    ctv_ticket_system.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    removeAllFragment();
                    replaceFragment(TicketSystemFragment.newInstance(), false, TicketSystemFragment.class.getName());
                    slideMenu.closeMenu();
                }
                break;
            case R.id.ctv_my_contests:
                if (isOpenSlide) {
                    setDefaultBG();
                    ctv_my_contests.setBackground(getResources().getDrawable(R.drawable.shaddo));
                    removeAllFragment();
                    //replaceFragment(MyContestFragment.newInstance(), false, MyContestFragment.class.getName());
                    replaceFragment(MyContestFragmentNotUses.newInstance(), false, MyContestFragmentNotUses.class.getName());
                    slideMenu.closeMenu();
                }
                break;
        }
        if (mFragmentManager.findFragmentById(R.id.fl_container) != null) {
            ((BaseFragment) mFragmentManager.findFragmentById(R.id.fl_container)).onButtonClick(view);
        }
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fl_container);

        if (isSlidingMenuOPen) {
            slideMenu.closeMenu();
        } else {
            if (MyApplication.isBackPressedEnabled) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    this.finish();
                }
            }
        }
    }

    /**
     * Begin transit.
     */
    public void beginTransit() {
        mFragmentTransaction = mFragmentManager.beginTransaction();
    }

    /**
     * Adds the fragment.
     *
     * @param mFragment        the m fragment
     * @param isAddToBackStack the is add to back stack
     * @param tag              the tag
     * @
     */
    public void addFragment(Fragment mFragment, boolean isAddToBackStack, String tag) {
        slideMenu.clearIgnoredViewList();
        firedOnPause();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (isAddToBackStack)
            mFragmentTransaction.addToBackStack(tag);
//        mFragmentTransaction.add(R.id.fl_container, mFragment, tag);
        mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
                R.anim.slide_out_right);
        mFragmentTransaction.replace(R.id.fl_container, mFragment, tag);
        mFragmentTransaction.commitAllowingStateLoss();
        firedOnResume(false);
    }

    /**
     * Fired on resume.
     *
     * @param isBackPressed the is back pressed
     */
    private void firedOnResume(boolean isBackPressed) {
        mFireResumeHandler.sendEmptyMessageDelayed(isBackPressed ? 0 : 1, 10);
    }

    /**
     * Fired on pause.
     */
    private void firedOnPause() {
        if (mFragmentManager.findFragmentById(R.id.fl_container) != null && mFragmentManager.findFragmentById(R.id.fl_container) instanceof BaseFragment) {
            ((BaseFragment) mFragmentManager.findFragmentById(R.id.fl_container)).onPauseFragment();
        }
    }

    /**
     * The m fire resume handler.
     */
    private final FireResumeHandler mFireResumeHandler = new FireResumeHandler();

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        
    }

    /**
     * The Class FireResumeHandler.
     */
    @SuppressLint("HandlerLeak")
    private class FireResumeHandler extends Handler {

        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mFragmentManager.findFragmentById(R.id.fl_container) != null && mFragmentManager.findFragmentById(R.id.fl_container) instanceof BaseFragment) {
                ((BaseFragment) mFragmentManager.findFragmentById(R.id.fl_container)).onResumeFragment(msg.what == 0);
            }
        }

    }

    /**
     * Replace fragment.
     *
     * @param mFragment        the m fragment
     * @param isAddToBackStack the is add to back stack
     * @param tag              the tag
     */
    public void replaceFragment(Fragment mFragment, boolean isAddToBackStack, String tag) {
        slideMenu.clearIgnoredViewList();
        firedOnPause();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (isAddToBackStack)
            mFragmentTransaction.addToBackStack(tag);
        mFragmentTransaction.replace(R.id.fl_container, mFragment, tag);
        mFragmentTransaction.commit();
        firedOnResume(false);
    }

    /**
     * Removes the fragment.
     */
    public void removeFragment() {
        firedOnPause();
        mFragmentManager.popBackStack();
        firedOnResume(true);
    }

    /**
     * Removes the all fragment.
     */
    public void removeAllFragment() {
        //firedOnPause();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // firedOnResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }
}
