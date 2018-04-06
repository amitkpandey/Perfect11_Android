package com.perfect11.base;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient2 {
    public static final String BASE_URL_RETROFIT = "http://perfect11.in:4201/";
    public static Retrofit retrofit = null;
    public static HttpLoggingInterceptor interceptor = null;
    public static OkHttpClient client = null;

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

        Log.d("Retrofit", "Creating Instance");
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_RETROFIT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
