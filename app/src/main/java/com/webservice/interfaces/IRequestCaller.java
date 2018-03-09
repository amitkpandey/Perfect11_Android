package com.webservice.interfaces;

// TODO: Auto-generated Javadoc
/**
 * The Interface IRequestCaller.
 */
public interface IRequestCaller {

	/**
	 * Gets the web service method.
	 *
	 * @return the web service method
	 */
	public String getWebServiceMethod();

	/**
	 * Gets the keys.
	 *
	 * @return the keys
	 */
	public String[] getKeys();

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public Object[] getValues();
	
	/**
	 * Call service.
	 */
	public void callService();
	
}
