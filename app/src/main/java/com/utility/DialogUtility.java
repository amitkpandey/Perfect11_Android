package com.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.perfect11.R;


public class DialogUtility {

    public static void showMessageWithOk(final String message, final Activity mActivity) {
        try {
            if (mActivity == null)
                return;
            mActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                    dialog.setMessage(message);
                    dialog.setTitle(R.string.app_name);
                    dialog.setNeutralButton(mActivity.getResources().getString(R.string.okay), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showMessageWithOk(final String title, final String message, final Activity mActivity) {
        if (mActivity == null)
            return;
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                dialog.setMessage(message);
                dialog.setTitle(title);
                dialog.setNeutralButton(mActivity.getResources().getString(R.string.okay), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }



    public static void showCustomDialogWithOk( final String message, final Activity mActivity,final AlertDialogCallBack callback)
    {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        TextView header = dialog.findViewById(R.id.ctv_title);
        TextView content = dialog.findViewById(R.id.ctv_body);
        Button cbtn_ok = dialog.findViewById(R.id.cbtn_ok);
        header.setText(R.string.app_name);
        content.setText(message);
        cbtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onSubmit();
            }
        });
//        dialog.show();
       mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });

    }


    public static void showMessageOkWithCallback(String message, Activity mContext, final AlertDialogCallBack callback) {
        if (mContext == null)
            return;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.app_name);
        dialog.setNegativeButton(mContext.getResources().getString(R.string.okay), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onSubmit();
            }
        });
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public static void showCallbackMessageWithAgree(String message, Activity mContext, final AlertDialogCallBack callback) {
        if (mContext == null)
            return;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(message);
        dialog.setTitle(R.string.app_name);
        dialog.setNegativeButton(mContext.getResources().getString(R.string.agree), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onSubmit();
            }
        });

        dialog.setPositiveButton(mContext.getResources().getString(R.string.do_not_agree), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onCancel();
            }
        });

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public static void showMessageAgreeWithCallback(String message, Activity mContext, final AlertDialogCallBack callback) {
        if (mContext == null)
            return;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.app_name);
        dialog.setNegativeButton(mContext.getResources().getString(R.string.terms_agree), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onSubmit();
            }
        });
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public static void showCallbackMessage(String message, Activity mContext, final AlertDialogCallBack callback) {
        if (mContext == null)
            return;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(message);
        dialog.setTitle(R.string.app_name);
        dialog.setNegativeButton(mContext.getResources().getString(R.string.yes), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onSubmit();
            }
        });

        dialog.setPositiveButton(mContext.getResources().getString(R.string.no), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onCancel();
            }
        });

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public static void showMessageWithOkCancelCallback(String message, Context mContext, final AlertDialogCallBack callback) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.app_name);

        dialog.setPositiveButton(mContext.getResources().getString(R.string.camera_cancel), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onCancel();

            }
        });

        dialog.setNegativeButton(mContext.getResources().getString(R.string.okay), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onSubmit();
            }
        });
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

}
