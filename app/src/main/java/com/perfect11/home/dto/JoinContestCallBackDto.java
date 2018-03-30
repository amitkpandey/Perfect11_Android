package com.perfect11.home.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Developer on 07-03-2018.
 */

public class JoinContestCallBackDto implements Serializable {
    public String message;
    public int error;
    public boolean status;
    public String payment_required;
    public int amount_to_paid;
    public String contest_id;
    public String msg;
}
