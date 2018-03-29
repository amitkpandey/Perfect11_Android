package com.perfect11.login_signup.dto;

import java.io.Serializable;

public class UserDto implements Serializable {

    public String member_id;
    public String first_name;
    public String last_name;
    public String email;
    public String phone;
    public String username;
    public String gender;
    public String address;
    public String address2;
    public String country;
    public String state;
    public String city;
    public String zipcode;
    public String social_id;
    public String device_token;
    public String device_type;
    public String weblink;
    public String picture;
    public String picture_sm;
    public String created_date;
    public String modified_date;
    public String created_by_type;
    public String created_by;
    public String status;
    public String status_modified_by;
    public String country_name;
    public String state_name;
    public String city_name;
    public String reference_id;
    public String total_balance;

    @Override
    public String toString() {
        return "UserDto{" +
                "member_id='" + member_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", address2='" + address2 + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", social_id='" + social_id + '\'' +
                ", device_token='" + device_token + '\'' +
                ", device_type='" + device_type + '\'' +
                ", weblink='" + weblink + '\'' +
                ", picture='" + picture + '\'' +
                ", picture_sm='" + picture_sm + '\'' +
                ", created_date='" + created_date + '\'' +
                ", modified_date='" + modified_date + '\'' +
                ", created_by_type='" + created_by_type + '\'' +
                ", created_by='" + created_by + '\'' +
                ", status='" + status + '\'' +
                ", status_modified_by='" + status_modified_by + '\'' +
                ", country_name='" + country_name + '\'' +
                ", state_name='" + state_name + '\'' +
                ", city_name='" + city_name + '\'' +
                '}';
    }

}
