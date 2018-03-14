package com.perfect11.contest.dto;


import java.io.Serializable;

public class TeamPlayerDto implements Serializable {

    public String lteam_list_id;
    public String team_id;
    public String matchId;
    public String player;
    public String type;
    public String credit;
    public String team_code;
    public String points_gain;
    public String full_name;

    @Override
    public String toString() {
        return "TeamPlayerDto{" +
                "lteam_list_id='" + lteam_list_id + '\'' +
                ", team_id='" + team_id + '\'' +
                ", matchId='" + matchId + '\'' +
                ", player='" + player + '\'' +
                ", type='" + type + '\'' +
                ", credit='" + credit + '\'' +
                ", team_code='" + team_code + '\'' +
                ", points_gain='" + points_gain + '\'' +
                ", full_name='" + full_name + '\'' +
                '}';
    }
}
