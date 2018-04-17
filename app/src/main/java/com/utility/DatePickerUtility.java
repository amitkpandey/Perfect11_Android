package com.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

// TODO: Auto-generated Javadoc

/**
 * The Class DatePickerUtility.
 */
public class DatePickerUtility {

    /**
     * The m activity.
     */
    private static Activity mActivity;

    /**
     * The m i date picker.
     */
    private static IDatePicker mIDatePicker;

    /**
     * The m i time picker.
     */
    @SuppressWarnings("unused")
    private static ITimePicker mITimePicker;

//    private static EditText et_next;

    /**
     * The tv date.
     */
    private static TextView tvDate;

    /**
     * The is expire date.
     */
    @SuppressWarnings("unused")
    private static boolean isExpireDate = false;

    /**
     * The is check current date.
     */
    private static boolean isCheckCurrentDate = false;

    /**
     * The time.
     */
    @SuppressWarnings("unused")
    private static String time;

    /**
     * The date.
     */
    @SuppressWarnings("unused")
    private static String date;

    /**
     * The is listener fired.
     */
    private static boolean isListenerFired = false;

    /**
     * The on date select listener.
     */
    public static OnDateSelectListener onDateSelectListener;

    /**
     * The Interface IDatePicker.
     */
    public interface IDatePicker {

        /**
         * On date changed.
         *
         * @param date the date
         */
        void onDateChanged(String date);
    }

    /**
     * The Interface ITimePicker.
     */
    public interface ITimePicker {

        /**
         * On time changed.
         *
         * @param time the time
         */
        void onTimeChanged(String time);
    }

    // public static Dialog createDatePickerDialog(Activity activity,IDatePicker
    // iDatePicker) {
    // mActivity = activity;
    // mIDatePicker = iDatePicker;
    // DatePickerUtility.isExpireDate = false;
    // Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    // int cyear = c.get(Calendar.YEAR);
    // int cmonth = c.get(Calendar.MONTH);
    // int cday = c.get(Calendar.DAY_OF_MONTH);
    // return new DatePickerDialog(mActivity, mDateSetListener, cyear, cmonth,
    // cday);
    // }

    /**
     * Creates the date picker dialog.
     *
     * @param activity the activity
     * @param tv       the tv
     * @param flag     the flag
     * @return the dialog
     */
    public static Dialog createDatePickerDialog(Activity activity, TextView tv, boolean flag) {
        mActivity = activity;
        tvDate = tv;
        isCheckCurrentDate = flag;
        isListenerFired = false;
        DatePickerUtility.isExpireDate = false;
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        // System.out.println("--> TimeZone.getDefault() "+c.getTimeZone());
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(mActivity, mDateSetListener, cYear, cMonth, cDay);
        mDatePickerDialog.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        return mDatePickerDialog;
    }

