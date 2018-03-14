package com.perfect11.contest.wrapper;

import com.perfect11.contest.dto.TeamDto;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamWrapper implements Serializable {

    public String message;
    public String error;
    public String status;
    public ArrayList<TeamDto> data;
}
