package com.perfect11.upcoming_matches;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.perfect11.R;
import com.perfect11.base.ApiClient;
import com.perfect11.base.ApiInterface;
import com.perfect11.team_create.CreateTeamActivity;
import com.perfect11.upcoming_matches.adapter.UpcomingMatchesAdapter;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.perfect11.upcoming_matches.wrapper.UpComingMatchesWrapper;
import com.utility.ActivityController;
import com.utility.DialogUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingMatchesActivity extends AppCompatActivity {
    private RecyclerView rv_list;
    private UpcomingMatchesAdapter upcomingMatchesAdapter;
    private ApiInterface apiInterface;
    private UpComingMatchesWrapper upComingMatchesWrapper;
    private static final String TAG = UpcomingMatchesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_matches);
        initView();
        callAPI();
    }

    private void initView() {
        rv_list = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(UpcomingMatchesActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);

    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    private void callAPI() {
        //API
        Log.d("API", "UpcomingMatches");
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UpComingMatchesWrapper> call = apiInterface.getUpcomingMatches();
        call.enqueue(new Callback<UpComingMatchesWrapper>() {
            @Override
            public void onResponse(Call<UpComingMatchesWrapper> call, Response<UpComingMatchesWrapper> response) {
                upComingMatchesWrapper = response.body();
                Log.e("UpcomingMatchesAPI", upComingMatchesWrapper.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                upcomingMatchesAdapter = new UpcomingMatchesAdapter(upComingMatchesWrapper.data, UpcomingMatchesActivity.this);
                rv_list.setAdapter(upcomingMatchesAdapter);

                upcomingMatchesAdapter.setOnButtonListener(new UpcomingMatchesAdapter.OnButtonListener() {
                    @Override
                    public void onButtonClick(int position) {
                        Bundle bundle = new Bundle();
                        UpComingMatchesDto upComingMatchesDto = upComingMatchesWrapper.data.get(position);
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                            Date date;
                            date = sdf.parse(upComingMatchesDto.start_date);
                            long millis = date.getTime();
                            long hoursMillis = 60 * 60 * 1000;
                            long timeDiff = (millis - hoursMillis) - System.currentTimeMillis();
                            if (timeDiff > 0) {
                                bundle.putSerializable("upComingMatchesDto", upComingMatchesDto);
                                ActivityController.startNextActivity(UpcomingMatchesActivity.this, CreateTeamActivity.class, bundle,
                                        false);
                            }else
                            {
                                DialogUtility.showMessageWithOk("Match Closed",UpcomingMatchesActivity.this);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call<UpComingMatchesWrapper> call, Throwable t) {
                Log.e("TAG", t.toString());
                //DialogUtility.showMessageWithOk(t.toString(),UpcomingMatchesActivity.this);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
}
