package com.fitbit.sampleandroidoauth2.Home;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.fitbit.sampleandroidoauth2.SharedPrefUtil;


public class HMStepsService extends Service implements SensorEventListener {

    SharedPrefUtil sharedPrefUtil;
    int step;
    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPrefUtil = new SharedPrefUtil();
        mSensorManager = (SensorManager)
                this.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            mStepDetectorSensor =
                    mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Sensor Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int step_fromPref = sharedPrefUtil.getSharedPreferenceInt(getApplicationContext(), "steps", 0);
        Log.e("TAG", "steps from preference: " + step_fromPref);
        step = step_fromPref + Math.round(event.values[0]);

        sharedPrefUtil.setSharedPreferenceInt(getApplicationContext(),"steps",step);
        Log.e("TAG", "steps to preference: " + step);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
