package com.utility.facebook;

import java.io.Serializable;

public class FBFriends_Model implements Serializable {

    String FBF_id;
    String FBF_name;
    String FBF_url;
    String FBF_location;
    String FBF_image_url;
    boolean is_AppUser = false;
    boolean is_Selected = false;
    String totalDrop = "0";

    public String getFBF_name() {
        return FBF_name;
    }

    public void setFBF_name(String fBF_name) {
        FBF_name = fBF_name;
    }

    public String getFBF_url() {
        return FBF_url;
    }

    public void setFBF_url(String fBF_url) {
        FBF_url = fBF_url;
    }

    public String getFBF_location() {
        return FBF_location;
    }

    public void setFBF_location(String fBF_location) {
        FBF_location = fBF_location;
    }

    public boolean is_AppUser() {
        return is_AppUser;
    }

    public void set_AppUser(boolean is_AppUser) {
        this.is_AppUser = is_AppUser;
    }

    public boolean is_Selected() {
        return is_Selected;
    }

    public void set_Selected(boolean is_selected) {
        this.is_Selected = is_selected;
    }

    public String getFBF_id() {
        return FBF_id;
    }

    public void setFBF_id(String fBF_id) {
        FBF_id = fBF_id;
    }

    public String getFBF_image_url() {
        return FBF_image_url;
    }

    public void setFBF_image_url(String fBF_image_url) {
        FBF_image_url = fBF_image_url;
    }

    @Override
    public String toString() {
        return FBF_id + "," + FBF_name + "," + FBF_url + "\n";
    }


    public void setTotalDrop(String drop) {
        totalDrop = drop;
    }

    public String getTotalDrop() {
        return totalDrop;
    }

}
