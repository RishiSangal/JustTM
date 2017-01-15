package com.android.encypher.justtrackme.api;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public abstract class BaseServiceable {
    private HashMap<String, String> params;
    private static HashMap<String, String> headers;
    JSONObject _mjsonParams;
    HashMap<String, RequestBody> _mfiles;
    String _url;

    public BaseServiceable() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder request = original.newBuilder();
                for (Map.Entry<String, String> curr : getHeaders().entrySet())
                    request.header(curr.getKey(), curr.getValue());
                request.method(original.method(), original.body());
                return chain.proceed(request.build());
            }
        }).addNetworkInterceptor(logging)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.google.com")
                    .client(client)
                    .build();
            apiInterface = retrofit.create(APIInterface.class);
        }

        this._mfiles = new HashMap<>();
        headers = new HashMap<>();
        if (params == null)
            params = new HashMap<>();
        else
            params.clear();
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public enum REQUEST_TYPE {GET, POST, PUT, DELETE}

    public REQUEST_TYPE requestType = REQUEST_TYPE.GET;

    public final void setRequestType(REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }

    public final void setUrl(String url) {
        this._url = url;
    }

    public synchronized final void addHeader(String key, String value) {
        headers.remove(key);
        headers.put(key, value);
    }

    public final void addFile(String key, File file) {
        if (TextUtils.isEmpty(key) || file == null || !file.exists())
            return;
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        // MultipartBody.Part is used to send also the actual file name
//        body = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        _mfiles.put(key + "\"; filename=\"" + file.getName(), requestFile);
    }

    public final void addParam(String key, String value) {
        params.remove(key);
        params.put(key, value);
    }

    public final void addJson(String key, Object value) {
        if (this._mjsonParams == null)
            this._mjsonParams = new JSONObject();
        try {
            this._mjsonParams.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setJson(JSONObject jsonObject) {
        this._mjsonParams = jsonObject;
    }

    static Retrofit retrofit;
    static APIInterface apiInterface;

    public void runAsync(OnApiFinishListener onApiFinishListener) {
        this.onApiFinishListener = onApiFinishListener;
        Call<ResponseBody> responseBodyCall = null;
        switch (requestType) {
            case GET:
                responseBodyCall = apiInterface.getAPI(_url, params);
                break;
            case POST:
                if (_mjsonParams != null)
                    responseBodyCall = apiInterface.postJSONAPI(_url, _mjsonParams.toString());
                else {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue());
                        _mfiles.put(entry.getKey(), requestBody);
                    }
                    responseBodyCall = apiInterface.postAPI(_url, _mfiles);
                }
                break;
            case DELETE:
                if (_mjsonParams != null)
                    responseBodyCall = apiInterface.deleteJSONAPI(_url, _mjsonParams.toString());
                else {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue());
                        _mfiles.put(entry.getKey(), requestBody);
                    }
                    responseBodyCall = apiInterface.deleteAPI(_url, _mfiles);
                }
                break;
            case PUT:
                if (_mjsonParams != null)
                    responseBodyCall = apiInterface.putJSONAPI(_url, _mjsonParams.toString());
                else {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue());
                        _mfiles.put(entry.getKey(), requestBody);
                    }
                    responseBodyCall = apiInterface.putAPI(_url, _mfiles);
                }
                break;
        }
        if (responseBodyCall != null)
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    new AsyncParseResponse(call, response).execute();
                    BaseServiceable.this.requestCall = call;
                    BaseServiceable.this.response = response;
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    new AsyncParseResponse(call, t).execute();
                }
            });
    }

    public interface OnApiFinishListener<T extends BaseServiceable> {
        public void onComplete(T t);
    }

    Call<ResponseBody> requestCall;
    Response<ResponseBody> response;
    OnApiFinishListener onApiFinishListener;

    public final Call<ResponseBody> getRequest() {
        return requestCall;
    }

    public final Response<ResponseBody> getResponse() {
        return response;
    }

    public class AsyncParseResponse extends AsyncTask<Void, Void, Void> {
        Call<ResponseBody> call;
        Response<ResponseBody> response;
        Throwable t;

        public AsyncParseResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            this.call = call;
            this.response = response;
        }

        public AsyncParseResponse(Call<ResponseBody> call, Throwable t) {
            this.call = call;
            this.t = t;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (response != null) {
                    int code = response.code();
                    String data = response.isSuccessful() ? response.body().string() : response.errorBody().string();
                    Platform.get().log("<--RESPONSE \nURL: " + call.request().url() + "\nBODY: " + data + "\n<--END RESPONSE");
                    onPostRun(code, data);
                } else
                    onError(call, t);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (onApiFinishListener != null)
                onApiFinishListener.onComplete(BaseServiceable.this);
        }
    }

    public abstract void onPostRun(int statusCode, String response);

    public abstract void onError(Call<ResponseBody> requestBodyCall, Throwable t);
}
