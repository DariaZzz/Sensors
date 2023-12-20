package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetic;

    TextView xyAngle;
    TextView xzAngle;
    TextView zyAngle;

    private float[] rotationMatrix;//матрица поворота
    private float[] accel;
    private float[] magnet;
    private float[] orientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        xyAngle = findViewById(R.id.xy);
        xzAngle = findViewById(R.id.xz);
        zyAngle = findViewById(R.id.zy);

        rotationMatrix = new float[16];
        accel = new float[3];
        magnet = new float[3];
        orientation = new float[3];
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        loadSensorData(sensorEvent);
        SensorManager.getRotationMatrix(rotationMatrix, null, accel, magnet);
        SensorManager.getOrientation(rotationMatrix, orientation);

        xyAngle.setText(String.valueOf(Math.round(Math.toDegrees(orientation[0]))));
        xzAngle.setText(String.valueOf(Math.round(Math.toDegrees(orientation[1]))));
        zyAngle.setText(String.valueOf(Math.round(Math.toDegrees(orientation[2]))));
    }

    private void loadSensorData(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        if(type == Sensor.TYPE_ACCELEROMETER){
            accel = sensorEvent.values.clone();
        }
        if(type == Sensor.TYPE_MAGNETIC_FIELD){
            magnet = sensorEvent.values.clone();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}