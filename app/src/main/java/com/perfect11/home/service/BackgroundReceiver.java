package com.perfect11.home.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BackgroundReceiver extends BroadcastReceiver {

    public onMessageUpDateListener messageUpDateListener;

    public BackgroundReceiver() {

    }

    public BackgroundReceiver(onMessageUpDateListener messageUpDateListener) {
        this.messageUpDateListener = messageUpDateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    }

    public interface onMessageUpDateListener {
        public void onMessageUpdate();
    }

}
