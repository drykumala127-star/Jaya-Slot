package org.cocos2dx.javascript.service;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public abstract class SDKClass implements SDKInterface {
    private Context mainActive = null;

    public Context getContext() {
        return this.mainActive;
    }

    @Override
    public void init(Context context) {
        this.mainActive = context;
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onNewIntent(Intent intent) {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onRestart() {
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void setGLSurfaceView(GLSurfaceView gLSurfaceView) {
    }
}
