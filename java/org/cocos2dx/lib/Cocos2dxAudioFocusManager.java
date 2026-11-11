package org.cocos2dx.lib;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

abstract class Cocos2dxAudioFocusManager {
    private static AudioManager.OnAudioFocusChangeListener a = new a();

    class a implements AudioManager.OnAudioFocusChangeListener {

        class a implements Runnable {
            a() {
            }

            @Override
            public void run() {
                Cocos2dxAudioFocusManager.a(1);
            }
        }

        class b implements Runnable {
            b() {
            }

            @Override
            public void run() {
                Cocos2dxAudioFocusManager.a(2);
            }
        }

        class c implements Runnable {
            c() {
            }

            @Override
            public void run() {
                Cocos2dxAudioFocusManager.a(3);
            }
        }

        class d implements Runnable {
            d() {
            }

            @Override
            public void run() {
                Cocos2dxAudioFocusManager.a(0);
            }
        }

        a() {
        }

        @Override
        public void onAudioFocusChange(int i) {
            Log.d("AudioFocusManager", "onAudioFocusChange: " + i + ", thread: " + Thread.currentThread().getName());
            if (i == -1) {
                Log.d("AudioFocusManager", "Pause music by AUDIOFOCUS_LOSS");
                Cocos2dxHelper.runOnGLThread(new a());
                return;
            }
            if (i == -2) {
                Log.d("AudioFocusManager", "Pause music by AUDIOFOCUS_LOSS_TRANSILENT");
                Cocos2dxHelper.runOnGLThread(new b());
            } else if (i == -3) {
                Log.d("AudioFocusManager", "Lower the volume, keep playing by AUDIOFOCUS_LOSS_TRANSILENT_CAN_DUCK");
                Cocos2dxHelper.runOnGLThread(new c());
            } else if (i == 1) {
                Log.d("AudioFocusManager", "Resume music by AUDIOFOCUS_GAIN");
                Cocos2dxHelper.runOnGLThread(new d());
            }
        }
    }

    class b implements Runnable {
        b() {
        }

        @Override
        public void run() {
            Cocos2dxAudioFocusManager.a(0);
        }
    }

    static void a(int i) {
        nativeOnAudioFocusChange(i);
    }

    static boolean b(Context context) {
        if (((AudioManager) context.getSystemService("audio")).requestAudioFocus(a, 3, 1) == 1) {
            Log.d("AudioFocusManager", "requestAudioFocus succeed");
            return true;
        }
        Log.e("AudioFocusManager", "requestAudioFocus failed!");
        return false;
    }

    static void c(Context context) {
        if (((AudioManager) context.getSystemService("audio")).abandonAudioFocus(a) == 1) {
            Log.d("AudioFocusManager", "abandonAudioFocus succeed!");
        } else {
            Log.e("AudioFocusManager", "abandonAudioFocus failed!");
        }
        Cocos2dxHelper.runOnGLThread(new b());
    }

    private static native void nativeOnAudioFocusChange(int i);
}
