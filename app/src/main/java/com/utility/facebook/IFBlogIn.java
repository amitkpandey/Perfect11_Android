package com.utility.facebook;

import com.facebook.GraphResponse;
import com.facebook.Profile;

import org.json.JSONObject;

public interface IFBlogIn {
	
	void receivedFBLoginDetails(Profile user, GraphResponse response);

	void getFriendsJSON(JSONObject friendsJSON);

	void publishEventsJSON(String eventsJSON);
}
