package com.perfect11.home.wrapper;

import com.perfect11.home.dto.TeamIDDto;

import java.io.Serializable;

/**
 * Created by Developer on 07-03-2018.
 */

public class CreateTeamCallBackWrapper implements Serializable {
    public TeamIDDto data;
    public String message;
    public int error;
    public boolean status;
}
