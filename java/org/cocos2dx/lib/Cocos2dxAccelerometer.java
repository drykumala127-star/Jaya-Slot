package org.cocos2dx.lib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Cocos2dxAccelerometer implements SensorEventListener {
    private static final String TAG = "Cocos2dxAccelerometer";
    private final Sensor mAcceleration;
    private final Sensor mAccelerationIncludingGravity;
    private final Context mContext;
    private final Sensor mGyroscope;
    private final SensorManager mSensorManager;
    private int mSamplingPeriodUs = 1;
    private b mDeviceMotionEvent = new b();

    class a {
        public float a = 0.0f;
        public float b = 0.0f;
        public float c = 0.0f;

        a() {
        }
    }

    class b {
        public a a;
        public a b;
        public c c;

        b() {
            this.a = Cocos2dxAccelerometer.this.new a();
            this.b = Cocos2dxAccelerometer.this.new a();
            this.c = Cocos2dxAccelerometer.this.new c();
        }
    }

    class c {
        public float a = 0.0f;
        public float b = 0.0f;
        public float c = 0.0f;

        c() {
        }
    }

    public Cocos2dxAccelerometer(Context context) {
        this.mContext = context;
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        this.mAcceleration = sensorManager.getDefaultSensor(1);
        this.mAccelerationIncludingGravity = sensorManager.getDefaultSensor(10);
        this.mGyroscope = sensorManager.getDefaultSensor(4);
    }

    public static native void onSensorChanged(float f, float f2, float f3, long j);

    public void disable() {
        this.mSensorManager.unregisterListener(this);
    }

    public void enable() {
        this.mSensorManager.registerListener(this, this.mAcceleration, this.mSamplingPeriodUs);
        this.mSensorManager.registerListener(this, this.mAccelerationIncludingGravity, this.mSamplingPeriodUs);
        this.mSensorManager.registerListener(this, this.mGyroscope, this.mSamplingPeriodUs);
    }

    public b getDeviceMotionEvent() {
        return this.mDeviceMotionEvent;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        if (type == 1) {
            a aVar = this.mDeviceMotionEvent.b;
            float[] fArr = sensorEvent.values;
            aVar.a = fArr[0];
            aVar.b = fArr[1];
            aVar.c = fArr[2];
            return;
        }
        if (type == 10) {
            a aVar2 = this.mDeviceMotionEvent.a;
            float[] fArr2 = sensorEvent.values;
            aVar2.a = fArr2[0];
            aVar2.b = fArr2[1];
            aVar2.c = fArr2[2];
            return;
        }
        if (type == 4) {
            this.mDeviceMotionEvent.c.a = (float) Math.toDegrees(sensorEvent.values[0]);
            this.mDeviceMotionEvent.c.b = (float) Math.toDegrees(sensorEvent.values[1]);
            this.mDeviceMotionEvent.c.c = (float) Math.toDegrees(sensorEvent.values[2]);
        }
    }

    public void setInterval(float f) {
        this.mSamplingPeriodUs = (int) (f * 1000000.0f);
        disable();
        enable();
    }
}
