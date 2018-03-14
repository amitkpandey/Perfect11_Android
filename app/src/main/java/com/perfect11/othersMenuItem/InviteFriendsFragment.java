package com.perfect11.othersMenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.login_signup.dto.UserDto;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;

public class InviteFriendsFragment extends BaseFragment {
    private UserDto userDto;
    private CustomTextView ctv_share_code;
    private boolean flag=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_invite_friends,container,false);
        setHeader("Invite Friends");
       initView();
        return view;
    }

    private void initView() {
        userDto = (UserDto) PreferenceUtility.getObjectInAppPreference(getActivity(), PreferenceUtility.APP_PREFERENCE_NAME);
        ctv_share_code=view.findViewById(R.id.ctv_share_code);
        ctv_share_code.setText(userDto.reference_id);

        try {
            flag=getArguments().getBoolean("flag");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flag)
        {
            setInnerHeader("Invite Friends");
        }
    }

    @Override
    public void onButtonClick(View view) {
        super.onButtonClick(view);
        switch (view.getId())
        {
            case R.id.img_back:
            getActivity().onBackPressed();
            break;
            case R.id.btn_reset:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Perfect11 Reference ID: "+userDto.reference_id);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
    }

    public static Fragment newInstance() {
    return new InviteFriendsFragment();
    }
}
