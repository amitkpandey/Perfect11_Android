package com.perfect11.contest.wrapper;

import com.perfect11.contest.dto.JoinedContestDto;

import java.io.Serializable;
import java.util.ArrayList;

public class JoinedContestWrapper implements Serializable {
    public ArrayList<JoinedContestDto> data;
    public String message;
    public int error;
    public boolean status;

    @Override
    public String toString() {
        return "JoinedContestWrapper{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", error=" + error +
                ", status=" + status +
                '}';
    }
}
