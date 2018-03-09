package com.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.IOException;

public class PreferenceUtility {

    public static final String APP_PREFERENCE_NAME = "RENTING_STREET_PREF";

    public static final String DEVICE_TOKEN = "token";

    /**
     * getAppSharedPreference returns Application Shared Preference
     *
     * @param activity
     * @return SharedPreferences
     */
    public static SharedPreferences getAppSharedPreference(String preferenceName, Activity activity) {
        return activity.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getAppSharedPreference(String preferenceName, Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    /**
     * getAppPrefsEditor
     *
     * @param activity
     * @return editor
     */
    public static Editor getAppPrefsEditor(String preferenceName, Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        //		prefsEditor.clear();
//		prefsEditor.commit();
        return prefs.edit();

    }

    public static Editor getAppPrefsEditor(String preferenceName, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return prefs.edit();

    }

    /**
     * saveObjectInAppPreference save given Object into Application
     * SharedPreference
     *
     * @param activity
     * @param object
     * @param prefName
     */
    public static void saveObjectInAppPreference(Activity activity, Object object, String prefName) {
        Editor prefsEditor = getAppPrefsEditor(prefName, activity);
        try {
            prefsEditor.putString(prefName, object != null ? ObjectSerializer.serialize(object) : null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }

    public static void saveObjectInAppPreference(Context context, Object object, String prefName) {
        Editor prefsEditor = getAppPrefsEditor(prefName, context);
        try {
            prefsEditor.putString(prefName, object != null ? ObjectSerializer.serialize(object) : null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }

    /**
     * getObjectInAppPreference returns saved Object from Application
     * SharedPreference
     *
     * @param activity
     * @param prefName
     * @return Object
     */
    public static Object getObjectInAppPreference(Activity activity, String prefName) {
        Object prefObject = null;
        SharedPreferences prefs = getAppSharedPreference(prefName, activity);
        String serializeString = prefs.getString(prefName, null);
        try {
            if (serializeString != null) {
                prefObject = ObjectSerializer.deserialize(serializeString);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return prefObject;

    }

    public static void clearPrefsEditor(String preferenceName, Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }


    public static Object getObjectInAppPreference(Context context, String prefName) {
        Object prefObject = null;
        SharedPreferences prefs = getAppSharedPreference(prefName, context);
        String serializeString = prefs.getString(prefName, null);
        try {
            if (serializeString != null) {
                prefObject = ObjectSerializer.deserialize(serializeString);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return prefObject;

    }

    public static void saveStringInPreference(Activity activity, String key, String value) {
        Editor prefsEditor = getAppPrefsEditor(key, activity);
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static String getStringFromPreference(Activity activity, String key) {
        SharedPreferences prefs = getAppSharedPreference(key, activity);
        return prefs.getString(key, "");
    }

}
