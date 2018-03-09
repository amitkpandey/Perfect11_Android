package com.perfect11.team_create.wrapper;

import com.perfect11.team_create.dto.ContestDto;
import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Developer on 26-02-2018.
 */

public class ContestWrapper implements Serializable {
    public ArrayList<ContestDto> data;
    public String message;
    public boolean status;

    @Override
    public String toString() {
        return "ContestWrapper{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
