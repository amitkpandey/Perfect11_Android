package com.webservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.perfect11.base.AppConstant;
import com.utility.Constants;
import com.webservice.interfaces.IServerResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

// TODO: Auto-generated Javadoc

/**
 * The Class RestServiceClient.
 */
public class RestServiceClient extends AsyncTask<Void, Void, String> {

    /**
     * The pd.
     */
    private ProgressDialog pd;

    /**
     * The i response.
     */
    private IServerResponse iResponse;

    /**
     * The values.
     */
    private Object[] values;

    /**
     * The file path.
     */
    private String[] filePath_Array;

    /**
     * The file path.
     */
    private String filePath;

    /**
     * The file path_video.
     */
    private String filePath_video;

    /**
     * The file path_image.
     */
    private String[] filePath_image;

    /**
     * The key for upload file.
     */
    private String keyForUploadFile = "";

    /**
     * The key for upload file image.
     */
    private String keyForUploadFileImage = "";

    /**
     * The key for upload file video.
     */
    private String keyForUploadFileVideo = "";

    /**
     * The keys.
     */
    private String[] keys;

    /**
     * The url.
     */
    private String url;

    private Activity activity;

    private int count;

    /**
     * The Constant ConnectionTimeout.
     */
    private static final int ConnectionTimeout = 200 * 1000;

    /**
     * The loading message.
     */
    private String loadingMessage = "";

    /**
     * The is enable multipart.
     */
    private boolean isEnableMultipart = false;

    private boolean isLoaderVisible = true;

    private int requestType;

    private boolean flag = false;
    private String serverErrorMessage = "Some Server Problem has been encountered";


    /**
     * Instantiates a new rest service client.
     *
     * @param iResponse the i response
     * @param values    the values
     * @param url       the url
     * @param activity  the activity
     */
    public RestServiceClient(IServerResponse iResponse, Object[] values, String[] keys, String url, Activity activity) {
        this.iResponse = iResponse;
        this.values = values;
        this.keys = keys;
        this.url = url;
        this.activity = activity;
        isLoaderVisible = true;
    }

    /**
     * Instantiates a new rest service client.
     *
     * @param iResponse        the i response
     * @param values           the values
     * @param keys             the keys
     * @param url              the url
     * @param activity         the activity
     * @param keyForUploadFile the key for upload file
     * @param filePath         the file path
     * @param loadingMessage   the loading message
     */
    public RestServiceClient(IServerResponse iResponse, Object[] values, String[] keys, String url, Activity activity,
                             String keyForUploadFile, String filePath, String loadingMessage) {
        this.iResponse = iResponse;
        this.values = values;
        this.keys = keys;
        this.url = url;
        this.activity = activity;
        this.filePath = filePath;
        this.keyForUploadFile = keyForUploadFile;
        this.loadingMessage = loadingMessage;
        isLoaderVisible = true;
    }

    /**
     * Instantiates a new rest service client.
     *
     * @param iResponse       the i response
     * @param values          the values
     * @param keys            the keys
     * @param url             the url
     * @param activity        the activity
     * @param still_path      the still_path
     * @param still_image_key the still_image_key
     * @param videoPath       the video path
     * @param video_path_key  the video_path_key
     * @param loadingMessage  the loding message
     */
    public RestServiceClient(IServerResponse iResponse, Object[] values, String[] keys, String url, Activity activity,
                             String[] still_path, String still_image_key, String videoPath, String video_path_key,
                             String loadingMessage) {
        this.iResponse = iResponse;
        this.values = values;
        this.keys = keys;
        this.url = url;
        this.activity = activity;
        this.filePath_image = still_path;
        this.keyForUploadFileImage = still_image_key;
        this.keyForUploadFileVideo = video_path_key;
        this.filePath_video = videoPath;
        this.loadingMessage = loadingMessage;
        isLoaderVisible = true;
    }

    /**
     * Instantiates a new rest service client.
     *
     * @param iResponse        the i response
     * @param values           the values
     * @param keys             the keys
     * @param url              the url
     * @param activity         the activity
     * @param keyForUploadFile the key for upload file
     * @param filePath         the file path
     */
    public RestServiceClient(IServerResponse iResponse, Object[] values, String[] keys, String url, Activity activity,
                             String keyForUploadFile, String filePath) {
        this.iResponse = iResponse;
        this.values = values;
        this.keys = keys;
        this.url = url;
        this.activity = activity;
        this.filePath = filePath;
        this.keyForUploadFile = keyForUploadFile;
        isLoaderVisible = true;
    }

