package com.android.encypher.justtrackme.api;

import android.app.Activity;

import com.android.encypher.justtrackme.common.JustTrackMeConstant;

import org.json.JSONObject;

import java.io.File;


/**
 * Created by ecomm-rishi on 7/15/2015.
 */
public class RegistrationApi extends Base {


    public RegistrationApi(Activity context) {
        setUrl(JustTrackMeConstant.SignUp);
        setRequestType(REQUEST_TYPE.POST);
        addCommonHeaders();
    }

    public RegistrationApi setEmail(String email){
        addParam("email", email);
        return this;
    }
    public RegistrationApi setPassword(String password){
        addParam("password", password);
        return this;
    }
    public RegistrationApi setPhoneNo(String phoneNo){
        addParam("mobile", phoneNo);
        return this;
    }
    public RegistrationApi setFirstName(String firstName) {
        addParam("fname", firstName);
        return this;
    }

    public RegistrationApi setLastName(String lastName) {
        addParam("lname", lastName);
        return this;
    }

    public RegistrationApi setGcmId(String id){
        addParam("gcm_id", id);
        return this;
    }

    public RegistrationApi setUsername(String id){
        addParam("username", id);
        return this;
    }
    public RegistrationApi setIsMale(String is_member) {
        addParam("gender", is_member);
        return this;
    }

    public RegistrationApi setCountry(String is_notification) {
        addParam("country", is_notification);
        return this;
    }

    public RegistrationApi setImage(File is_member) {
        addFile("image", is_member);
        return this;
    }

    public RegistrationApi setFacebookId(String facebookId) {
        setUrl(JustTrackMeConstant.SocialSignUp);
        addParam("fb_id", facebookId);
        return this;
    }

    public RegistrationApi seGoogleId(String googleId) {
        setUrl(JustTrackMeConstant.SocialSignUp);
        addParam("gp_id", googleId);
        return this;
    }

    @Override
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()){
            JSONObject object = getData().optJSONObject("user");
            if (object == null)
                object = new JSONObject();
//            NkccServices.saveUserData(object.toString());
        }
    }

}
