package com.perfect11.team_create.dto;

import java.io.Serializable;

/**
 * Created by Developer on 26-02-2018.
 */

public class PlayerDto implements Serializable {
    public String createddate;
    public String credit;
    public String full_name;
    public String id;
    public String is_active;
    public String match_key;
    public String seasonal_role;
    public String short_name;
    public String team_code;

    public boolean isSelected=false;
    public boolean isC=false;
    public boolean isCV=false;
    public String titleHeader="";
    @Override
    public String toString() {
        return "PlayerDto{" +
                "createddate='" + createddate + '\'' +
                ", credit='" + credit + '\'' +
                ", full_name='" + full_name + '\'' +
                ", id='" + id + '\'' +
                ", is_active='" + is_active + '\'' +
                ", match_key='" + match_key + '\'' +
                ", seasonal_role='" + seasonal_role + '\'' +
                ", short_name='" + short_name + '\'' +
                ", team_code='" + team_code + '\'' +
                ", team_name='" + team_name + '\'' +
                '}';
    }

    public String team_name;
}
