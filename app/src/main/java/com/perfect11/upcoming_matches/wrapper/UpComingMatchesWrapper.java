package com.perfect11.upcoming_matches.wrapper;

import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;
import com.perfect11.upcoming_matches.dto.UpComingMatchesDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Developer on 23-02-2018.
 */

public class UpComingMatchesWrapper implements Serializable {
    public ArrayList<UpComingMatchesDto> data;
    public String counter;
    public String success;

    @Override
    public String toString() {
        return "UpComingMatchesWrapper{" +
                "data=" + data.toString() +
                ", counter='" + counter + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
