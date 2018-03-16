package com.perfect11.login_signup.wrapper;

import com.perfect11.login_signup.dto.PictureDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Developer on 16-03-2018.
 */

public class PictureWrapper implements Serializable {
    public ArrayList<PictureDto> data;
    public String message;
    public boolean status;
}
