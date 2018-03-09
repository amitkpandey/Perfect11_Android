package com.webservice.interfaces;

// TODO: Auto-generated Javadoc

/**
 * The Interface IServerResponse.
 */
public interface IServerResponse {

    /**
     * On success.
     *
     * @param response the response
     */
    public void onSuccess(String response);

    /**
     * On failure.
     *
     * @param message   the message
     * @param errorCode the errorCode
     */
    public void onFailure(String message, String errorCode);

    /**
     * On failure.
     *
     * @param message the message
     * @param errorcode the errorcode
     */
//	public void onFailure(String message, String errorcode);

}
