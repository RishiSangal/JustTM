package com.android.encypher.justtrackme.api;

import android.content.Context;
import android.provider.Settings;

import com.android.encypher.justtrackme.JustTrackMeApplication;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Raman Kumar on 3/19/2016.
 */
public abstract class Base extends BaseServiceable {

    ConnectionDetector connection;

    public final void addCommonHeaders() {
      //  boolean userLogin = false;
//        addHeader("X-DEVICE-ID", getAndroidDeviceId(SavanaApplication.getContext()));
//        addHeader("X-APPKEY", "sasefyweadfkdhaecommstreetinfoservicessiowedaflsdfjs");
//        addHeader("X-AUTHKEY", NkccServices.getUser().getAuth_key());
        connection = new ConnectionDetector(JustTrackMeApplication.getContext());
    }
    public static String getAndroidDeviceId(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    private JSONObject data;

    public JSONObject getData() {
        return data;
    }

    private String errorMessage;

    @Override
    public void onPostRun(int statusCode, String response) {
        try {
            data = new JSONObject(response);
            if (data.optString("success").contains("1")){
                isValidResponse = true;
            }
            else
                errorMessage = data.optString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        isValidResponse = SavanaHelper.isResponseValid(data);
//        if (!isValidResponse())
//            errorMessage = SavanaHelper.getErrorMesage(data);
//        data = data.optJSONObject("data");
//        if (data == null)
//            data = new JSONObject();
    }

    @Override
    public void runAsync(OnApiFinishListener onApiFinishListener) {
        if (connection.isConnectingToInternet())
            super.runAsync(onApiFinishListener);
        else{
//            Intent i = new Intent(SavanaApplication.getContext(), NoConnectionActivity.class);
//            SavanaApplication.getContext().startActivity(i);
        }
    }

    @Override
    public void onError(Call<ResponseBody> requestBodyCall, Throwable t) {
        errorMessage = t.getMessage();
    }

    private boolean isValidResponse;

    public boolean isValidResponse() {
        return isValidResponse;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
