package com.utility.facebook;

import com.facebook.GraphResponse;
import com.facebook.Profile;

import java.util.List;

public interface FBInterface extends IFBlogIn {

    void receiveFBFriendsList(List<Profile> users, GraphResponse response);

    void getWallPostResponse(GraphResponse response);

    void getCommentsAgainstPost(GraphResponse response);

    void getFBCheckInResponse(GraphResponse response);

}
