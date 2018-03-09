package com.utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.perfect11.R;


public class ActivityController {

    // public static List<Activity> activities;

    public static void startNextActivity(Activity activity, Class<?> clazz, boolean isClearActivityStack) {
        Intent intent = new Intent(activity, clazz);
        if (isClearActivityStack) {
            clearActivityStack(intent);
        }
        startAndTracActivity(activity, intent);
    }

    public static void startNextService(Activity activity, Class<?> clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startService(intent);
    }

    public static void stopService(Activity activity, Class<?> clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.stopService(intent);
    }

    public static void startNextActivity(Activity activity, Class<?> clazz, Bundle bundle, boolean isClearActivityStack) {
        Intent intent = new Intent(activity, clazz);
        if (isClearActivityStack) {
            clearActivityStack(intent);
        }
        if (bundle != null)
            intent.putExtras(bundle);
        startAndTracActivity(activity, intent);
    }

    public static void startNextActivityForResult(Activity activity, Class<?> clazz, boolean isClearActivityStack,
                                                  int requestCode) {
        Intent intent = new Intent(activity, clazz);
        if (isClearActivityStack) {
            clearActivityStack(intent);
        }
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void startNextActivityForResult(Activity activity, Class<?> clazz, Bundle bundle,
                                                  boolean isClearActivityStack, int requestCode) {

        Intent intent = new Intent(activity, clazz);
        if (isClearActivityStack) {
            clearActivityStack(intent);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private static void startAndTracActivity(Activity activity, Intent intent) {
        addActivityToStack(activity);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private static void addActivityToStack(Activity activity) {
        // if(activities == null){
        // activities = new ArrayList<Activity>();
        // }
        // activities.add(activity);
    }

    private static void clearActivityStack(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}
