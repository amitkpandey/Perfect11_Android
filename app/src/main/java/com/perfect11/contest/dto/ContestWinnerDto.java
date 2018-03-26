package com.perfect11.contest.dto;

import java.io.Serializable;

public class ContestWinnerDto implements Serializable {

    public int poistion;
    public float percentage;
    public float amount;

    @Override
    public String toString() {
        return "ContestWinnerDto{" +
                "poistion=" + poistion +
                ", percentage=" + percentage +
                ", amount=" + amount +
                '}';
    }
}
