package com.perfect11.base;

import com.perfect11.contest.dto.LiveLeaderboardDto;
import com.perfect11.contest.wrapper.JoinedContestWrapper;
import com.perfect11.home.dto.JoinContestCallBackDto;
import com.perfect11.home.wrapper.CreateTeamCallBackWrapper;
import com.perfect11.team_create.wrapper.ContestWrapper;
import com.perfect11.team_create.wrapper.PlayerWrapper;
import com.perfect11.upcoming_matches.wrapper.UpComingMatchesWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Developer on 23-02-2018.
 */

public interface ApiInterface {

    @GET("api/getSchedule/0/0/notstarted")
    Call<UpComingMatchesWrapper> getUpcommingMatches();

    @GET("api/getSchedule/0/0/started")
    Call<UpComingMatchesWrapper> getLiveMatches();

    @GET("api/getPlayers/{matchkey}")
    Call<PlayerWrapper> getPlayer(@Path("matchkey") String matchkey);

    @FormUrlEncoded
    @POST("api/getAllContest/{matchkey}")
    Call<ContestWrapper> getContestList(@Path("matchkey") String matchkey, @Field("paySearch") String paySearch, @Field("sizeSearch") String sizeSearch,
                                        @Field("winSearch") String winSearch);

    @FormUrlEncoded
    @POST("api/setTeam")
    Call<CreateTeamCallBackWrapper> createTeamAPI(@Field("batsmandata[]") ArrayList<String> batsmanList, @Field("allrounderdata[]") ArrayList<String> allRounderList,
                                                  @Field("bowlerdata[]") ArrayList<String> bowlerList, @Field("keeperdata[]") ArrayList<String> keeperList,
                                                  @Field("captain") String captain, @Field("player_amount_count") float player_amount_count,
                                                  @Field("team_id") String team_id, @Field("vice_captain") String vice_captain,
                                                  @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("api/makeJoinContest")
    Call<JoinContestCallBackDto> joinContest(@Field("join_id") String join_id, @Field("reference_id") String reference_id, @Field("user_id")
            String user_id, @Field("matchID") String matchID);

    @GET("api/getMyContests/{matchkey}/{userid}")
    Call<JoinedContestWrapper> getJoinedContestList(@Path("matchkey") String matchkey, @Path("userid") String userid);


    @GET("players/getpoints/{matchkey}/{roomid}")
    Call<ArrayList<LiveLeaderboardDto>> getLeaderboardList(@Path("matchkey") String matchkey, @Path("roomid") String roomid);
}
