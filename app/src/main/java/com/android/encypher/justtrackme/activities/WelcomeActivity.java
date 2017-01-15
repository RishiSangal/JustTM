package com.android.encypher.justtrackme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.encypher.justtrackme.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Rishi Sangal on 23-10-2016.
 */

public class WelcomeActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    CallbackManager callBack;
    GoogleSignInOptions gso;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callBack = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callBack, fbLogin);

        setContentView(R.layout.activity_welcome);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        initalize();
    }

    GoogleApiClient mGoogleApiClient;
    Button register, login, butFacebook;
    SignInButton butGoogle;
    private void initalize() {

        register = (Button) findViewById(R.id.signupPhone);
        login = (Button) findViewById(R.id.signlogin);
        butFacebook = (Button) findViewById(R.id.butFacebook);
        butGoogle = (SignInButton) findViewById(R.id.butGoogle);
        butGoogle.setSize(SignInButton.SIZE_STANDARD);
        butGoogle.setScopes(gso.getScopeArray());

        butGoogle.setOnClickListener(googleClick);
        butFacebook.setOnClickListener(facebookClick);
        register.setOnClickListener(registerClick);
        login.setOnClickListener(loginClick);
    }

    private View.OnClickListener facebookClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoginManager.getInstance().logInWithReadPermissions(WelcomeActivity.this,
                    Arrays.asList("email", "public_profile"));
        }
    };
    private View.OnClickListener googleClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    };

    private View.OnClickListener registerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(i);
        }
    };

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(i);
        }
    };

    FacebookCallback<LoginResult> fbLogin = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(final LoginResult result) {
            final AccessToken accessToken = result.getAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(
                    result.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted( JSONObject object,
                                GraphResponse response) {
                            Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
                            i.putExtra(SOCIAL_FACE_LOGIN_DATA,response.getJSONObject().toString());
                            startActivity(i);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday,first_name,last_name");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Toast.makeText(WelcomeActivity.this, "Cancel", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(WelcomeActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callBack.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount acct = result.getSignInAccount();
                JSONObject object = new JSONObject();
                try {
                    object.put("id", acct.getId());
                    object.put("name", acct.getDisplayName());
                    object.put("email", acct.getEmail());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
                i.putExtra(SOCIAL_GOOGLE_LOGIN_DATA, object.toString());
                startActivity(i);
            }
        }
    }
}
