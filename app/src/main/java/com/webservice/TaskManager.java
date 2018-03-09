package com.webservice;

import android.app.Activity;
import android.content.Context;

import com.perfect11.R;
import com.utility.CommonUtilities;
import com.utility.Constants;
import com.utility.DialogUtility;
import com.webservice.interfaces.IRequestCaller;
import com.webservice.interfaces.IServerResponse;


// TODO: Auto-generated Javadoc

/**
 * The Class TaskManager.
 */
public class TaskManager implements IServerResponse {

    /**
     * The i request caller.
     */
    private IRequestCaller iRequestCaller;

    /**
     * The i server response.
     */
    private IServerResponse iServerResponse;

    /**
     * The activity.
     */
    private Activity activity;

    /**
     * The context.
     */
    private Context context;

    /* (non-Javadoc)
     * @see com.webservice.interfaces.IServerResponse#onSuccess(java.lang.String, java.lang.String)
     */
    @Override
    public void onSuccess(String response) {
        iServerResponse.onSuccess(response);
    }

    /* (non-Javadoc)
     * @see com.webservice.interfaces.IServerResponse#onFailure(java.lang.String, java.lang.String)
     */
    @Override
    public void onFailure(String message, String errorCode) {
        iServerResponse.onFailure(message, errorCode);
    }

    /**
     * Instantiates a new task manager.
     *
     * @param caller          the caller
     * @param iServerResponse the i server response
     * @param mActivity       the m activity
     */
    public TaskManager(IRequestCaller caller, IServerResponse iServerResponse, Activity mActivity) {
        iRequestCaller = caller;
        this.iServerResponse = iServerResponse;
        activity = mActivity;
    }

    /**
     * Instantiates a new task manager.
     *
     * @param caller          the caller
     * @param iServerResponse the i server response
     * @param mActivity       the m activity
     */
    public TaskManager(IRequestCaller caller, IServerResponse iServerResponse, Context mActivity) {
        iRequestCaller = caller;
        this.iServerResponse = iServerResponse;
        context = mActivity;
    }


