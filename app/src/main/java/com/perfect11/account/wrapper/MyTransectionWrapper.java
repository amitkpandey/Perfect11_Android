package com.perfect11.account.wrapper;

import com.perfect11.account.dto.MyTransectionDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Developer on 20-03-2018.
 */

public class MyTransectionWrapper implements Serializable {
    public String message;
    public boolean status;
    public MyTransectionDataWrapper data;
}
