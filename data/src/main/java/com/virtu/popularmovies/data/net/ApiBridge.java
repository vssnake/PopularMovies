package com.virtu.popularmovies.data.net;

import android.app.DownloadManager;
import android.support.annotation.Nullable;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by virtu on 09/07/2015.
 */
@Singleton
public class ApiBridge implements Callable<String> {

    private static final String CONTENT_TYPE_HEADER_NAME = "Content_Type";
    private static final String CONTENT_TYPE_HEADER_VALUE = "application/json; charset= utf-8";

    private HttpUrl mURL;
    private String mApiResponse;

    OkHttpClient okHTTPClient;

    public ApiBridge(){
        okHTTPClient = this.createClient();
    }

    public boolean setURL(String url){
        this.mURL = HttpUrl.parse(url);
        boolean mUrlMalformed = this.mURL == null ?  true :  false;
        return mUrlMalformed;
    }


    @Nullable
    public String requestSyncCall() {
        if (this.mURL != null){
            connectApiSync();
            return mApiResponse;
        }
        return null;
    }

    public void requestAsyncCall(final ResponseStringCallback stringCallback){
        if (this.mURL != null){
            connectApiAsync(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Response response) {
                    try {
                        stringCallback.onStringResponse(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void connectApiSync(){
        final Request request = new Request.Builder()
                .url(mURL)
                .addHeader(CONTENT_TYPE_HEADER_NAME,CONTENT_TYPE_HEADER_VALUE)
                .get()
                .build();

        try{
            this.mApiResponse = okHTTPClient.newCall(request).execute().body().string();
        }catch (IOException exception){
            exception.printStackTrace();
        }


    }
    private void connectApiAsync(Callback callback){
        final Request request = new Request.Builder()
                .url(mURL)
                .addHeader(CONTENT_TYPE_HEADER_NAME,CONTENT_TYPE_HEADER_VALUE)
                .get()
                .build();
            okHTTPClient.newCall(request).enqueue(callback);

    }

    private OkHttpClient createClient() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);

        return okHttpClient;
    }

    @Override
    public String call() throws Exception {
        return requestSyncCall();
    }


    public interface ResponseStringCallback{
        public abstract void onStringResponse(String body);
    }



}
