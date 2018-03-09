package com.perfect11.upcoming_matches.dto;

import java.io.Serializable;

/**
 * Created by Developer on 23-02-2018.
 */

public class UpComingMatchesDto implements Serializable {
    public String format;
    public String id;
    public String key_name;
    public String matchrelated_name;
    public String matchstatus;
    public String season;
    public String short_name;
    public String start_date;
    public String teama;
    public String teamb;
    public long timeRemaining;

    @Override
    public String toString() {
        return "UpCommingMatchesDto{" +
                "format='" + format + '\'' +
                ", id='" + id + '\'' +
                ", key_name='" + key_name + '\'' +
                ", matchrelated_name='" + matchrelated_name + '\'' +
                ", matchstatus='" + matchstatus + '\'' +
                ", season='" + season + '\'' +
                ", short_name='" + short_name + '\'' +
                ", start_date='" + start_date + '\'' +
                ", teama='" + teama + '\'' +
                ", teamb='" + teamb + '\'' +
                '}';
    }
}
