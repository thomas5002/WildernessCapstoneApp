package com.example.yusefcapstione;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Locale;

public class GraphMain extends AppCompatActivity {

    // UI elements
    private Button button;
    private Button button2;
    EditText xInput, yInput;
    GraphView graph;
    TextInputLayout xInputLayout, yInputLayout;

    // Database variables
    LineGraphSeries<DataPoint> series;
    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        // Initialize UI elements
        button2 = findViewById(R.id.insertBTN);
        xInputLayout = findViewById(R.id.inputTextX);
        yInputLayout = findViewById(R.id.inputTextY);
        xInput = xInputLayout.getEditText();
        yInput = yInputLayout.getEditText();
        graph = findViewById(R.id.graph);

        // Initialize database helper and writable database
        myHelper = new MyHelper(this);
        sqLiteDatabase = myHelper.getWritableDatabase();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("heartRate")){
            double heartRate = intent.getDoubleExtra("heartRate",0);

            long currentTime = System.currentTimeMillis();
            double currentTimeMinutes = System.currentTimeMillis() / 60000.0;

            // Adjusted x-value using the app's start time as the baseline

            // Insert data into the database
            myHelper.insertData(currentTimeMinutes, (int) heartRate);

            // Load the graph data including the new entry
            loadGraphData();
        }

        // Initialize the clear data button
        Button clearButton = findViewById(R.id.clrBTN);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearGraphData();
            }
        });

        // Set onClickListener for back button
        button = findViewById(R.id.backBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphMain.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for insert button
        exqButton();
        // Load graph data when activity is created
        loadGraphData();

    }

    // Method to handle insert button click
    private void exqButton(){
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values, clean up and parse them
                String xValStr = xInput.getText().toString().replaceAll("[\n\\s]+", "").trim();
                String yValStr = yInput.getText().toString().replaceAll("[\n\\s]+", "").trim();
                int xVal = Integer.parseInt(xValStr);
                int yVal = Integer.parseInt(yValStr);

                // Insert data into database
                myHelper.insertData(xVal,yVal);

                // Reload graph data after new data insertion
                loadGraphData();
            }
        });
    }

    private void loadGraphData() {
        // Get data from database and create series
        series = new LineGraphSeries<>(getData());
        // Clear the graph before adding the new series
        graph.removeAllSeries();

        // Customize series appearance
        series.setColor(Color.RED);
        series.setThickness(8);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setAnimated(true);

        // Add series to the graph
        graph.addSeries(series);

        // Customize graph title
        graph.setTitle("Heart Rate (BPM) v.s. Time (min)");
        graph.setTitleColor(Color.BLUE);
        graph.setTitleTextSize(48);

        // Customize legend
        series.setTitle("Data Series");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setTextSize(26);

        // Make sure the graph's viewport can scroll and scale with the data
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);

        // Optionally, you can set the viewport to always include all the data points
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(series.getLowestValueX());
        graph.getViewport().setMaxX(series.getHighestValueX());

        // For the Y-axis, you can set it to automatically scale and include all data points
        graph.getViewport().setYAxisBoundsManual(false);



        // Set a custom label formatter to convert the timestamp values to a "minutes:seconds" string
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // Since we're using integers from 0 to 100 for the x-values, we can simply convert to int
                    return String.format(Locale.getDefault(), "%d", (int) value);
                } else {
                    // For y-values, we can use the default format
                    return super.formatLabel(value, false);
                }

            }
        });

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);


        // Set the number of horizontal labels (optional, for better control over the labels)
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // You might want to adjust this number

        // Clear any existing series
        graph.removeAllSeries();

        // Add the new series with updated data
        graph.addSeries(series);
    }

    // Method to retrieve data from database
    private DataPoint[] getData() {
        // Query database for x and y values
        Cursor cursor = sqLiteDatabase.query("MyTable", new String[]{"xValues", "yValues"}, null, null, null, null, null);
        // Initialize array to store data points
        DataPoint[] dp = new DataPoint[cursor.getCount()];

        // Iterate over cursor to populate data points array
        for (int i = 0; cursor.moveToNext(); i++) {
            dp[i] = new DataPoint(cursor.getDouble(0), cursor.getInt(1));
        }
        cursor.close();
        return dp;
    }

    private void clearGraphData() {
        // Clear the database table
        SQLiteDatabase db = myHelper.getWritableDatabase();
        db.delete("MyTable", null, null);

        // Clear the graph
        if (series != null) {
            series.resetData(new DataPoint[] {});
        }
        graph.removeAllSeries();

        // Optionally, show a message
        Toast.makeText(GraphMain.this, "Data cleared", Toast.LENGTH_SHORT).show();
    }

}