package com.perfect11.contest.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamDto implements Serializable {

    public String team_id;
    public String matchId;
    public String captain;
    public String vice_captain;
    public String usedCredit;
    public String createdby;
    public String addedon;
    public String modifedon;
    public String status;
    public ArrayList<TeamPlayerDto> team_player;
}
