
package com.perfect11.contest.dto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyContestDto implements Serializable
{

    @SerializedName("join_id")
    @Expose
    public String joinId;
    @SerializedName("contest_code")
    @Expose
    public String contestCode;
    @SerializedName("team_id")
    @Expose
    public String teamId;
    @SerializedName("matchID")
    @Expose
    public String matchID;
    @SerializedName("contestId")
    @Expose
    public String contestId;
    @SerializedName("createdby")
    @Expose
    public String createdby;
    @SerializedName("reference_id")
    @Expose
    public String referenceId;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("addedon")
    @Expose
    public String addedon;
    @SerializedName("teamscore")
    @Expose
    public String teamscore;
    @SerializedName("payment_id")
    @Expose
    public String paymentId;
    @SerializedName("paid")
    @Expose
    public String paid;
    @SerializedName("winingamount")
    @Expose
    public String winingamount;
    @SerializedName("winingrank")
    @Expose
    public String winingrank;
    @SerializedName("paymentstatus")
    @Expose
    public String paymentstatus;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("tournament")
    @Expose
    public String tournament;
    @SerializedName("room_name")
    @Expose
    public String roomName;
    @SerializedName("entryfee")
    @Expose
    public String entryfee;
    @SerializedName("winningAmount")
    @Expose
    public String winningAmount;
    @SerializedName("contestSize")
    @Expose
    public String contestSize;
    @SerializedName("matchid")
    @Expose
    public String matchid;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("created_by")
    @Expose
    public String createdBy;
    @SerializedName("addedfor")
    @Expose
    public String addedfor;

}
