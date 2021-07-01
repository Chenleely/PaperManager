package com.chaoshanshop.papermanager.net;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpUtils {
    private static OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
    private OkhttpUtils(){}

    public interface CallbackListener{
        public String onFinish(String response);
        public String onFailed();
    }

    public Request getRequestBuilder(String url){
        final Request mRequest=new Request.Builder()
                .url(url)
                .get()
                .build();
        return mRequest;
    }
    //get
    public static String doGet(Request request){
        final Call call=client.newCall(request);
        Response response=null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                response=client.newCall(request).execute();
            }
        }).start();
        if(response==null){
            return "Error";
        }
        return response.body().toString();
    }
    public String doAsyncGet(Request request,CallbackListener listener){
        final Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                listener.onFailed();
                Log.d("onGetfalied","falied");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                listener.onFinish(response.body().toString());
                Log.d("onGetfinish","finish");
            }
        });
    }
}
