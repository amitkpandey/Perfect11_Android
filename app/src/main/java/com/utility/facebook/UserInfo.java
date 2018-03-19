package com.utility.facebook;

import java.io.Serializable;

// TODO: Auto-generated Javadoc

/**
 * The Class UserInfo.
 */
public class UserInfo implements Serializable {

    /**
     * Gets the email id.
     *
     * @return the email id
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * Sets the email id.
     *
     * @param emailId the new email id
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    //public String deviceId;
    //public String deviceToken;
    /**
     * The access token.
     */
    public String accessToken = "";

    /**
     * The facebook user id.
     */
    public String facebookUserId = "";

    /**
     * The email id.
     */
    public String emailId;
    //public String birthDate;
    /**
     * The first name.
     */
    public String firstName = "";

    /**
     * The last name.
     */
    public String lastName = "";
    //public String age;
    /**
     * The sex.
     */
    public String sex = "";

    /**
     * The profile image url.
     */
    public String profileImageUrl = "";

    /**
     * The link.
     */
    public String link = "";


    /**
     * Logout.
     */
    public void logout() {
        //birthDate = null;
        //emailId = null;
        accessToken = null;
    }

/*	public String getBirthDate() {
        return birthDate;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}*/

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param name the new first name
     */
    public void setFirstName(String name) {
        this.firstName = name;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

/*	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}*/

    /**
     * Gets the sex.
     *
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the sex.
     *
     * @param sex the new sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Gets the profile image url.
     *
     * @return the profile image url
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     * Sets the profile image url.
     *
     * @param profileImageUrl the new profile image url
     */
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

/*	public String getEmailId() {
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}*/

    /**
     * Gets the facebook user id.
     *
     * @return the facebook user id
     */
    public String getFacebookUserId() {
        return facebookUserId;
    }

    /**
     * Sets the facebook user id.
     *
     * @param facebookUserId the new facebook user id
     */
    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    /**
     * Gets the access token.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets the access token.
     *
     * @param accessToken the new access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
	
/*	public String getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceToken() {
		return deviceToken;
	}
	
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}*/


    /**
     * Gets the link.
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link.
     *
     * @param link the new link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "UserInfo [deviceId=" + "" + ", deviceToken="
                + "" + ", accessToken=" + accessToken
                + ", facebookUserId=" + facebookUserId + ", emailId=" + ""
                + ", birthDate=" + "" + ", firstName=" + firstName + ",lastName=" + lastName + " age="
                + "" + ", sex=" + sex + ", profileImageUrl=" + profileImageUrl
                + "]";
    }
}
