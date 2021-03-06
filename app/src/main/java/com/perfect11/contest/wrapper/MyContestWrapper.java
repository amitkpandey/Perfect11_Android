
package com.perfect11.contest.wrapper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.perfect11.contest.dto.JoinedContestDto;

import java.io.Serializable;
import java.util.ArrayList;

public class MyContestWrapper implements Serializable {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("error")
    @Expose
    public Integer error;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public ArrayList<JoinedContestDto> data = null;

}
