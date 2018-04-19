package com.utility;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;


import com.perfect11.R;
import com.perfect11.upcoming_matches.UpcomingMatchesActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The Class CommonUtility.
 */
public class CommonUtility {

    public static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static String[] Small_months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static String[] weeks = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    public static String[] small_weeks = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    /**
     * The Constant MAIL_SEND.
     */
    private final static int MAIL_SEND = 2;

    /**
     * Check connectivity.
     *
     * @param context the context
     * @return true, if successful
     */
    public static boolean checkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isAvailable() && ni.isConnected();
    }

    public static boolean isNotExpired(String start_date,Activity activity) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date;
            date = sdf.parse(start_date);
            long millis = date.getTime();
            long hoursMillis = 60 * 60 * 1000;
            long timeDiff = (millis - hoursMillis) - System.currentTimeMillis();
            if (timeDiff > 0) {
                return true;
            } else {
                DialogUtility.showMessageWithOk("Match Closed", activity);
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    /*
     * Checks if is login.
     *
     * @param context the context
     * @return true, if is login
     */
//	public static boolean isLogin(Context context) {
//		LoginDto loginDto = PreferenceData.getUserDto(context, PreferenceData.SNAPP);
//		return loginDto != null;
//	}


    /**
     * Creates the file.
     *
     * @param context   the context
     * @param url       the url
     * @param extension the extension
     * @return the file
     */
    public static File createFile(Context context, String url, String extension) {
        File cacheDir;
        // Find the dir to save cached images
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDir = new File(Environment.getExternalStorageDirectory(), context.getResources().getString(R.string.app_name));
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            //noinspection ResultOfMethodCallIgnored
            cacheDir.mkdirs();

        String filename = String.valueOf(Math.abs(url.hashCode())) + "." + "jpeg";

        // if(f.exists())
        // f.delete();
        // try {
        // f.createNewFile();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        return new File(cacheDir, filename);
    }

    /**
     * ** Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView  ***
     *
     * @param listView the new search_list view height based on children
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
//			view.measure(MeasureSpec.makeMeasureSpec(desiredWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String getDateForEventDetailScreen(String eventDate) {
        String date = null;
        try {

            String[] arr;
            int weekPosition = 0;
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat outputFormat = new SimpleDateFormat("EEE-MM-dd-yyyy");//"KK:mm a"
            arr = outputFormat.format(inputFormat.parse(eventDate)).split("-");
            for (int i = 0; i < small_weeks.length; i++) {
                if (arr[0].equals(small_weeks[i])) {
                    weekPosition = i;
                }
            }
            date = weeks[weekPosition] + " " + months[Integer.parseInt(arr[1]) - 1] + " " + arr[2] + ", " + arr[3];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }


    public static int getDateDifference(String fromDate, String toDate) {
        try {
            Date date1 = null, date2 = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            date1 = df.parse(toDate);
            date2 = df.parse(fromDate);
            long diff = Math.abs(date1.getTime() - date2.getTime());
            long diffDays = diff / (24 * 60 * 60 * 1000);
//            System.out.println(diffDays);
            return (int) diffDays + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public static String getTomorrowDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return dateFormat.format(cal.getTime());
    }

    public static String getTodayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String[] getThisWeek() {
        String todayDate, firstDayOfWeek = null, currentDay;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        todayDate = dateFormat.format(cal.getTime());
        currentDay = new SimpleDateFormat("EEE").format(cal.getTime());
        if (currentDay.equals(small_weeks[0])) {
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[1])) {
            cal.add(Calendar.DATE, -1);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[2])) {
            cal.add(Calendar.DATE, -2);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[3])) {
            cal.add(Calendar.DATE, -3);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[4])) {
            cal.add(Calendar.DATE, -4);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[5])) {
            cal.add(Calendar.DATE, -5);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[6])) {
            cal.add(Calendar.DATE, -6);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        }
//        System.out.println("firstDayOfWeek==>" + firstDayOfWeek);
        try {
            cal.setTime(dateFormat.parse(firstDayOfWeek));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] arrayOfWeek = new String[7];
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfWeek[0] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfWeek[1] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfWeek[2] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfWeek[3] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfWeek[4] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfWeek[5] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfWeek[6] = dateFormat.format(cal.getTime());
        return arrayOfWeek;
    }

    public static String[] getNextWeek() {
        String todayDate, firstDayOfWeek = null, currentDay;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        todayDate = dateFormat.format(cal.getTime());
        currentDay = new SimpleDateFormat("EEE").format(cal.getTime());
        if (currentDay.equals(small_weeks[0])) {
            cal.add(Calendar.DATE, 7);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[1])) {
            cal.add(Calendar.DATE, 6);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[2])) {
            cal.add(Calendar.DATE, 5);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[3])) {
            cal.add(Calendar.DATE, 4);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[4])) {
            cal.add(Calendar.DATE, 3);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[5])) {
            cal.add(Calendar.DATE, 2);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        } else if (currentDay.equals(small_weeks[6])) {
            cal.add(Calendar.DATE, 1);
            firstDayOfWeek = dateFormat.format(cal.getTime());
        }
//        System.out.println("firstDayOfWeek==>" + firstDayOfWeek);
        try {
            cal.setTime(dateFormat.parse(firstDayOfWeek));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] arrayOfNextWeek = new String[7];
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfNextWeek[0] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfNextWeek[1] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfNextWeek[2] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfNextWeek[3] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfNextWeek[4] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfNextWeek[5] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
//        System.out.println("forward==>" + dateFormat.format(cal.getTime()));
        arrayOfNextWeek[6] = dateFormat.format(cal.getTime());
        return arrayOfNextWeek;
    }

    public static void getNextWeek(String todayDate) {

    }

    /**
     * Convert in km.
     *
     * @param value the value
     * @return the string
     */
    public static String convertInKM(String value) {
        double distance = Double.parseDouble(value);
        distance = distance / 1000;
        DecimalFormat df = new DecimalFormat("###.##");
        return (df.format(distance)) + " km";
    }

    public static String convertAmOrPm(String time) {
        String t1 = null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DateFormat outputFormat = new SimpleDateFormat("KK:mm a");//"KK:mm a"
        try {
            t1 = outputFormat.format(inputFormat.parse(time));
//            System.out.println(outputFormat.format(inputFormat.parse(time)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t1;
    }

    /**
     * Send email.
     *
     * @param emailId  the email id
     * @param subject  the subject
     * @param body     the body
     * @param activity the activity
     */
    public static void sendEmail(String emailId, String subject, String body, Activity activity) {
        Intent mailer = new Intent(Intent.ACTION_SEND);
        // mailer.setType("vnd.android.cursor.item/email");
        mailer.setType("message/rfc822");
        mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
        mailer.putExtra(Intent.EXTRA_SUBJECT, subject);
        mailer.putExtra(Intent.EXTRA_TEXT, body);
        activity.startActivityForResult(mailer, MAIL_SEND);
    }

    public static void sendEmail(String emailId, Activity activity) {
        Intent mailer = new Intent(Intent.ACTION_SEND);
        // mailer.setType("vnd.android.cursor.item/email");
        mailer.setType("message/rfc822");
        mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
//        mailer.putExtra(Intent.EXTRA_SUBJECT, subject);
//        mailer.putExtra(Intent.EXTRA_TEXT, body);
        activity.startActivityForResult(mailer, MAIL_SEND);
    }

    /**
     * Convert date to social networking format.
     *
     * @param datePosted the datePosted
     * @return the string
     */
    public static String convertDateToSocialNetworkingFormat(String datePosted) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        StringBuffer sBuffer = new StringBuffer("");
        Date d1 = null;

        try {
            d1 = formatter.parse(datePosted);
            Calendar c = Calendar.getInstance();
            c.setTime(d1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(new Date());
            // in milliseconds
            long diff = c1.getTimeInMillis() - c.getTimeInMillis();
            System.out.println("diff--->" + diff);
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays > 0)
                sBuffer.append(diffDays).append(" days ");
            else if (diffHours > 0 && diffDays <= 0)
                sBuffer.append(diffHours).append(" hours ");
            else if (diffMinutes > 0 && diffHours <= 0)
                sBuffer.append(diffMinutes).append(" mins ");
            else if (diffSeconds >= 0 && diffMinutes <= 0)
                sBuffer.append(diffSeconds + " secs");

            System.out.println("@@date--->" + sBuffer);

            return sBuffer.toString() + " ago";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Distance between two lat long.
     *
     * @param fromLatitude  the from latitude
     * @param fromLongitude the from longitude
     * @param toLatitude    the to latitude
     * @param toLongitude   the to longitude
     * @return the double
     */
    public static double distanceBetweenTwoLatLong(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {
        Location from_location = new Location("locationA");
        from_location.setLatitude(fromLatitude);
        from_location.setLongitude(fromLongitude);
        Location to_locations = new Location("locationB");
        to_locations.setLatitude(toLatitude);
        to_locations.setLongitude(toLongitude);
        return (double) from_location.distanceTo(to_locations);
    }

    /**
     * Validate email.
     *
     * @param emailID the email id
     * @return true, if successful
     */
    public static boolean validateEmail(String emailID) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(emailID);
        return !matcher.matches();
    }

    public static boolean validateDateFormat(String date) {
        String date_pattern = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern pattern = Pattern.compile(date_pattern);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    /**
     * Validate email.
     *
     * @param emailID the email id
     * @return true, if successful
     */
    public static boolean validateGovernmentEmail(String emailID) {
        if (!validateEmail(emailID)) {
            String lastfix = emailID.substring(emailID.lastIndexOf(".") + 1);
            System.out.println("last prefix==>" + lastfix);
            if (lastfix.equals("mil") || lastfix.equals("gov"))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    /**
     * Validate password.
     *
     * @param password the password
     * @return true, if successful
     */
    public static boolean validatePassword(String password) {
        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[A-Z])(?=.*[[~!@#$%^&*()_+-={}}|\"'?/:<,>;}]]).{6,20})";
//        String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\\d)(?!.*(AND|NOT)).*[a-z].*";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Validate password without special character.
     *
     * @param password the password
     * @return true, if successful
     */
    public static boolean validatePasswordWithoutSpecialCharacter(String password) {
        String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?!.*(AND|NOT)).*[a-z].*";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    /**
     * Show keyboard.
     *
     * @param et       the et
     * @param activity the activity
     */
    public static void showKeyboard(EditText et, Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboardForcefully(EditText et, Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(et, 0);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide keyboard.
     *
     * @param activity the activity
     */
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboardForceFully(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(new View(activity).getWindowToken(), 0);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * Scale picture.
     *
     * @param width             the width
     * @param height            the height
     * @param mCurrentPhotoPath the m current photo path
     * @return the bitmap
     */
    public static Bitmap scalePicture(float width, float height, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = (int) width;
        int targetH = (int) height;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }

    /**
     * Gets the resized bitmap.
     *
     * @param bm        the bm
     * @param newHeight the new height
     * @param newWidth  the new width
     * @return the resized bitmap
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    // decodes image and scales it to reduce memory consumption

    /**
     * Decode file.
     *
     * @param width  the width
     * @param height the height
     * @param f      the f
     * @return the bitmap
     */
    public static Bitmap decodeFile(int width, int height, File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			/*
             * // Find the correct scale value. It should be the power of 2. int
			 * REQUIRED_Height = (int)height; int REQUIRED_Widtht = (int)width;;
			 * int scale = 0; int width_tmp = o.outWidth, height_tmp =
			 * o.outHeight;
			 */

			/*
             * while (true) { if (width_tmp < REQUIRED_Widtht || height_tmp <
			 * REQUIRED_Height) break; width_tmp /= 2; height_tmp /= 2; scale *=
			 * 2; }
			 */

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = calculateInSampleSize(o, width, height);
            // System.out.println("Scale: " + scale);
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            // System.out.println("--- exception "+e.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Calculate in sample size.
     *
     * @param options   the options
     * @param reqWidth  the req width
     * @param reqHeight the req height
     * @return the int
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /**
     * Utility function for creating a scaled version of an existing bitmap.
     *
     * @param unscaledBitmap Bitmap to scale
     * @param dstWidth       Wanted width of destination bitmap
     * @param dstHeight      Wanted height of destination bitmap
     * @param scalingLogic   Logic to use to avoid image stretching
     * @return New scaled bitmap object
     */
    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    /**
     * Calculate src rect.
     *
     * @param srcWidth     the src width
     * @param srcHeight    the src height
     * @param dstWidth     the dst width
     * @param dstHeight    the dst height
     * @param scalingLogic the scaling logic
     * @return the rect
     */
    private static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    /**
     * Calculate dst rect.
     *
     * @param srcWidth     the src width
     * @param srcHeight    the src height
     * @param dstWidth     the dst width
     * @param dstHeight    the dst height
     * @param scalingLogic the scaling logic
     * @return the rect
     */
    private static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    /**
     * Calculate sample size.
     *
     * @param srcWidth     the src width
     * @param srcHeight    the src height
     * @param dstWidth     the dst width
     * @param dstHeight    the dst height
     * @param scalingLogic the scaling logic
     * @return the int
     */
    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    /**
     * ScalingLogic defines how scaling should be carried out if source and
     * destination image has different aspect ratio.
     * <p/>
     * CROP: Scales the image the minimum amount while making sure that at least
     * one of the two dimensions fit inside the requested destination area.
     * Parts of the source image will be cropped to realize this.
     * <p/>
     * FIT: Scales the image the minimum amount while making sure both
     * dimensions fit inside the requested destination area. The resulting
     * destination dimensions might be adjusted to a smaller size than
     * requested.
     */
    public enum ScalingLogic {

        /**
         * The crop.
         */
        CROP,
        /**
         * The fit.
         */
        FIT
    }

    // decodes image and scales it to reduce memory consumption

    /**
     * Decode file.
     *
     * @param width  the width
     * @param height the height
     * @param path   the path
     * @return the bitmap
     */
    public static Bitmap decodeFile(int width, int height, String path) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            // System.out.println("Scale:  width_tmp  " + width_tmp +
            // " height_tmp "+ height_tmp);

            while (true) {
                if (width_tmp < width || height_tmp < height)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            // System.out.println("Scale: " + scale);
            return BitmapFactory.decodeFile(path, o2);
        } catch (Exception e) {
            // System.out.println("--- exception "+e.toString());
            e.printStackTrace();
        }
        return null;
    }

    // public static Bitmap scalePicture( float width , float height , String
    // mCurrentPhotoPath) {
    // // Get the dimensions of the View
    // int targetW = (int)width;
    // int targetH = (int)height;
    //
    // // Get the dimensions of the bitmap
    // BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    // bmOptions.inJustDecodeBounds = true;
    // BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    // int photoW = bmOptions.outWidth;
    // int photoH = bmOptions.outHeight;
    //
    // // Determine how much to scale down the image
    // int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
    //
    // // Decode the image file into a Bitmap sized to fill the View
    // bmOptions.inJustDecodeBounds = false;
    // bmOptions.inSampleSize = scaleFactor;
    // bmOptions.inPurgeable = true;
    // return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    // }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need
     *                to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on
     * device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * This method converts device specific pixels to density independent
     * pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }


//    /**
//     * Check whats app is present.
//     *
//     * @param activity the activity
//     * @return true, if successful
//     */
//    public static boolean checkWhatsAppIsPresent(Activity activity) {
//        PackageManager pm = activity.getPackageManager();
//        boolean app_installed = false;
//        try {
//            pm.getApplicationInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
//            app_installed = true;
//        } catch (PackageManager.NameNotFoundException e) {
//            app_installed = false;
//        }
//        return app_installed;
//    }

    public static boolean isValidEmail(String hex) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getAspectBitmap(Context mContext, Bitmap bitmap, ImageView imageView, int backgroundImageID) {
        int resizeWidth = imageView.getWidth();
        int resizeHeight = imageView.getHeight();

        if ((resizeWidth == 0 || resizeHeight == 0) && backgroundImageID > 0) {
            Bitmap bitmap2 = BitmapFactory.decodeResource(mContext.getResources(), backgroundImageID);
            resizeWidth = bitmap2.getWidth();
            resizeHeight = bitmap2.getHeight();
        }
        if (resizeHeight > bitmap.getHeight())
            resizeHeight = bitmap.getHeight();
        if (resizeWidth > bitmap.getWidth())
            resizeWidth = bitmap.getWidth();
        double div_res = 0.0;

        if (bitmap.getHeight() > bitmap.getWidth()) {
            div_res = ((double) bitmap.getWidth() / (double) bitmap.getHeight());
            resizeWidth = (int) (div_res * resizeHeight);
        } else if (bitmap.getHeight() < bitmap.getWidth()) {
            div_res = ((double) bitmap.getHeight() / (double) bitmap.getWidth());
            resizeHeight = (int) (div_res * resizeWidth);
        }
        Bitmap bitmap_temp = getResizedBitmap(bitmap, resizeHeight, resizeWidth);
        if (bitmap_temp != null)
            bitmap = bitmap_temp;
        return bitmap;
    }

    public static Bitmap retrieveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 15)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
