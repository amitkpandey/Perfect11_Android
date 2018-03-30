package com.perfect11.payment.wrapper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.perfect11.contest.dto.JoinedContestDto;
import com.perfect11.payment.dto.WalletDto;

import java.io.Serializable;
import java.util.ArrayList;

public class WalletWrapper implements Serializable {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("data")
    @Expose
    public WalletDto data;
}
