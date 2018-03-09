package com.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import com.perfect11.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtilities {

    private Locale myLocale;

    public static boolean checkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isAvailable() && ni.isConnected();
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public static boolean isGreaterThanSDK19() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            return false;
        }
    }

    public static void taptoDial(String number, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        activity.startActivity(intent);
    }

    public static void taptoDial(String number, Activity activity, int RECOGCODE) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        activity.startActivityForResult(intent, RECOGCODE);
    }

    public static void sendEmail(String emailId, String subject, String body, Activity activity) {
        Intent mailer = new Intent(Intent.ACTION_SEND);
        //mailer.setType("vnd.android.cursor.item/email");
        mailer.setType("message/rfc822");
        mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
        mailer.putExtra(Intent.EXTRA_SUBJECT, subject);
        mailer.putExtra(Intent.EXTRA_TEXT, body);
        activity.startActivity(Intent.createChooser(mailer, subject));
    }


    public static String convertDateToMillisecond(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            java.util.Date date = simpleDateFormat.parse(dateStr);
            System.out.println("date " + date.toString());
            Timestamp timestamp = new Timestamp(date.getTime());
            // System.out.println("-- time "+timestamp.getSeconds());
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String convertMillisecondToDate(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            String date = simpleDateFormat.format(new Date(Long.parseLong(dateStr)));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Bitmap getBitmapFromURL(String map_icon) {
        try {
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL url = new URL(map_icon);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void doPhotoPrint(Activity activity, String name, String printImage) {
        PrintHelper photoPrinter = new PrintHelper(activity);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.setOrientation(PrintHelper.ORIENTATION_PORTRAIT);
//        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), printImageId);
        Bitmap bitmap = getBitmapFromURL(printImage);
        photoPrinter.printBitmap(name, bitmap);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void createWebPrintJob(WebView webView, Activity activity, String name) {
        List<PrintJob> mPrintJobs = new ArrayList<>();

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = activity.getString(R.string.app_name) + " " + name;
        PrintJob printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());

        // Save the job object for later status checking
        mPrintJobs.add(printJob);
    }

//	private void doPrint(Activity activity) {
//	    // Get a PrintManager instance
//	    PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);
//
//	    Set job name, which will be displayed in the print queue
//	    String jobName = activity.getString(R.string.app_name) + " Document";
//
//	    Start a print job, passing in a PrintDocumentAdapter implementation to handle the generation of a print document
//	    printManager.print(jobName, new MyPrintDocumentAdapter(getActivity()), null); //
//	}

    //
    public static boolean validateEmail(String emailID) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(emailID);
        return matcher.matches();
    }

    public static void hideKeyBord(EditText editText, Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static double distanceBtnLatLong(double sourcelat, double sourcelong, double destlat, double destlong) {
        double theta = sourcelong - destlong;
        double dist = Math.sin(deg2rad(sourcelat)) * Math.sin(deg2rad(destlat))
                + Math.cos(deg2rad(sourcelat)) * Math.cos(deg2rad(destlat)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
