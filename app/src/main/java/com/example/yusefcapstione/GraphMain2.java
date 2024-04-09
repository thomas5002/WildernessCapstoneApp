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

public class GraphMain2 extends AppCompatActivity {

    // UI elements
    private Button button;
    private Button button2;
    private static final long appStartTime = System.currentTimeMillis() / 60000;
    EditText xInput2, yInput2;
    GraphView graph2;
    TextInputLayout xInputLayout2, yInputLayout2;

    // Database variables
    LineGraphSeries<DataPoint> series2;
    MyHelper2 myHelper2;
    SQLiteDatabase sqLiteDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        // Initialize UI elements
        button2 = findViewById(R.id.insertBTN);
        xInputLayout2 = findViewById(R.id.inputTextX);
        yInputLayout2 = findViewById(R.id.inputTextY);
        xInput2 = xInputLayout2.getEditText();
        yInput2 = yInputLayout2.getEditText();
        graph2 = findViewById(R.id.graph2);

        // Initialize database helper and writable database
        myHelper2 = new MyHelper2(this);
        sqLiteDatabase2 = myHelper2.getWritableDatabase();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("respiratoryRate")){
            int respiratoryRate = intent.getIntExtra("respiratoryRate",0);

            long currentTime = System.currentTimeMillis();
            double currentTimeMinutes = System.currentTimeMillis() / 60000.0;

            // Adjusted x-value using the app's start time as the baseline

            // Insert data into the database
            myHelper2.insertData(currentTimeMinutes, respiratoryRate);

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
                Intent intent = new Intent(GraphMain2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for insert button
        exqButton();
        // Load graph data when activity is created
        loadGraphData();
    }

    // Method to handle insert button click
    private void exqButton() {
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String xValStr2 = xInput2.getText().toString().replaceAll("[\n\\s]+", "").trim();
                String yValStr2 = yInput2.getText().toString().replaceAll("[\n\\s]+", "").trim();
                int xVal2 = Integer.parseInt(xValStr2);
                int yVal2 = Integer.parseInt(yValStr2);

                // Insert data into database
                myHelper2.insertData(xVal2, yVal2);

                // Reload graph data after new data insertion
                loadGraphData();
            }
        });
    }

    // Method to load graph data from database
    private void loadGraphData() {
        // Get data from database and create series
        series2 = new LineGraphSeries<>(getData());
        // Clear the graph before adding the new series
        graph2.removeAllSeries();

        // Customize series appearance
        series2.setColor(Color.RED);
        series2.setThickness(8);
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10);
        series2.setAnimated(true);

        // Add series to the graph
        graph2.addSeries(series2);

        // Customize graph title
        graph2.setTitle("Breath Rate (BPM) v.s. Time (min)");
        graph2.setTitleColor(Color.BLUE);
        graph2.setTitleTextSize(48);

        // Customize legend
        series2.setTitle("Data Series");
        graph2.getLegendRenderer().setVisible(true);
        graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph2.getLegendRenderer().setTextSize(26);

        // Make sure the graph's viewport can scroll and scale with the data
        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScalableY(true);
        graph2.getViewport().setScrollable(true);
        graph2.getViewport().setScrollableY(true);

        // Optionally, you can set the viewport to always include all the data points
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setMinX(series2.getLowestValueX());
        graph2.getViewport().setMaxX(series2.getHighestValueX());

        // For the Y-axis, you can set it to automatically scale and include all data points
        graph2.getViewport().setYAxisBoundsManual(false);



        // Set a custom label formatter to convert the timestamp values to a "minutes:seconds" string
        graph2.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
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

        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(100);


        // Set the number of horizontal labels (optional, for better control over the labels)
        graph2.getGridLabelRenderer().setNumHorizontalLabels(4); // You might want to adjust this number

        // Clear any existing series
        graph2.removeAllSeries();

        // Add the new series with updated data
        graph2.addSeries(series2);
    }

    // Method to retrieve data from database
    private DataPoint[] getData() {
        // Query database for x and y values
        Cursor cursor = sqLiteDatabase2.query("MyTable2", new String[]{"xValues2", "yValues2"}, null, null, null, null, null);
        // Initialize array to store data points
        DataPoint[] dp = new DataPoint[cursor.getCount()];

        // Iterate over cursor to populate data points array
        for (int i = 0; cursor.moveToNext(); i++) {
            dp[i] = new DataPoint(cursor.getDouble(0), cursor.getInt(1));
        }
        // Close the cursor after use
        cursor.close();
        return dp;
    }

    private void clearGraphData() {
        // Clear the database table
        SQLiteDatabase db = myHelper2.getWritableDatabase();
        db.delete("MyTable2", null, null);

        // Clear the graph
        if (series2 != null) {
            series2.resetData(new DataPoint[] {});
        }
        graph2.removeAllSeries();

        // Optionally, show a message
        Toast.makeText(GraphMain2.this, "Data cleared", Toast.LENGTH_SHORT).show();
    }
}