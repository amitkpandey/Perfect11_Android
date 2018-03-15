package com.perfect11.team_create.dto;

import java.io.Serializable;

/**
 * Created by Developer on 02-03-2018.
 */

public class SelectedMatchDto implements Serializable {
    public int numberOfPlayer;
    public float credit_used;
    public String teamName1;
    public String teamName2;
    public int numberOfTeamName1;
    public int numberOfTeamName2;
    public boolean isEditing=false;
    public String team_id;
}
