package com.perfect11.base;

import com.perfect11.account.wrapper.MyAccountWrapper;
import com.perfect11.account.wrapper.MyTransectionWrapper;
import com.perfect11.contest.dto.ContestCallBackDto;
import com.perfect11.contest.dto.LiveLeaderboardDto;
import com.perfect11.contest.wrapper.JoinedContestWrapper;
import com.perfect11.contest.wrapper.MyContestWrapper;
import com.perfect11.contest.wrapper.TeamWrapper;
import com.perfect11.home.dto.JoinContestCallBackDto;
import com.perfect11.home.wrapper.CreateTeamCallBackWrapper;
import com.perfect11.login_signup.dto.InviteDto;
import com.perfect11.login_signup.wrapper.PictureWrapper;
import com.perfect11.myprofile.wrapper.MyContestInfoWrapper;
import com.perfect11.payment.wrapper.TransactionWrapper;
import com.perfect11.payment.wrapper.WalletWrapper;
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
    Call<UpComingMatchesWrapper> getUpcomingMatches();

    @GET("api/getSchedule/0/0/completed")
    Call<UpComingMatchesWrapper> getCompletedMatches();

    @GET("api/getSchedule/0/0/started")
    Call<UpComingMatchesWrapper> getLiveMatches();

    @GET("api/getPlayers/{matchkey}")
    Call<PlayerWrapper> getPlayer(@Path("matchkey") String matchkey);

    @GET("api/getMyContests/{matchkey}/{userid}")
    Call<JoinedContestWrapper> getUserContest(@Path("matchkey") String matchkey, @Path("userid") String userid);

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
                                                  @Field("user_id") String user_id, @Field("team_name") String team_name);

    @FormUrlEncoded
    @POST("api/setTeam/{team_id}")
    Call<CreateTeamCallBackWrapper> updateTeamAPI(@Path("team_id") String team_id_path,
                                                  @Field("batsmandata[]") ArrayList<String> batsmanList, @Field("allrounderdata[]") ArrayList<String> allRounderList,
                                                  @Field("bowlerdata[]") ArrayList<String> bowlerList, @Field("keeperdata[]") ArrayList<String> keeperList,
                                                  @Field("captain") String captain, @Field("player_amount_count") float player_amount_count,
                                                  @Field("team_id") String team_id, @Field("vice_captain") String vice_captain,
                                                  @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("api/makeJoinContest")
    Call<JoinContestCallBackDto> joinContest(@Field("join_id") String join_id, @Field("reference_id") String reference_id, @Field("user_id")
            String user_id, @Field("matchID") String matchID, @Field("team_id") String team_id);

    @GET("api/getMyContests/{matchkey}/{userid}")
    Call<JoinedContestWrapper> getJoinedContestList(@Path("matchkey") String matchkey, @Path("userid") String userid);


    @GET("players/getpoints/{matchkey}/{roomid}")
    Call<ArrayList<LiveLeaderboardDto>> getLeaderBoardList(@Path("matchkey") String matchkey, @Path("roomid") String roomid);

    @FormUrlEncoded
    @POST("api/getMultipleTeam/{matchkey}")
    Call<TeamWrapper> getTeamList(@Path("matchkey") String matchkey, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("api/myAccount")
    Call<MyContestInfoWrapper> getMyContestDetails(@Field("member_id") String member_id);

    @GET("api/getteamData/{team_id}/{matchkey}")
    Call<TeamWrapper> getPlayerLiveScore(@Path("team_id") String team_id, @Path("matchkey") String matchkey);

    @FormUrlEncoded
    @POST("api/myAccountProfile")
    Call<MyAccountWrapper> getMyAccountDetails(@Field("member_id") String member_id);

    @GET("api/bannerlist")
    Call<PictureWrapper> getPictureList();

    @FormUrlEncoded
    @POST("api/inviteCode")
    Call<InviteDto> inviteCall(@Field("user_id") String member_id, @Field("invite_code") String invite_code);

    @FormUrlEncoded
    @POST("api/ticketSystem")
    Call<InviteDto> ticketSaveCall(@Field("user_id") String member_id, @Field("ticket_number") String ticket_number);

    @FormUrlEncoded
    @POST("api/joinContestByContestName")
    Call<InviteDto> joinContestByContestCode(@Field("user_id") String member_id, @Field("contestcode") String contestcode);

    @FormUrlEncoded
    @POST("api/myTransactionList")
    Call<MyTransectionWrapper> myTransactionList(@Field("user_id") String member_id, @Field("pageno") int pageno, @Field("perpage") int perpage);

    @FormUrlEncoded
    @POST("api/doCreateContest")
    Call<ContestCallBackDto> createContest(@Field("contestSize") int contestSize, @Field("customize_winning") int customize_winning,
                                           @Field("entryfee") float entryfee, @Field("join_multiple_item") int join_multiple_item,
                                           @Field("matchID") String matchID, @Field("room_name") String room_name,
                                           @Field("win_amt") ArrayList<Float> win_amt, @Field("win_per") ArrayList<Float> win_per,
                                           @Field("winner_set") int winner_set, @Field("winningAmount") int winningAmount,
                                           @Field("user_id") String user_id, @Field("reference_id") String reference_id,
                                           @Field("team_id") String team_id);

    @GET("api/getMyContests/0/{userid}")
    Call<MyContestWrapper> getMyContest(@Path("userid") String userid);

    @FormUrlEncoded
    @POST("api/addWallet")
    Call<WalletWrapper> addwallet(@Field("paymentId") String transactionId, @Field("amount") String amount, @Field("payment_from") String type,
                                  @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("api/paymentForJoinContest")
    Call<TransactionWrapper> paymentForJoinContest(@Field("contest_id") String contest_id, @Field("user_id") String user_id, @Field("payment_id") String paymentId,
                                                   @Field("payment_from") String type, @Field("payment_status") String status, @Field("payment_amount") String amount);

    @FormUrlEncoded
    @POST("api/paymentForCreateContest")
    Call<TransactionWrapper> paymentForCreateContest(@Field("join_id") String contest_id, @Field("user_id") String user_id, @Field("payment_id") String paymentId,
                                                   @Field("payment_from") String type, @Field("payment_status") String status, @Field("payment_amount") String amount);
}
