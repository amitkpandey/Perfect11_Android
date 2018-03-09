package com.perfect11.base;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perfect11.R;
import com.utility.CommonUtilities;
import com.utility.customView.CustomButton;
import com.utility.customView.CustomTextView;

/**
 * The Class BaseFragment that extends Fragment class
 */
public class BaseFragment extends Fragment {

    public CustomButton  btn_menu;
    public LinearLayout ll_image;
    protected CustomTextView tv_header;
    private ImageView img_back;

    public View view;


//    private static ImageLoadingListener animateFirstListener;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        animateFirstListener = new AnimateFirstDisplayListener();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }

    public void onNewIntent(Intent intent) {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

    }

    /**
     * Sliding menu toggle.
     */
    public void slidingMenuToggle() {
        System.out.println("slidingMenuToggle");
    }

    /**
     * onCreateView
     *
     * @param inflater           the LayoutInflater
     * @param container          the ViewGroup
     * @param savedInstanceState the Bundle
     * @return view the View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyApplication.isBackPressedEnabled = true;
        ((BaseHeaderActivity) getActivity()).setActivityName("");
//        ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Call parent method.
     */
    public void callParentMethod() {
        getActivity().onBackPressed();
    }

    /**
     * Initialize the views
     */
    private void initializer() {
        img_back = view.findViewById(R.id.img_back);
        tv_header = view.findViewById(R.id.tv_header);
        btn_menu = view.findViewById(R.id.btn_menu);
//        ll_image = view.findViewById(R.id.ll_image);
    }

    protected void checkConnection() {
        if (CommonUtilities.checkConnectivity(getActivity())) {
            ll_image.setVisibility(View.GONE);
        } else {
            ll_image.setVisibility(View.VISIBLE);
        }
    }


    protected void setHeader(String title) {
        initializer();
        btn_menu.setVisibility(View.VISIBLE);
        tv_header.setVisibility(View.VISIBLE);
        img_back.setVisibility(View.GONE);
        btn_menu.setClickable(true);
        tv_header.setText(title);
    }



    protected void setInnerHeader(String title) {
        initializer();
        btn_menu.setVisibility(View.GONE);
        img_back.setVisibility(View.VISIBLE);
        tv_header.setVisibility(View.VISIBLE);
        btn_menu.setClickable(true);
        tv_header.setText(title);
    }


    /**
     * Sets the header with back button.
     *
     * @param headerText the header String
     */
    protected void setHeaderWithBackButton(String headerText, String buttonText) {
        initializer();
    }

    /**
     * On button click.
     *
     * @param view the View
     */
    public void onButtonClick(View view) {

    }

    /**
     * Call base methods.
     */
    public void callBaseMethods() {
        ((BaseHeaderActivity) getActivity()).beginTransit();
        ((BaseHeaderActivity) getActivity()).removeFragment();
    }

    /**
     * Show the keyboard.
     *
     * @param et the EditText
     */
    public void showKeyboard(EditText et) {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide the keyboard.
     */
    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * On resume fragment.
     *
     * @param isBackPressed the is back pressed
     */
    public void onResumeFragment(boolean isBackPressed) {

    }

    /**
     * On open the sliding menu.
     */
    public void onOpen() {
        enableDisableViewGroup((ViewGroup) view.findViewById(R.id.ll_root), false);
    }

    /**
     * On close the sliding menu.
     */
    public void onClose() {
        enableDisableViewGroup((ViewGroup) view.findViewById(R.id.ll_root), true);
    }


    /**
     * Enables/Disables all child views in a view group.
     *
     * @param viewGroup the view group
     * @param enabled   <code>true</code> to enable, <code>false</code> to disable
     *                  the views.
     */
    private void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        if (view.findViewById(R.id.img_back) != null)
            view.findViewById(R.id.img_back).setEnabled(true);
        if (view.findViewById(R.id.btn_menu) != null)
            view.findViewById(R.id.btn_menu).setEnabled(true);

        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }

    /**
     * On pause fragment.
     */
    public void onPauseFragment() {

    }

    /**
     * The listener interface for receiving animateFirstDisplay events.
     * The class that is interested in processing a animateFirstDisplay
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addAnimateFirstDisplayListener<code> method. When
     * the animateFirstDisplay event occurs, that object's appropriate
     * method is invoked.
     *
     * @see AnimateFirstDisplayListener
     */
//    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//
//        /**
//         * The Constant displayedImages.
//         */
//        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//        /* (non-Javadoc)
//         * @see com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener#onLoadingComplete(java.lang.String, android.view.View, android.graphics.Bitmap)
//         */
//        @Override
//        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            if (loadedImage != null) {
//                ImageView imageView = (ImageView) view;
//                boolean firstDisplay = !displayedImages.contains(imageUri);
//                if (firstDisplay) {
//                    FadeInBitmapDisplayer.animate(imageView, 500);
//                    displayedImages.add(imageUri);
//                }
//            }
//        }
//    }

    /**
     * On back pressed.
     */
    public void onBackPressed() {

    }
}
