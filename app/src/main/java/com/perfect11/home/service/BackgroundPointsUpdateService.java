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

import com.perfect11.base.ApiClient2;
import com.perfect11.base.ApiInterface;
import com.perfect11.contest.dto.LiveLeaderboardDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundPointsUpdateService extends Service {
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.perfect11.home.service.PointsUpdate";
    private final Handler handler = new Handler();
    private Intent intent;

    private String matchId, contestId;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (intent != null) {
            matchId = intent.getStringExtra("matchId");
            contestId = intent.getStringExtra("contestId");
        }
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            if (isOnline())
                runBackgroundOperation(matchId, contestId);
            handler.postDelayed(this, 10000); // 10 seconds
        }
    };

    private void publishLeaderBoardList(ArrayList<LiveLeaderboardDto> liveLeaderBoardDtoArrayList) {
        Log.d(TAG, "entered DisplayLoggingInfo");
        Bundle bundle = new Bundle();
        bundle.putSerializable("liveLeaderBoardDtoArrayList", liveLeaderBoardDtoArrayList);

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
        ApiInterface apiInterface = ApiClient2.getApiClient().create(ApiInterface.class);
        Call<ArrayList<LiveLeaderboardDto>> call = apiInterface.getLeaderBoardList(matchId, contestId);
        call.enqueue(new Callback<ArrayList<LiveLeaderboardDto>>() {
            @Override
            public void onResponse(Call<ArrayList<LiveLeaderboardDto>> call, Response<ArrayList<LiveLeaderboardDto>> response) {
                publishLeaderBoardList(response.body());
                System.out.println("hello");
            }

            @Override
            public void onFailure(Call<ArrayList<LiveLeaderboardDto>> call, Throwable t) {
                Log.e("TAG", t.toString());
            }
        });
    }
}
