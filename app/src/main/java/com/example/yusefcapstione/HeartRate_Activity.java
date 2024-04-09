package com.example.yusefcapstione;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import android.hardware.Camera;

public class HeartRate_Activity extends AppCompatActivity {



    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        turnOffFlash();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private Button button;


    private CameraManager cameraManager;
    private String cameraId;
    private Camera camera;
    private Camera.Parameters params;


    private static final int VIDEO_CAPTURE = 101;
    private static final int RATE = 26;
    String filePath;
    private double heartRate = 0;
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
    int total_time = 0;
    TextView logger;

    private  String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        configurePermissions();

        Button measureHeartRateButton = (Button) findViewById(R.id.heartRateButton);
        Button recordSymptoms = (Button) findViewById(R.id.record_symptoms);
        Button respiratoryRateButton = findViewById(R.id.manBTN);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        logger = (TextView) findViewById(R.id.heartRateInstruction);


        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0]; // Get the ID of the rear camera
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // New code for back button, needs to be within the onCreate function
        // Initialization and click listener, similar to other buttons
        button = findViewById(R.id.backBTN);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartRate_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // End of new code for the back button

        Intent intent = getIntent();
        String symptomsString = (String) intent.getStringExtra("SYMPTOMS");

        getSymptoms(symptomsString);

        if (!isCameraPermitted()) {
            measureHeartRateButton.setEnabled(false);
        }

        measureHeartRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //turnOnFlash();
                measureHeartRate();
            }
        });
        recordSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HeartRate_Activity.this, GraphMain.class);
                intent.putExtra("heartRate", heartRate); // calculatedRespiratoryRate is your float value
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
                    heartRate = Math.round(buttonPressCount / elapsedTimeMinutes);
                    // Reset the count and timestamp
                    buttonPressCount = 0;
                    startTimeMillis = 0;

                    // Display or use the respiratory rate value
                    ((TextView) findViewById(R.id.respInstruction)).setText("Heart rate: " + heartRate + " beats per minute");

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


    private boolean isCameraPermitted() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }

    private void turnOnFlash() {
        try {
            cameraManager.setTorchMode(cameraId, true); // Turn on the flash
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOffFlash() {
        try {
            cameraManager.setTorchMode(cameraId, false); // Turn off the flash
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
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
    private void getSymptoms(String symptomsString) {
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



        }
    }
    public int getHeartRate() {
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

    private void measureHeartRate() {
        //turnOnFlash();
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 45);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, VIDEO_CAPTURE);


    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // Handle the video capture result
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed
            }

            turnOffFlash(); // Turn off the flash when video capture is complete or cancelled
        }

        if (resultCode != RESULT_OK) return;

        Uri selectedImage = intent.getData();

        filePath = getPath(selectedImage);
        Log.d("Filepath", filePath);
        Toast.makeText(this, "Video Location is " + filePath, Toast.LENGTH_SHORT).show();

        new HeartRateAsync().execute();
    }

    //Calculating the heart rate in a separate thread so as to not block the UI
    private class HeartRateAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            turnOffFlash(); // Turn off the flash
            logger.setText(heartRate + " bpm");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Uri videoFileUri = Uri.parse(filePath);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath);

            MediaPlayer mp = MediaPlayer.create(getBaseContext(), videoFileUri);
            int time = mp.getDuration();

            int crpWidth = 50;
            int crpHeight = 50;

            ArrayList<Float> meanRedIntensity = new ArrayList<Float>();
            ArrayList<Float> diffList = new ArrayList<Float>();
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);

            for (int i = 0; i < time; i = i + 100) {
                int sum = 0;
                Bitmap croppedBitmap = ThumbnailUtils.extractThumbnail(retriever.getFrameAtTime(i * 1000, MediaMetadataRetriever.OPTION_CLOSEST), crpWidth, crpHeight);

                int[] px = new int[crpWidth * crpHeight];
                croppedBitmap.getPixels(px, 0, crpWidth, 0, 0, crpWidth, crpHeight);

                for (int j = 0; j < crpWidth * crpHeight; j++) {
                    int redIntensity = (px[j] & 0xff0000) >> 16;
                    sum = sum + redIntensity;
                }

                meanRedIntensity.add((float) sum / (crpWidth * crpHeight));

                float perc = (i / (float) time) * 100;
                String p = formatter.format(perc);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logger.setText(p + "% processed");
                    }
                });
            }

            for (int i = 0; i < meanRedIntensity.size() - 1; i++) {
                diffList.add(Math.abs(meanRedIntensity.get(i) - meanRedIntensity.get(i + 1)));
            }

            float noise = 0.1f;
            int peak = 0;

            //peak calulation
            for (int i = 1; i < diffList.size(); i++) {
                if (diffList.get(i) <= noise) {
                    ++peak;
                }
            }
            time /= 1000;
            heartRate = (60 * peak) / time + 20;

            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }






}


