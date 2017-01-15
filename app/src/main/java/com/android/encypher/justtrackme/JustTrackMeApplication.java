package com.android.encypher.justtrackme;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import net.ralphpina.permissionsmanager.PermissionsManager;

import java.io.File;

/**
 * Created by Raman Kumar on 3/2/2016.
 */
public class JustTrackMeApplication extends Application {
    private static Context mContext;
//    private ImageLoaderConfiguration config;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        PermissionsManager.init(this);
        initializeImgaeLoader();
    }

    private void initializeImgaeLoader() {
        File cacheDir = new File(Environment.getExternalStorageDirectory(), "trustyoo/cache");
//        config = new ImageLoaderConfiguration.Builder(this)
//                .diskCache(new UnlimitedDiskCache(cacheDir))
//                .threadPriority(Thread.MAX_PRIORITY)
//                .defaultDisplayImageOptions(getDefaultOptions())
//                .build();
//        ImageLoader.getInstance().init(config);
    }

//    private DisplayImageOptions getDefaultOptions() {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.app_icon)
//                .showImageForEmptyUri(R.drawable.app_icon)
//                .showImageOnFail(R.drawable.app_icon)
//                .cacheInMemory(true) // default
//                .considerExifParams(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .build();
//        return options;
//    }

    public static Context getContext() {
        return mContext;
    }
}
