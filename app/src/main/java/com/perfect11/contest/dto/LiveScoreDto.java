package com.perfect11.contest.dto;

import java.io.Serializable;

public class LiveScoreDto implements Serializable {

    public String player_short_name;

    public String points;

    public String playerRole;

    public String player_full_name;

    @Override
    public String toString() {
        return "LiveScoreDto{" +
                "player_short_name='" + player_short_name + '\'' +
                ", points='" + points + '\'' +
                ", playerRole='" + playerRole + '\'' +
                ", player_full_name='" + player_full_name + '\'' +
                '}';
    }
}
