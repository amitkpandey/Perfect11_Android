
package com.perfect11.payment.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionDto implements Serializable {

    @SerializedName("member_id")
    @Expose
    public String memberId;
    @SerializedName("amount")
    @Expose
    public Integer amount;
    @SerializedName("payment_id")
    @Expose
    public String paymentId;
    @SerializedName("payment_request_id")
    @Expose
    public String paymentRequestId;
    @SerializedName("date_created")
    @Expose
    public String dateCreated;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("payment_from")
    @Expose
    public String paymentFrom;

}
