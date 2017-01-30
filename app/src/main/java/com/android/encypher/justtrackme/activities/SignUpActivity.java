package com.android.encypher.justtrackme.activities;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.api.BaseServiceable;
import com.android.encypher.justtrackme.api.RegistrationApi;
import com.android.encypher.justtrackme.common.ActivityManager;
import com.android.encypher.justtrackme.home.HomeActivity;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Rishi Sangal on 23-10-2016.
 */
public class SignUpActivity extends BaseActivity{

    TextView txtRegister;
    ImageView imgPickImage;
    Spinner countrySpinner;
    EditText edtUsername, edtFirstName, edtLastName, edtPhoneNo, edtPassword, edtEmail;
    RadioButton radMale, radFemale;

    String facebookId, googleId;
    boolean isSocialFaceLogin = false, isSocialGoogleLogin = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initalize();

        if (getIntent().hasExtra(SOCIAL_FACE_LOGIN_DATA)){
            isSocialFaceLogin = true;
            try {
                JSONObject object = new JSONObject(getIntent().getStringExtra(SOCIAL_FACE_LOGIN_DATA));
                facebookId = object.optString("id");

                edtUsername.setText(object.optString("name"));
                edtEmail.setText(object.optString("email"));
                if (object.optString("gender").contains("male")){
                    radMale.setChecked(true);
                }
                else {
                    radFemale.setChecked(true);
                }
                edtFirstName.setText(object.optString("first_name"));
                edtLastName.setText(object.optString("last_name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (getIntent().hasExtra(SOCIAL_GOOGLE_LOGIN_DATA)){
            isSocialGoogleLogin = true;
            JSONObject object = null;
            try {
                object = new JSONObject(getIntent().getStringExtra(SOCIAL_GOOGLE_LOGIN_DATA));
                googleId = object.optString("id");

                edtUsername.setText(object.optString("name"));
                edtEmail.setText(object.optString("email"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initalize() {
        imgPickImage = (ImageView) findViewById(R.id.imgPickImage);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        countrySpinner = (Spinner) findViewById(R.id.spinner);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtPhoneNo = (EditText) findViewById(R.id.edtPhoneNo);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        radMale = (RadioButton) findViewById(R.id.radMale);
        radFemale = (RadioButton) findViewById(R.id.radFemale);
        findViewById(R.id.txtLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getCountryCode();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivity.this, R.layout.simple_spinner_dropdown_item,
                data);
        countrySpinner.setAdapter(adapter);
        txtRegister.setOnClickListener(registerClick);
        imgPickImage.setOnClickListener(pickImageClick);
        countrySpinner.setOnItemSelectedListener(countrySelected);
    }

    private View.OnClickListener pickImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openImageChoiceDialoge();
        }
    };

    private View.OnClickListener registerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String username, firstName, lastName, phoneNo, password, email;
            username =  edtUsername.getText().toString();
            firstName = edtFirstName.getText().toString();
            lastName = edtLastName.getText().toString();
            phoneNo = edtPhoneNo.getText().toString();
            password = edtPassword.getText().toString();
            email = edtEmail.getText().toString();

            if (username.isEmpty()){
                showSnackBar(v, "Please enter Username");
                return;
            }
            if (firstName.isEmpty()){
                showSnackBar(v, "Please enter first name");
                return;
            }
            if (lastName.isEmpty()){
                showSnackBar(v, "Please enter last name");
                return;
            }
            if (phoneNo.isEmpty() || phoneNo.length()< 10){
                showSnackBar(v, "Please enter correct phone No");
                return;
            }
            if (password.length()< 6){
                showSnackBar(v, "Password length min 6 character");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                showSnackBar(v, "Please enter correct email");
                return;
            }

            showDialog();
            RegistrationApi register = new RegistrationApi(SignUpActivity.this);
            register.setUsername(username).setFirstName(firstName).setLastName(lastName)
                    .setCountry(country).setPhoneNo(phoneNo).setPassword(password)
                    .setEmail(email).setIsMale(radMale.isChecked()?"M":"F")
                    .setImage(currSeletctedImage);
            if (isSocialFaceLogin){
                register.setFacebookId(facebookId);
            }
            if (isSocialGoogleLogin){
                register.seGoogleId(googleId);
            }
            register.runAsync(RegisterUserApi);
        }
    };

    private BaseServiceable.OnApiFinishListener<RegistrationApi> RegisterUserApi = new BaseServiceable.OnApiFinishListener<RegistrationApi>() {
        @Override
        public void onComplete(RegistrationApi registrationApi) {
            dismissDialog();
            if (registrationApi.isValidResponse()){
                Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(i);
                ActivityManager.getInstance().clearStack();
                finish();
            }
            else {
                showToast(registrationApi.getErrorMessage());
            }
        }
    };

    String country;
    private AdapterView.OnItemSelectedListener countrySelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            country=parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    ArrayList<String> data = new ArrayList<>(233);
    public void getCountryCode(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("countries.dat"), "UTF-8"));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String str[]=line.split(",");
                data.add(str[0]+" (+"+str[2]+")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case Crop.REQUEST_PICK:
                Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis()));
//                			new Crop(data.getData()).output(outputUri).asSquare().start(this);
                Crop.of(data.getData(), outputUri).asSquare().start(SignUpActivity.this);
                break;
            case Crop.REQUEST_CROP:
                handleCrop(resultCode, data);
                break;
            case REQUEST_IMAGE_CAPTURE:
                showDialog();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Handler handler;
                        Runnable runnable;
                        handler = new Handler();
                        runnable = new Runnable() {
                            public void run() {
                                MediaScannerConnection.scanFile(SignUpActivity.this, new String[]{file.getAbsolutePath()}, null,
                                        new MediaScannerConnection.OnScanCompletedListener() {
                                            @Override
                                            public void onScanCompleted(final String path, final Uri uri) {

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dismissDialog();
                                                        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis()));
//                                                        new Crop(uri).output(outputUri).asSquare().start(SignUpActivity.this);
                                                        Crop.of(uri, outputUri).asSquare().start(SignUpActivity.this);
                                                    }
                                                });
                                            }
                                        });
                            }
                        };
                        handler.postDelayed(runnable, 3000);
                    }
                });
                break;
            default:
                break;
        }
    }

    File currSeletctedImage;
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            currSeletctedImage = new File(Crop.getOutput(result).getEncodedPath()+".jpg");
            imgPickImage.setImageURI(Crop.getOutput(result));
        } else {
            if (resultCode == Crop.RESULT_ERROR) {
                Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
