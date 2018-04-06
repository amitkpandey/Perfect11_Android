package com.perfect11.base;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Developer on 23-02-2018.
 */

public class ApiClient {
    public static final String BASE_URL_RETROFIT = "https://perfect11.in/";
    public static Retrofit retrofit = null;
    public static HttpLoggingInterceptor interceptor = null;
    public static OkHttpClient client = null;
    public static Gson gson;

    /**
     * ThiS Class Will Give an Instance of Retrofit
     */
    public static Retrofit getApiClient() {

        if (interceptor == null) {
            interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        if (client == null) {
            client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        }

        if (gson == null) {
            gson = new GsonBuilder()
                    .setLenient()
                    .create();
        }
        Log.d("Retrofit", "Creating Instance");
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_RETROFIT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
