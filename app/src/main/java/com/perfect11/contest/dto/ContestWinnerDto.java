package com.perfect11.contest.dto;

import java.io.Serializable;

public class ContestWinnerDto implements Serializable {

    public int position;
    public float percentage;
    public float amount;

    @Override
    public String toString() {
        return "ContestWinnerDto{" +
                "position=" + position +
                ", percentage=" + percentage +
                ", amount=" + amount +
                '}';
    }
}
