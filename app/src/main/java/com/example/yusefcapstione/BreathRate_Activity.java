package com.example.yusefcapstione;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BreathRate_Activity extends AppCompatActivity implements SensorEventListener  {

    private Button button;


   @Override
   protected void onPause() {
       super.onPause();
       if (countDownTimer != null) {
           countDownTimer.cancel();
       }
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

        private String respiratoryRate = "0";
        private HashMap<String, String> symptoms = new HashMap<String, String>();
        private int buttonPressCount = 0;
        private long startTimeMillis = 0;
        private CountDownTimer countDownTimer;
        private TextView timerTextView;

        private SensorManager accelManage;
        private Sensor senseAccel;
        final private int MAX_COUNT = 220;
        float accelValuesX[] = new float[MAX_COUNT];
        float accelValuesY[] = new float[MAX_COUNT];
        float accelValuesZ[] = new float[MAX_COUNT];
        long time1;
        long time2;
        int total_time = 0;
        int x;
        int index = 0;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_breath_rate);
            configurePermissions();


            Button measureRespiratoryButton = (Button) findViewById(R.id.respRateButton);
            Button recordSymptoms = (Button) findViewById(R.id.record_symptoms);
            Button respiratoryRateButton = findViewById(R.id.manBTN);
            timerTextView = (TextView) findViewById(R.id.timerTextView);

            // Code for back button initialization and click listener
            button = findViewById(R.id.backBTN);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BreathRate_Activity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            // End of code for back button



            measureRespiratoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    measureRespiratoryRate();

                }
            });
            recordSymptoms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Intent intent = new Intent(BreathRate_Activity.this, GraphMain2.class);
                intent.putExtra("respiratoryRate", x); // calculatedRespiratoryRate is your float value
                startActivity(intent);
                }
            });
            respiratoryRateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Start timing on the first press
                    if (buttonPressCount == 0) {
                        startTimeMillis = System.currentTimeMillis();
                        startTimer(60000);
                    }
                    buttonPressCount++;


                    // Calculate the elapsed time in minutes
                    long currentTimeMillis = System.currentTimeMillis();
                    float elapsedTimeMinutes = (currentTimeMillis - startTimeMillis) / 60000.0f;

                    // Calculate the respiratory rate if at least one minute has passed
                    if (elapsedTimeMinutes >= 1) {
                        x = Math.round(buttonPressCount / elapsedTimeMinutes);
                        // Reset the count and timestamp
                        buttonPressCount = 0;
                        startTimeMillis = 0;

                        // Display or use the respiratory rate value
                        ((TextView) findViewById(R.id.respInstruction)).setText("Respiratory rate: " + x + " breaths per minute");

                    }
                }
            });




        }
    private void startTimer(long timeInMilliseconds) {
        countDownTimer = new CountDownTimer(timeInMilliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                // Update the timer TextView every second
                timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timerTextView.setText("00:00");
                // Optional: Code to be executed when the timer finishes
            }
        }.start();

    }

        void configurePermissions() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                            , 10);
                }
                return;
            }
        }

        private void measureRespiratoryRate() {
            ((TextView) findViewById(R.id.respInstruction)).setText("Calculating...");
            accelManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            senseAccel = accelManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            accelManage.registerListener(BreathRate_Activity.this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }


        public int getBreathRate() {
            if (total_time == 0)
                return 0;
            float xSum = 0, ySum = 0, zSum = 0;
            for (int i = 0; i < MAX_COUNT; i++) {
                xSum += accelValuesX[i];
                ySum += accelValuesY[i];
                zSum += accelValuesZ[i];
            }
            float xAvg = xSum / MAX_COUNT, yAvg = ySum / MAX_COUNT, zAvg = zSum / MAX_COUNT;
            int xCount = 0, yCount = 0, zCount = 0;
            for (int i = 1; i < MAX_COUNT; i++) {
                if ((accelValuesX[i - 1] <= xAvg && xAvg <= accelValuesX[i]) || (accelValuesX[i - 1] >= xAvg && xAvg >= accelValuesX[i]))
                    xCount++;
                if ((accelValuesY[i - 1] <= yAvg && yAvg <= accelValuesY[i]) || (accelValuesY[i - 1] >= yAvg && yAvg >= accelValuesY[i]))
                    yCount++;
                if ((accelValuesZ[i - 1] <= zAvg && zAvg <= accelValuesZ[i]) || (accelValuesZ[i - 1] >= zAvg && zAvg >= accelValuesZ[i]))
                    zCount++;
            }
            int max = Math.max(xCount, Math.max(yCount, zCount));

            return x = max * 30 / total_time;

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor eventSensor = event.sensor;

            if (eventSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if (index == 0)
                    time1 = System.currentTimeMillis();
                index++;
                accelValuesX[index] = event.values[0];
                accelValuesY[index] = event.values[1];
                accelValuesZ[index] = event.values[2];
                if (index >= MAX_COUNT - 1) {
                    time2 = System.currentTimeMillis();
                    index = 0;
                    total_time = (int) ((time2 - time1) / 1000);
                    accelManage.unregisterListener(this);

                    int breathRate = getBreathRate();

                    respiratoryRate = String.valueOf(breathRate);
                    Toast.makeText(getApplicationContext(), "Breathing rate is: " + respiratoryRate, Toast.LENGTH_LONG).show();

                    String currentValue = String.format(Locale.getDefault(), "Respiratory rate is: " + respiratoryRate);
                    ((TextView) findViewById(R.id.respInstruction)).setText(currentValue);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
