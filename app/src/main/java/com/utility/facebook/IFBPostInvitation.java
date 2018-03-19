package com.utility.facebook;

import android.os.Bundle;

import com.facebook.FacebookException;
import com.facebook.GraphResponse;

import org.json.JSONObject;

import java.util.List;

public interface IFBPostInvitation {
	
	void receivedFBLoginDetails(JSONObject user, GraphResponse response);

	void onRequestComplete(Bundle values, FacebookException error);

	void getFriendsList(List<FBFriends_Model> friendsList);
}
