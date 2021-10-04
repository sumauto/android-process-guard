package com.sumauto.guard;

import android.app.Application;
import android.content.Context;

import java.security.Guard;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SumautoGuard.attachDaemon(this);
    }
}