    /**
     * Call service context.
     */
    public void callServiceContext() {
        if (CommonUtilities.checkConnectivity(context)) {
            String[] keys = iRequestCaller.getKeys();
            Object[] values = iRequestCaller.getValues();
            String url = Constants.BASE_URL + iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, null, "");
            restServiceClient.execute();
        } /*else {
            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), null);
		}*/
    }

    /**
     * Call service.
     *
     * @param keyForFileUpload the key for file upload
     * @param Path             the path
     * @param loadingMessage   the loadingMessage
     */
    public void callService(String keyForFileUpload, String Path, String loadingMessage) {
        if (CommonUtilities.checkConnectivity(activity)) {
            String[] keys = iRequestCaller.getKeys();
            Object[] values = iRequestCaller.getValues();
            String url = Constants.BASE_URL + iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, activity, keyForFileUpload, Path,
                    loadingMessage);
            restServiceClient.execute();
//        } else {
//            LinearLayout ll_image = activity.findViewById(R.id.ll_image);
//            ll_image.setVisibility(View.VISIBLE);
//            ImageView imageView = activity.findViewById(R.id.imagePregunta);
//            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }

    /**
     * Call service.
     *
     * @param still_path      the still_path
     * @param still_image_key the still_image_key
     * @param videoPath       the video path
     * @param video_path_key  the video_path_key
     * @param loadingMessage  the loadingMessage
     */
    public void callService(String[] still_path, String still_image_key, String videoPath, String video_path_key,
                            String loadingMessage) {
        if (CommonUtilities.checkConnectivity(activity)) {
            String[] keys = iRequestCaller.getKeys();
            Object[] values = iRequestCaller.getValues();
            String url = Constants.BASE_URL + iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, activity, still_path,
                    still_image_key, videoPath, video_path_key, loadingMessage);
            restServiceClient.execute();
//        } else {
//            LinearLayout ll_image = activity.findViewById(R.id.ll_image);
//            ll_image.setVisibility(View.VISIBLE);
//            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }

    /**
     * Call service context.
     *
     * @param still_path      the still_path
     * @param still_image_key the still_image_key
     * @param videoPath       the video path
     * @param video_path_key  the video_path_key
     * @param loadingMessage  the loadingMessage
     */
    public void callServiceContext(String[] still_path, String still_image_key, String videoPath, String video_path_key,
                                   String loadingMessage) {
        if (CommonUtilities.checkConnectivity(context)) {
            String[] keys = iRequestCaller.getKeys();
            Object[] values = iRequestCaller.getValues();
            String url = Constants.BASE_URL + iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, activity, still_path,
                    still_image_key, videoPath, video_path_key, loadingMessage);
            restServiceClient.execute();
//        } else {
//            LinearLayout ll_image = activity.findViewById(R.id.ll_image);
//            ll_image.setVisibility(View.VISIBLE);
//            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }

    /**
     * Call service.
     */
    public void callService() {
        if (CommonUtilities.checkConnectivity(activity)) {
            String[] keys = iRequestCaller.getKeys();
            Object[] values = iRequestCaller.getValues();
            String url = Constants.BASE_URL + iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, activity);
            restServiceClient.execute();
//        } else {
//            LinearLayout ll_image = activity.findViewById(R.id.ll_image);
//            ll_image.setVisibility(View.VISIBLE);
//            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }

    /**
     * Call service.
     *
     * @param loadingMessage the loading message
     */
    public void callService(String loadingMessage, boolean isLanding,String baseURL) {
        if (isLanding) {
            if (CommonUtilities.checkConnectivity(activity)) {
                String[] keys = iRequestCaller.getKeys();
                Object[] values = iRequestCaller.getValues();
                String url = baseURL+ iRequestCaller.getWebServiceMethod();
                RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, activity, loadingMessage);
                restServiceClient.execute();
            }
        } else {
            if (CommonUtilities.checkConnectivity(activity)) {
                String[] keys = iRequestCaller.getKeys();
                Object[] values = iRequestCaller.getValues();
                String url =  baseURL + iRequestCaller.getWebServiceMethod();
                RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, activity, loadingMessage);
                restServiceClient.execute();
            } else {
                DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
            }
        }
    }

//    public void callService(String loadingMessage, boolean isLanding) {
//        if (CommonUtilities.checkConnectivity(activity)) {
//            String[] keys = iRequestCaller.getKeys();
//            Object[] values = iRequestCaller.getValues();
//            String url = Constants.BASE_URL + iRequestCaller.getWebServiceMethod();
//            RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, activity, loadingMessage);
//            restServiceClient.execute();
//        } else {
//            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
//        }
//    }

    /**
     * Call service.
     *
     * @param keyForFileUpload the key for file upload
     * @param Path             the path
     */
    public void callService(String keyForFileUpload, String[] Path) {
        if (CommonUtilities.checkConnectivity(activity)) {
            String[] keys = iRequestCaller.getKeys();
            Object[] values = iRequestCaller.getValues();
            String url = Constants.BASE_URL + iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, activity, keyForFileUpload, Path);
            restServiceClient.execute();
        } else {
            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }

    /**
     * Call service.
     *
     * @param keyForFileUpload the key for file upload
     * @param Path             the path
     */
    public void callService(String keyForFileUpload, String Path) {
        if (CommonUtilities.checkConnectivity(activity)) {
            String[] keys = iRequestCaller.getKeys();
            Object[] values = iRequestCaller.getValues();
            String url = Constants.BASE_URL + iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, values, keys, url, activity, keyForFileUpload, Path);
            restServiceClient.execute();
        } else {
            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }

    public void callServiceForGet(String loadingMessage,String baseURL) {
        if (CommonUtilities.checkConnectivity(activity)) {
            String url = baseURL+ iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, url, activity, loadingMessage);
            restServiceClient.execute();
        } else {
            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }

    public void callServiceForGet(String loadingMessage, boolean isLanding,String baseURL) {
        if (CommonUtilities.checkConnectivity(activity)) {
            String url = baseURL + iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, url, activity, loadingMessage);
            restServiceClient.execute();
//        } else {
//            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }

    public void callServiceForGetVimeoThumbnail(String loadingMessage) {
        if (CommonUtilities.checkConnectivity(activity)) {
            String url = iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, url, activity, loadingMessage, true);
            restServiceClient.execute();
        } else {
            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }

    public void callServiceForGetVimeoThumbnail(String loadingMessage, boolean isLanding) {
        if (CommonUtilities.checkConnectivity(activity)) {
            String url = iRequestCaller.getWebServiceMethod();
            RestServiceClient restServiceClient = new RestServiceClient(this, url, activity, loadingMessage, true);
            restServiceClient.execute();
//        } else {
//            DialogUtility.showMessageWithOk(activity.getResources().getString(R.string.network_unavailable), activity);
        }
    }


//	/* (non-Javadoc)
//	 * @see com.webservice.interfaces.IServerResponse#onFailure(java.lang.String, java.lang.String, int)
//	 */
//	@Override
//	public void onFailure(String message, String errorCode, int count) {
//		iServerResponse.onFailure(message, errorCode, count);
//	}
}