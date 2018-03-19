package com.utility.facebook;

import android.app.Activity;

// TODO: Auto-generated Javadoc

/**
 * The Class FacebookShareUtility.
 */
public class FacebookShareUtility {

    /**
     * On share on facebook.
     *
     * @param name        the name
     * @param caption     the caption
     * @param description the description
     * @param captionLink the caption link
     * @param pictureLink the picture link
     * @param activity    the activity
     */
    public static void onShareOnFacebook(final String name, final String caption, final String description, final String captionLink,
                                         final String pictureLink, Activity activity) {
        final FacebookUtil facebookUtil = FacebookUtil.getFBUtilInstance(activity);
//		facebookUtil.isFBLogin = true;
//		facebookUtil.initFacebook(activity);
//		facebookUtil.setFacebookLoginListener(new FacebookLoginListener() {
//			//
//			@Override
//			public void onFailure() {
//				System.out.println("failed");
//			}
//
//			@Override
//			public void onCompleted(UserInfo userInfo) {
//				System.out.println("userInfo  " + userInfo);
        String trimmedDescription = description.substring(0, Math.min(description.length(), 50));
        facebookUtil.publishFeedDialog(name + "\n" + caption, trimmedDescription + ". " + captionLink, captionLink, pictureLink);
        /* http://mobisolz.com/DemoProject/snapp/ */
    }
//		});
//		facebookUtil.doLogin();
//	}
}
