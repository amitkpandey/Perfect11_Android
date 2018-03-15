package com.perfect11.account.wrapper;

import com.perfect11.account.dto.MyAccountDto;

import java.io.Serializable;

/**
 * Created by Developer on 14-03-2018.
 */

public class MyAccountWrapper implements Serializable {
    public MyAccountDto data;
    public String message;
    public String status;
}