    /**
     * Creates the date picker dialog.
     *
     * @param activity              the activity
     * @param tv                    the tv
     * @param flag                  the flag
     * @param isfutureDated         the isfuture dated
     * @param onDateSelectListener1 the on date select listener1
     * @return the dialog
     */
    public static Dialog createDatePickerDialog(Activity activity, TextView tv, @SuppressWarnings("SameParameterValue") boolean flag, boolean isfutureDated,
                                                EditText et_nextFocus, OnDateSelectListener onDateSelectListener1) {
        mActivity = activity;
        tvDate = tv;
//        et_next = et_nextFocus;
        onDateSelectListener = onDateSelectListener1;
        isCheckCurrentDate = flag;
        isListenerFired = false;
        DatePickerUtility.isExpireDate = false;
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(mActivity, mDateSetListener, cyear, cmonth, cday);
        if (isfutureDated) {
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        mDatePickerDialog.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        return mDatePickerDialog;
    }

    /**
     * Creates the date picker dialog.
     *
     * @param activity   the activity
     * @param preSetDate the pre set date
     * @param tv         the tv
     * @param flag       the flag
     * @return the dialog
     */
    @SuppressLint("SimpleDateFormat")
    public static Dialog createDatePickerDialog(Activity activity, String preSetDate, TextView tv, boolean flag) {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setTimeZone(TimeZone.getDefault());
        Date d1 = null;
        isCheckCurrentDate = flag;
        isListenerFired = false;
        mActivity = activity;
        tvDate = tv;
        DatePickerUtility.isExpireDate = false;
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        try {
            d1 = formatter.parse(preSetDate);
            c.setTime(d1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(mActivity, mDateSetListener, cyear, cmonth, cday);
    }

    /**
     * Creates the time picker dialog.
     *
     * @param activity the activity
     * @param tv       the tv
     * @return the dialog
     */
    public static Dialog createTimePickerDialog(Activity activity, TextView tv) {
        mActivity = activity;
        tvDate = tv;
        DatePickerUtility.isExpireDate = false;
        isListenerFired = false;
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        int chour = c.get(Calendar.HOUR_OF_DAY);
        int cminute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(mActivity, mOnTimeSetListener, chour, cminute, false);
    }

    /**
     * Creates the time picker dialog.
     *
     * @param activity   the activity
     * @param preSetDate the pre set date
     * @param tv         the tv
     * @return the dialog
     */
    @SuppressLint("SimpleDateFormat")
    public static Dialog createTimePickerDialog(Activity activity, String preSetDate, TextView tv) {
        DateFormat formatter = new SimpleDateFormat("hh:mm aa");
        Date d1 = null;
        mActivity = activity;
        tvDate = tv;
        DatePickerUtility.isExpireDate = false;
        isListenerFired = false;
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        try {
            d1 = formatter.parse(preSetDate);
            c.setTime(d1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int chour = c.get(Calendar.HOUR_OF_DAY);
        int cminute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(mActivity, mOnTimeSetListener, chour, cminute, false);
    }

    // public static Dialog createDatePickerDialog(Activity activity,TextView
    // tv, boolean isExpireDate) {
    // mActivity = activity;
    // tvDate = tv;
    // DatePickerUtility.isExpireDate = isExpireDate;
    // Calendar c = Calendar.getInstance(TimeZone.getDefault());
    // int cyear = c.get(Calendar.YEAR);
    // int cmonth = c.get(Calendar.MONTH);
    // int cday = c.get(Calendar.DAY_OF_MONTH);
    // DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity,
    // mDateSetListener, cyear, cmonth, cday);
    // customDatePicker(datePickerDialog);
    // return datePickerDialog;
    // }

    /**
     * The m date set listener.
     */
    private static final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        // onDateSet method
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            System.out.println("month " + monthOfYear + " year " + year + " day " + dayOfMonth);
            if (!isListenerFired) {

                isListenerFired = true;
                String month;
                String day;
                if ((monthOfYear + 1) < 10) {
                    month = "0" + String.valueOf(monthOfYear + 1);
                } else {
                    month = String.valueOf(monthOfYear + 1);
                }

                if (dayOfMonth < 10) {
//                    System.out.println("dayOfMonth " + dayOfMonth);
                    day = "0" + String.valueOf(dayOfMonth);
                } else {
                    day = String.valueOf(dayOfMonth);
                }
                System.out.println(year + "-" + month + "-" + dayOfMonth);
                tvDate.setText(day + "-" + month + "-" + year);
                String date_selected = String.valueOf(year) + "-" + month + "-" + day;
//				if (mIDatePicker != null) {
                //mIDatePicker.onDateChanged(date_selected);
//				}

//				if (isCheckCurrentDate) {
//					if (!DateUtility.isAfterCurrentDate(date_selected)) {
//						DialogUtility.showMessageWithOk("You can not select date before current date.", mActivity);
//						return;
//					}
//				}

                if (tvDate != null) {
//                    if (et_next != null)
//                        et_next.requestFocus();
                    if (onDateSelectListener != null)
                        onDateSelectListener.onDateSelect(tvDate.getText().toString());
                }

            }
        }
    };

    /**
     * The m on time set listener.
     */
    private static final TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            updateTime(hourOfDay, minute);
        }
    };

    // Used to convert 24hr format to 12hr format with AM/PM values

    /**
     * Update time.
     *
     * @param hours the hours
     * @param mins  the mins
     */
    private static void updateTime(int hours, int mins) {

        // if(DateUtility.isAfterTimeDate(hours+":"+mins)){
        // DialogUtility.showMessageWithOk("You can not select time before current time.",mActivity);
        // return ;
        // }

        String timeSet;
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        System.out.println("aTime==>" + aTime);
        tvDate.setText(aTime);
    }

    /**
     * Custom date picker.
     *
     * @param datePickerDialog the date picker dialog
     */
    @SuppressWarnings("unused")
    private static void customDatePicker(DatePickerDialog datePickerDialog) {
        try {
            // datePickerDialog.setTitle("Set Expire Date ");
            Field[] datePickerDialogFields = datePickerDialog.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(datePickerDialog);
                    Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            // ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void setOnDateClickListener(OnDateSelectListener OnDateSelectListener) {
        onDateSelectListener = OnDateSelectListener;
    }

    /**
     * The Interface OnDateSelectListener.
     */
    public interface OnDateSelectListener {

        /**
         * On date select.
         */
        void onDateSelect(String date);

    }

}
