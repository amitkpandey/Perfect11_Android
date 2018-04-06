package com.perfect11.team_create.dto;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Developer on 02-03-2018.
 */

public class ContestDto implements Serializable {
    public String addedfor;
    public String addedon;
    public String contestSize;
    public String createdby;
    public String created_by;
    public String entryfee;
    public String id;
    public String join_size;
    public String matchid;
    public String paid;
    public String payment_id;
    public String room_name;
    public String status;
    public String tournament;
    public String winningAmount;
    public ArrayList<ContestSubDto> sub_data;

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
