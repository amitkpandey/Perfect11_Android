
package com.perfect11.payment.wrapper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.perfect11.payment.dto.TransactionDto;

import java.io.Serializable;

public class TransactionWrapper implements Serializable {

    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("error")
    @Expose
    public Integer error;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public TransactionDto data;

}