    /**
     * Instantiates a new rest service client.
     *
     * @param iResponse        the i response
     * @param values           the values
     * @param keys             the keys
     * @param url              the url
     * @param activity         the activity
     * @param keyForUploadFile the key for upload file
     * @param filePath         the file path
     */
    public RestServiceClient(IServerResponse iResponse, Object[] values, String[] keys, String url, Activity activity,
                             String keyForUploadFile, String[] filePath) {
        this.iResponse = iResponse;
        this.values = values;
        this.keys = keys;
        this.url = url;
        this.activity = activity;
        this.filePath_Array = filePath;
        this.keyForUploadFile = keyForUploadFile;
        isLoaderVisible = true;
        this.requestType = AppConstant.HTTP_POST;
    }

    /**
     * Instantiates a new rest service client.
     *
     * @param iResponse      the i response
     * @param values         the values
     * @param keys           the keys
     * @param url            the url
     * @param activity       the activity
     * @param loadingMessage the loading message
     */
    public RestServiceClient(IServerResponse iResponse, Object[] values, String[] keys, String url, Activity activity,
                             String loadingMessage) {
        this.iResponse = iResponse;
        this.values = values;
        this.keys = keys;
        this.url = url;
        this.activity = activity;
        this.loadingMessage = loadingMessage;
        isLoaderVisible = true;
        this.requestType = AppConstant.HTTP_POST;
    }

    public RestServiceClient(IServerResponse iResponse, Object[] values, String[] keys, String url, Activity activity,
                             String loadingMessage, boolean flag) {
        this.iResponse = iResponse;
        this.values = values;
        this.keys = keys;
        this.url = url;
        this.activity = activity;
        this.loadingMessage = loadingMessage;
        isLoaderVisible = flag;
        this.requestType = AppConstant.HTTP_POST;
    }

    public RestServiceClient(IServerResponse iResponse, String url, Activity activity, String loadingMessage) {
        this.iResponse = iResponse;
        this.url = url;
        this.activity = activity;
        this.loadingMessage = loadingMessage;
        this.requestType = AppConstant.HTTP_GET;
    }

