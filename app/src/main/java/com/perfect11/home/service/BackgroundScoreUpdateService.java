package com.perfect11.home.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiClient2;
import com.perfect11.base.ApiInterface;
import com.perfect11.contest.dto.TeamDto;
import com.perfect11.contest.wrapper.TeamWrapper;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundScoreUpdateService extends Service {
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.perfect11.home.service.BackgroundReceiver";
    private final Handler handler = new Handler();
    private Intent intent;
    private int counter = 0;

    private String matchId, team_id;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (intent != null) {
            matchId = intent.getStringExtra("matchId");
            team_id = intent.getStringExtra("teamId");
        }
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            if (isOnline())
                runBackgroundOperation(matchId, team_id);
            handler.postDelayed(this, 3000); // 3 seconds
        }
    };

    private void displayLoggingInfo(TeamDto teamDto) {
        Log.d(TAG, "entered DisplayLoggingInfo");
        Bundle bundle = new Bundle();
        bundle.putSerializable("teamDto", teamDto);

        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    public void runBackgroundOperation(String matchId, String contestId) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<TeamWrapper> call = apiInterface.getPlayerLiveScore("325", "sltriseriest20_2018_g5");
        call.enqueue(new Callback<TeamWrapper>() {
            @Override
            public void onResponse(Call<TeamWrapper> call, Response<TeamWrapper> response) {
                TeamWrapper teamWrapper = response.body();

                Log.e("CreateTeamCallBack", "" + teamWrapper.data.size());
                displayLoggingInfo(teamWrapper.data.get(0));
//                if (callBackDto.status) {
//                    Log.e("CreateTeamCallBack", callBackDto.message);
//                    // callAPIJoinContest(callBackDto.data.team_id);
//                } else {
//                    DialogUtility.showMessageWithOk(callBackDto.message, getActivity());
//                }
//                if (mProgressDialog.isShowing())
//                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TeamWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
//                DialogUtility.showMessageWithOk(t.toString(), getActivity());
            }
        });
    }
}
