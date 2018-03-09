package com.perfect11.team_create.dto;

import java.io.Serializable;

/**
 * Created by Developer on 02-03-2018.
 */

public class ContestDto implements Serializable {
    public String addedon;
    public String contestSize;
    public String entryfee;
    public String id;
    public String join_size;
    public String matchid;
    public String room_name;
    public String winningAmount;

    @Override
    public String toString() {
        return "ContestDto{" +
                "addedon='" + addedon + '\'' +
                ", contestSize='" + contestSize + '\'' +
                ", entryfee='" + entryfee + '\'' +
                ", id='" + id + '\'' +
                ", join_size='" + join_size + '\'' +
                ", matchid='" + matchid + '\'' +
                ", room_name='" + room_name + '\'' +
                ", winningAmount='" + winningAmount + '\'' +
                '}';
    }
}
