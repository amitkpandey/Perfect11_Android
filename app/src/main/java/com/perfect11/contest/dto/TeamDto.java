package com.perfect11.contest.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamDto implements Serializable {

    public String team_name;
    public String team_id;
    public String matchId;
    public String captain;
    public String vice_captain;
    public String usedCredit;
    public String createdby;
    public String addedon;
    public String modifedon;
    public String status;
    public String teama;
    public String teamb;
    public String short_name;
    public ArrayList<TeamPlayerDto> team_player;

    @Override
    public String toString() {
        return "TeamDto{" +
                "team_id='" + team_id + '\'' +
                ", matchId='" + matchId + '\'' +
                ", captain='" + captain + '\'' +
                ", vice_captain='" + vice_captain + '\'' +
                ", usedCredit='" + usedCredit + '\'' +
                ", createdby='" + createdby + '\'' +
                ", addedon='" + addedon + '\'' +
                ", modifedon='" + modifedon + '\'' +
                ", status='" + status + '\'' +
                ", team_player=" + team_player.toString() +
                '}';
    }
}
