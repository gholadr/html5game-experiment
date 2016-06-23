package com.squar.html5games;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by gholadr on 6/22/16.
 */
public class MyApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Fresco.initialize(this);
    }

}
