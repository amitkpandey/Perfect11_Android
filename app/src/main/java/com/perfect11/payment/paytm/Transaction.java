package com.perfect11.payment.paytm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transaction implements Serializable {
    @SerializedName("TXNID")
    @Expose
    public String tXNID;
    @SerializedName("BANKTXNID")
    @Expose
    public String bANKTXNID;
    @SerializedName("ORDERID")
    @Expose
    public String oRDERID;
    @SerializedName("TXNAMOUNT")
    @Expose
    public String tXNAMOUNT;
    @SerializedName("STATUS")
    @Expose
    public String sTATUS;
    @SerializedName("TXNTYPE")
    @Expose
    public String tXNTYPE;
    @SerializedName("GATEWAYNAME")
    @Expose
    public String gATEWAYNAME;
    @SerializedName("RESPCODE")
    @Expose
    public String rESPCODE;
    @SerializedName("RESPMSG")
    @Expose
    public String rESPMSG;
    @SerializedName("BANKNAME")
    @Expose
    public String bANKNAME;
    @SerializedName("MID")
    @Expose
    public String mID;
    @SerializedName("PAYMENTMODE")
    @Expose
    public String pAYMENTMODE;
    @SerializedName("REFUNDAMT")
    @Expose
    public String rEFUNDAMT;
    @SerializedName("TXNDATE")
    @Expose
    public String tXNDATE;
}
