package com.perfect11.account.wrapper;

import com.perfect11.account.dto.MyTransectionDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Developer on 20-03-2018.
 */

public class MyTransectionDataWrapper implements Serializable {
    public String balanceavailable;
    public int listcount;
    public ArrayList<MyTransectionDto> shopkeeperlist;
}
