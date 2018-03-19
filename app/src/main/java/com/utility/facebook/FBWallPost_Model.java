package com.utility.facebook;

public class FBWallPost_Model {

	String FBWallpost_id;
	String FBWallpost_UserName;
	String FBWallpost_PicUrl;
	String fBWallpost_Message;

	public String getFBWallpost_id() {
		return FBWallpost_id;
	}

	public void setFBWallpost_id(String fBWallpost_id) {
		FBWallpost_id = fBWallpost_id;
	}

	public String getFBWallpost_UserName() {
		return FBWallpost_UserName;
	}

	public void setFBWallpost_UserName(String fBWallpost_UserName) {
		FBWallpost_UserName = fBWallpost_UserName;
	}

	public String getFBWallpost_PicUrl() {
		return FBWallpost_PicUrl;
	}

	public void setFBWallpost_PicUrl(String fBWallpost_PicUrl) {
		FBWallpost_PicUrl = fBWallpost_PicUrl;
	}

	public String getfBWallpost_Message() {
		return fBWallpost_Message;
	}

	public void setfBWallpost_Message(String fBWallpost_Message) {
		this.fBWallpost_Message = fBWallpost_Message;
	}

	@Override
	public String toString() {
		return FBWallpost_id + " " + FBWallpost_UserName + " "
				+ FBWallpost_PicUrl + " " + fBWallpost_Message;
	}
}
