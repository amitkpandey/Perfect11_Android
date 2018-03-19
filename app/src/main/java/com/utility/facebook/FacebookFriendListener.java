package com.utility.facebook;

import java.util.List;

public interface FacebookFriendListener {
	
	void onCompleted(List<FBFriends_Model> app_user_friends/*, List<FBFriends_Model> taggable_user_friends*/);

	void onFailure();
}
