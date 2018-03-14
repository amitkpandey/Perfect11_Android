package com.perfect11.myprofile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imageCaptured.VideoSelectionDialog;
import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.base.AppConstant;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.login_signup.dto.UserDto;
import com.perfect11.myprofile.wrapper.MyContestInfoWrapper;
import com.perfect11.othersMenuItem.InviteFriendsFragment;
import com.perfect11.requestHandler.ApplicationServiceRequestHandler;
import com.perfect11.team_create.SelectPlayersActivity;
import com.perfect11.team_create.wrapper.PlayerWrapper;
import com.squareup.picasso.Picasso;
import com.utility.ActivityController;
import com.utility.CommonUtility;
import com.utility.Constants;
import com.utility.DialogUtility;
import com.utility.MarshMallowPermission;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyProfileFragment extends BaseFragment {
    private CustomTextView ctv_name,ctv_phone,ctv_mail;
    private UserDto userDto;
    private AutoCompleteTextView name;
    private CircleImageView image;
    public final static int STILL_IMAGE = 1;
    public final static int IMAGE1 = 1;
    private String selectProfileImage1 = "";
    private ApiInterface apiInterface;
    private MyContestInfoWrapper myContestInfoWrapper;
private Button btn_edit;

private TextView tv_contest_played,tv_contest_won;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        setHeader("My Profile");
        initView();
        callGetContestDetailsAPI();
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
        if (!marshMallowPermission.checkPermissionForCamera())
            marshMallowPermission.requestPermissionForCamera();
        return view;
    }

    private void initView() {
        btn_edit=view.findViewById(R.id.btn_edit);
        ctv_name=view.findViewById(R.id.ctv_name);
        ctv_phone=view.findViewById(R.id.ctv_phone);
        ctv_mail=view.findViewById(R.id.ctv_mail);
        image=view.findViewById(R.id.image);
        name=view.findViewById(R.id.name);
        name.setVisibility(View.GONE);

        tv_contest_played=view.findViewById(R.id.tv_contest_played);
        tv_contest_won=view.findViewById(R.id.tv_contest_won);

        setValue();
    }

    private void setValue() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        ctv_name.setText(userDto.first_name+" "+userDto.last_name);
        if(userDto.phone.trim().equals(""))
        {
            ctv_phone.setVisibility(View.GONE);
        }else
        {
            ctv_phone.setText(userDto.phone);
        }
        ctv_mail.setText(userDto.email);
        if (!userDto.picture.equals("")) {
            Picasso.with(getActivity()).load(userDto.picture).placeholder(R.drawable.progress_animation).error(R.drawable.myteam).into(image);
        }
    }

    public static Fragment newInstance() {
        return new MyProfileFragment();
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);

        switch (view.getId())
        {
            case R.id.btn_edit:
                //((BaseHeaderActivity)getActivity()).addFragment(new ChangeProfileFragment(),true,ChangeProfileFragment.class.getName());
                if (ctv_name.getVisibility() == View.VISIBLE) {
                    name.setVisibility(View.VISIBLE);
                    name.setText(userDto.first_name + " " + userDto.last_name);
                    ctv_name.setVisibility(View.GONE);
                    btn_edit.setBackground(getActivity().getResources().getDrawable(R.drawable.save_profile_icon));
                }else{
                    if(isValid())
                    callChangeProfileAPI();
                }
                break;
            case R.id.rl_imageHolder:
                Bundle bundle = new Bundle();
                bundle.putInt("type", STILL_IMAGE);
                ActivityController.startNextActivityForResult(getActivity(), VideoSelectionDialog.class, bundle, false,
                        IMAGE1);
                break;
            case R.id.rl_change_password:
                ((BaseHeaderActivity)getActivity()).addFragment(new ChangePasswordFragment(),true,ChangePasswordFragment.class.getName());
                break;
            case R.id.rl_refer_fnd:
                InviteFriendsFragment inviteFriendsFragment=new InviteFriendsFragment();
                Bundle bundle1=new Bundle();
                bundle1.putBoolean("flag",true);
                inviteFriendsFragment.setArguments(bundle1);
                ((BaseHeaderActivity)getActivity()).addFragment(inviteFriendsFragment,true,InviteFriendsFragment.class.getName());
                break;
        }
    }

    private void localMaskImage(Bitmap bitmap, ImageView imageView, int backgroundImageID) {
        bitmap = CommonUtility.getAspectBitmap(getActivity(), bitmap, imageView, backgroundImageID);
        imageView.setImageBitmap(bitmap);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Toast.makeText(this,"TEST"+resultCode,Toast.LENGTH_SHORT).show();
            if (requestCode == IMAGE1) {
                if (data != null) {
                    selectProfileImage1 = data.getStringExtra("path");
                    System.out.println("selectProfileImage " + selectProfileImage1);
                    Bitmap background = CommonUtility.decodeFile(AppConstant.BITMAP_WIDTH, AppConstant.BITMAP_HEIGHT,
                            selectProfileImage1);
                    localMaskImage(background, image, R.drawable.myteam);

                    if(!selectProfileImage1.equals(""))
                    {
                        callAPI1();
                    }

                }
            }
        }

    }

    private void callAPI1() {
        //API
        new ApplicationServiceRequestHandler(getActivity(),this, new String[]{"member_id", "picture"},
                new Object[]{userDto.member_id,new File(selectProfileImage1)},
                "Loading...", ApplicationServiceRequestHandler.CHANGEMYPICTURE, Constants.BASE_URL);
    }

    private void callChangeProfileAPI() {
        //API
        String str[]=name.getText().toString().trim().split(" ");
        new ApplicationServiceRequestHandler(getActivity(),this, new String[]{"member_id", "first_name","last_name","weblink","address"
                ,"country","state","city","gender","zipcode"},
                new Object[]{userDto.member_id,str[0],(str.length>=2)?str[1]:"","perfect11","","","","","",""},
                "Loading...", ApplicationServiceRequestHandler.EDIT_PROFILE, Constants.BASE_URL);
    }

    private boolean isValid() {
        if (name.getText().toString().trim().length() == 0) {
            name.requestFocus();
            name.setError("Enter your name");
            return false;
        }
        return true;
    }
    public void serviceCallback(String message) {
        DialogUtility.showMessageWithOk(message,getActivity());
    }

    public void serviceChangeProfileCallback(String message) {
        DialogUtility.showMessageWithOk(message,getActivity());
        ctv_name.setVisibility(View.VISIBLE);
        ctv_name.setText(userDto.first_name + " " + userDto.last_name);
        name.setVisibility(View.GONE);
        btn_edit.setBackground(getActivity().getResources().getDrawable(R.drawable.edit_profile_icon));
    }

    private void callGetContestDetailsAPI() {
        //API
        /**
         * Collecting data*/
        Log.d("API", "Get Player");
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<MyContestInfoWrapper> call = apiInterface.getMyContestDetails(userDto.member_id);
        call.enqueue(new Callback<MyContestInfoWrapper>() {
            @Override
            public void onResponse(Call<MyContestInfoWrapper> call, Response<MyContestInfoWrapper> response) {
                myContestInfoWrapper= response.body();
                tv_contest_played.setText(""+myContestInfoWrapper.data.contest_played);
                tv_contest_won.setText(""+myContestInfoWrapper.data.contest_win);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyContestInfoWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }
}