    public RestServiceClient(IServerResponse iResponse, String url, Activity activity, String loadingMessage, boolean flag) {
        this.iResponse = iResponse;
        this.url = url;
        this.activity = activity;
        this.loadingMessage = loadingMessage;
        this.requestType = AppConstant.HTTP_GET;
        this.flag = flag;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (!activity.isFinishing()) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd = ProgressDialog.show(activity, "", loadingMessage, false, false);
                        // pd.setContentView(R.layout.custume_progress_dailog);
//                            pd.setMessage(loadingMessage);
//                            pd.setCancelable(false);
//                            pd.show();
                    }
                });
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected String doInBackground(Void... params) {
        if (requestType == AppConstant.HTTP_POST)
//            return httpsJsonPost();
            return httpJsonPost();
        else if (requestType == AppConstant.HTTP_GET)
            return sendHTTPGetRequest();
        else
            return null;
    }

    /**
     * Read from asset.
     *
     * @return the string builder
     */
    @SuppressWarnings("deprecation")
    public StringBuilder readFromAsset() {
        StringBuilder returnString = new StringBuilder();
        String fileName = "services/" + url.substring(Constants.BASE_URL.length()) + ".txt";
        // System.out.println("file name "+fileName);
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = activity.getResources().getAssets().open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(String sResponse) {
        if (activity != null) {
            try {
                if (pd != null) {
                    pd.dismiss();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            System.out.println("flag " + flag);
            if (flag) {
                System.out.println("hello");
                if (iResponse != null && !sResponse.equals("")) {
                    iResponse.onSuccess(sResponse);
                } else {
                    if (iResponse != null)
                        iResponse.onFailure("", "0");
                }
            } else {
                if (iResponse != null && !sResponse.equals("")) {
                    JSONObject json;
                    try {
                        json = new JSONObject(sResponse);
                    /*String errorCode = json.optString("error");*/
                        boolean status = json.optBoolean("status");

                        System.out.println("status  " + status);
                        String msg = json.optString("message");
                        if (status) {
                            iResponse.onSuccess(sResponse);
                        } else {
                            iResponse.onFailure(msg, /*errorCode*/"0");
                        }
                    } catch (JSONException e) {
                        iResponse.onFailure(serverErrorMessage, "1");
                    }
                } else {
                    if (iResponse != null) {
                        iResponse.onFailure(serverErrorMessage, "0");
                    }
                }
            }
        }
    }

    /**
     * Adds the files in request entity.
     *
     * @param reqEntity        the req entity
     * @param keyForUploadFile the key for upload file
     * @param filePath         the file path
     */

    private void addFilesInRequestEntity(MultipartEntity reqEntity, String keyForUploadFile, String filePath) {
        System.out.println("RestClient: FilePath " + filePath);
        if (filePath != null && !"".equals(filePath)) {
            File file = new File(filePath);
            System.out.println("RestClient: File " + file);
            FileBody fileBodyVideo = new FileBody(file);
            reqEntity.addPart(keyForUploadFile, fileBodyVideo);
        }
    }

    private void addFilesInRequestEntity(MultipartEntity reqEntity, String keyForUploadFile, String[] filePath) {

        if (filePath != null && filePath.length > 0) {

            for (int i = 0; i < filePath.length; i++) {
                if (filePath[i] != null) {
                    File file = new File(filePath[i]);

                    FileBody fileBody = new FileBody(file);
                    reqEntity.addPart(keyForUploadFile + i, fileBody);
                    System.out.println(keyForUploadFile + i + " RestClient: File " + file);
                }
            }
        }
    }

    /**
     * Adds the files in request entity.
     *
     * @param reqEntity the req entity
     */
    private void addFilesInRequestEntity(MultipartEntity reqEntity) {
        if (filePath_image != null && filePath_image.length > 0) {

            for (int i = 0; i < filePath_image.length; i++) {
                if (filePath_image[i] != null) {
                    File file = new File(filePath_image[i]);

                    FileBody fileBodyVideo = new FileBody(file);
                    reqEntity.addPart(keyForUploadFileImage + i, fileBodyVideo);
                    System.out.println(keyForUploadFileImage + i + "   RestClient: File " + file);
                }
            }
        }
        if (filePath_video != null && filePath_video.length() > 0) {
            File file = new File(filePath_video);

            FileBody fileBodyVideo = new FileBody(file);
            reqEntity.addPart(keyForUploadFileVideo, fileBodyVideo);
            System.out.println(keyForUploadFileVideo + " RestClient: File " + file);
        }
    }

    private String sendHTTPGetRequest() {
        StringBuilder builderResponse = new StringBuilder();
        try {
            System.out.println("is called service " + this.url);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(this.url); // add request header
//        request.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            builderResponse = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                builderResponse.append(line);
            }
            Log.e("Response111: ", "Response111: " + builderResponse.toString());
//            System.out.println(builderResponse.toString());

//            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            serverErrorMessage = e.getMessage();
        }
        return builderResponse.toString();
    }

    /**
     * HTTP JSON post.
     *
     * @return the string
     */
    private String httpJsonPost() {
        String sResponse;

        StringBuilder builderResponse = new StringBuilder();
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParams = httpClient.getParams();
            httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, ConnectionTimeout);
            httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, ConnectionTimeout);
            HttpPost postRequest = new HttpPost(url);
//            postRequest.setHeader("Accept", "application/json");
//            postRequest.setHeader("Content-type", "application/json");
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (values != null && values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    Log.e("Data ", keys[i] + " " + values[i]);
                    if (values[i] instanceof String) {
                        reqEntity.addPart(keys[i], new StringBody(values[i].toString()));
                    } else if (values[i] instanceof Integer) {
                        reqEntity.addPart(keys[i], new StringBody(String.valueOf(values[i])));
                    } else if (values[i] instanceof File) {
                        addFilesInRequestEntity(reqEntity, keys[i], String.valueOf(values[i]));
                    }
                }
                postRequest.setEntity(reqEntity);
                Log.e("Request", "Request " + postRequest.getURI());
            }
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

            while ((sResponse = reader.readLine()) != null) {
                builderResponse = builderResponse.append(sResponse);
            }
            Log.e("Response111: ", "Response111: " + builderResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            serverErrorMessage = e.getMessage();
        }

        return builderResponse.toString();
    }

    /**
     * HTTPS JSON post.
     *
     * @return the string
     */
    private String httpsJsonPost() {
        String sResponse;

        StringBuilder builderResponse = new StringBuilder();
        try {
            HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            DefaultHttpClient client = new DefaultHttpClient();
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("http", socketFactory, 80));
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

//            HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParams = httpClient.getParams();
            httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, ConnectionTimeout);
            httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, ConnectionTimeout);
            HttpPost postRequest = new HttpPost(url);
//            postRequest.setHeader("Accept", "application/json");
//            postRequest.setHeader("Content-type", "application/json");
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (values != null && values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    Log.e("Data ", keys[i] + " " + values[i]);
                    if (values[i] instanceof String) {
                        reqEntity.addPart(keys[i], new StringBody(values[i].toString()));
                    } else if (values[i] instanceof Integer) {
                        reqEntity.addPart(keys[i], new StringBody(String.valueOf(values[i])));
                    } else if (values[i] instanceof File) {
                        addFilesInRequestEntity(reqEntity, keys[i], String.valueOf(values[i]));
                    }
                }
//                addFilesInRequestEntity(reqEntity, keyForUploadFile, filePath_Array);
                postRequest.setEntity(reqEntity);
                Log.e("Request", "Request " + postRequest.getURI());
            }
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

            while ((sResponse = reader.readLine()) != null) {
                builderResponse = builderResponse.append(sResponse);
            }
            Log.d("Response111: ", "Response111: " + builderResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            serverErrorMessage = e.getMessage();
        }

        return builderResponse.toString();
    }
}
