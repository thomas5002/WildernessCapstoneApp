package com.example.yusefcapstione;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Locale;

public class BreathRate_Activity extends AppCompatActivity implements SensorEventListener  {

    private Button button;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private RecordSymptoms graphManager;
    private TextView tvBreathRate; // The TextView in your layout for displaying the breath rate

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView breathRateView;
    private EditText contactIdInput;
    private Button calculateButton, saveButton, bkBTN, recordSymptoms;
    private DatabaseTest database;

    private static final int VIDEO_CAPTURE = 101;
    private static final int RATE = 26;
    String filePath;
    private double heartRate = 0;
    private String respiratoryRate = "0";
    private HashMap<String, String> symptoms = new HashMap<String, String>();


    private SensorManager accelManage;
    private Sensor senseAccel;
    final private int MAX_COUNT = 220;
    float accelValuesX[] = new float[MAX_COUNT];
    float accelValuesY[] = new float[MAX_COUNT];
    float accelValuesZ[] = new float[MAX_COUNT];
    long time1;
    long time2;
    int total_time = 0;
    int index =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_rate); // Update with correct layout


        // Initialize your GraphManager here
        LineChart lineChart = findViewById(R.id.chart); // Make sure you have a LineChart with id 'chart' in your XML
        graphManager = new RecordSymptoms(this, lineChart);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //  tvBreathRate = findViewById(R.id.tvBreathRate);
        breathRateView = findViewById(R.id.respInstruction);
        tvBreathRate = findViewById(R.id.tvBreathRate);
        contactIdInput = findViewById(R.id.contactIdInput);
        calculateButton = findViewById(R.id.respRateButton);
        saveButton = findViewById(R.id.saveButton);
        recordSymptoms = (Button) findViewById(R.id.record_symptoms);
        EditText contactIdInput = findViewById(R.id.contactIdInput);
        database = new DatabaseTest(this);
        bkBTN = findViewById(R.id.backBTN);

        bkBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BreathRate_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int breathRate = calculateBreathRate();
                displayBreathRate(breathRate);
                measureRespiratoryRate();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBreathRate();
            }
        });
     /*   recordSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecordSymptoms();
            }
        }); */
    }

  /* private void exportDB() {
        File file = new File("/storage/self/primary/Download/covid_sym_db.csv");

        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = database.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM SymptomsTable",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(9), curCSV.getString(10), curCSV.getString(11), curCSV.getString(12), curCSV.getString(13)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    } */

   /* private void getSymptoms(String symptomsString) {
        if(symptomsString != null){
            symptomsString = symptomsString.substring(1);
            symptomsString = symptomsString.substring(0, symptomsString.length() - 1);

            String[] pairs = symptomsString.split(",");
            for (int i=0;i<pairs.length;i++) {
                String pair = pairs[i].trim();
                String[] keyValue = pair.split("=");
                symptoms.put(keyValue[0].trim(), keyValue[1].trim());
            }
            symptoms.put("Heart Rate", String.valueOf(heartRate));
            symptoms.put("Resp Rate", respiratoryRate);
            if (database.updateDbWithSymptoms(symptoms)) {
                exportDB();
                Toast.makeText(this, "DB updated", Toast.LENGTH_LONG).show();
            }

        }
    } */


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


        public int calculateBreathRate() {
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

            return max * 30 / total_time;

        }
   /* public void openRecordSymptoms() {
        boolean result = database.insertHeartRate(String.valueOf(heartRate), respiratoryRate);

        if (result) {
            Toast.makeText(this, "Heart rate and respiratory rate inserted successfully", Toast.LENGTH_LONG);
        }

        Intent intent = new Intent(this, GraphMain.class);
        startActivity(intent);
    } */

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

                    int breathRate = calculateBreathRate();

                    respiratoryRate = String.valueOf(breathRate);
                    Toast.makeText(getApplicationContext(), "Breathing rate is: " + respiratoryRate, Toast.LENGTH_LONG).show();

                    String currentValue = String.format(Locale.getDefault(), "Respiratory rate is: " + respiratoryRate);
                    ((TextView) findViewById(R.id.respInstruction)).setText(currentValue);
                }
            }
        }

    private void saveBreathRate() {
        try {
            String contactId = contactIdInput.getText().toString();
            if (contactId.isEmpty()) {
                Toast.makeText(this, "Contact ID is required.", Toast.LENGTH_SHORT).show();
                return;
            }

            int breathRate;
            // Extracting the numeric part of the breath rate from the correct TextView
            String breathRateText = tvBreathRate.getText().toString();
            String numericPart = breathRateText.replaceAll("[^\\d]", ""); // Remove any non-digit characters

            try {
                breathRate = Integer.parseInt(numericPart);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid breath rate.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Assuming saveBreathRate returns the row ID of the new entry or -1 on failure.
            long result = database.saveOrUpdateBreathRate(contactId, breathRate);

            if (result < 0) {
                Toast.makeText(this, "Failed to save breath rate.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Breath rate saved successfully.", Toast.LENGTH_SHORT).show();
                graphManager.updateGraph(); // Update the graph with the latest data
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void displayBreathRate(int breathRate) {
        tvBreathRate.setText("Breath Rate: " + breathRate);

    }



        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}



