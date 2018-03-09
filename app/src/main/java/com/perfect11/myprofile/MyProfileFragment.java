package com.perfect11.myprofile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect11.R;
import com.perfect11.base.BaseFragment;
import com.perfect11.base.BaseHeaderActivity;
import com.perfect11.login_signup.dto.UserDto;
import com.utility.PreferenceUtility;
import com.utility.customView.CustomTextView;


public class MyProfileFragment extends BaseFragment {
    private CustomTextView ctv_name,ctv_phone,ctv_mail;
    private UserDto userDto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        setHeader("My Profile");
        initView();
        return view;
    }

    private void initView() {
        ctv_name=view.findViewById(R.id.ctv_name);
        ctv_phone=view.findViewById(R.id.ctv_phone);
        ctv_mail=view.findViewById(R.id.ctv_mail);
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
                ((BaseHeaderActivity)getActivity()).addFragment(new ChangeProfileFragment(),true,ChangeProfileFragment.class.getName());
                break;
        }
    }
}
