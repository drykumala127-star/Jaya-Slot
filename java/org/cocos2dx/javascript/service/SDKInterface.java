package org.cocos2dx.javascript.service;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public interface SDKInterface {
    void init(Context context);

    void onActivityResult(int i, int i2, Intent intent);

    void onBackPressed();

    void onConfigurationChanged(Configuration configuration);

    void onDestroy();

    void onNewIntent(Intent intent);

    void onPause();

    void onRestart();

    void onRestoreInstanceState(Bundle bundle);

    void onResume();

    void onSaveInstanceState(Bundle bundle);

    void onStart();

    void onStop();

    void setGLSurfaceView(GLSurfaceView gLSurfaceView);
}
