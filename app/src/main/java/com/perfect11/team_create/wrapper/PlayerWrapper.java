package com.perfect11.team_create.wrapper;

import com.perfect11.team_create.dto.PlayerDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Developer on 26-02-2018.
 */

public class PlayerWrapper implements Serializable {
    public ArrayList<PlayerDto> data;
    public UpComingMatchesDto matchDetails;
    public String counter;
    public String success;

    @Override
    public String toString() {
        return "PlayerWrapper{" +
                "data=" + data.toString() +
                ", matchDetails=" + matchDetails.toString() +
                ", counter='" + counter + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
