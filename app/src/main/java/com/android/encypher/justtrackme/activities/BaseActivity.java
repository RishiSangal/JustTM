package com.android.encypher.justtrackme.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.common.AlertDialogeBuilder;
import com.android.encypher.justtrackme.common.ICommonData;
import com.soundcloud.android.crop.Crop;

import net.ralphpina.permissionsmanager.PermissionsManager;

import java.io.File;
import java.util.Vector;

/**
 * Created by Rishi.Sangal on 10/21/2016.
 */
public class BaseActivity extends AppCompatActivity implements ICommonData{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getSupportActionBar().hide();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        progress = new Vector<>();
    }

    TextView txtHeaderName, txtHeaderBack;
    public void setHeader(){
        txtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        txtHeaderBack = (TextView) findViewById(R.id.txtHeaderBack);

        txtHeaderBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public  static Toast toas;
    public void showToast(final String toast){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toas != null) {
                    toas.makeText(BaseActivity.this, toast, Toast.LENGTH_SHORT).show();
                } else {
                    toas = new Toast(BaseActivity.this);
                    toas.makeText(BaseActivity.this, toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showSnackBar(View v, String msg){
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    Vector<ProgressDialog> progress;
    public void showDialog() {
        showDialog("Loading please wait");
    }
    public void showDialog(String message) {
        progress.add(ProgressDialog.show(this, "", "Loading please wait", true, false));
    }

    public void dismissDialog() {
        if (progress != null)
            for (ProgressDialog prog : progress)
                prog.dismiss();
    }

    public File file;
    public final void openImageChoiceDialoge() {
        AlertDialogeBuilder alert = new AlertDialogeBuilder(BaseActivity.this, new AlertDialogeBuilder.AlertListeners() {
            @Override
            public void Negative() {
                if (!PermissionsManager.get().isCameraGranted()){
                    PermissionsManager.get().requestCameraPermission(BaseActivity.this);
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File folder = new File(Environment.getExternalStorageDirectory() + File.separator + JUST_TRACK_FOLDER);
                if (!folder.exists()) folder.mkdir();
                file = new File(Environment.getExternalStorageDirectory() + File.separator + JUST_TRACK_FOLDER
                        + File.separator + System.currentTimeMillis() + "image.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                GetBaseActivity().startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }

            @Override
            public void Positive() {
                Crop.pickImage(GetBaseActivity());
            }
        });
        alert.setMessage(getResources().getString(R.string.image_option));
        alert.setPositiveButton(getResources().getString(R.string.gallery));
        alert.setNegativeButton(getResources().getString(R.string.camera));
        alert.createAlert();
    }

    public BaseActivity GetBaseActivity(){
        return this;
    }



}
