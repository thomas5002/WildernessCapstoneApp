package com.example.yusefcapstione;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecordSymptoms extends AppCompatActivity {

    private Button button;
    private static LineChart lineChart;
    private static DatabaseTest database;

    public RecordSymptoms(Context context, LineChart chart) {
        this.lineChart = chart;
        this.database = new DatabaseTest(context);
    }

    public static void updateGraph() {
        List<Entry> entries = new ArrayList<>();

        Cursor cursor = database.getAllBreathRates();
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexId = cursor.getColumnIndex(DatabaseTest.DatabaseHelper.COLUMN_ID);
            int columnIndexRate = cursor.getColumnIndex(DatabaseTest.DatabaseHelper.BREATH_RATE);



            if(columnIndexId == -1 || columnIndexRate == -1) {
                // If either of the column indexes are -1, log an error or throw an exception
                Log.e("GraphManager", "Invalid column name");
                cursor.close();
                return;
            }

            // Now that we've checked the column indexes, we can read from the cursor
            do {
                float xValue = cursor.getFloat(columnIndexId);
                float yValue = cursor.getFloat(columnIndexRate);
                entries.add(new Entry(xValue, yValue));
            } while (cursor.moveToNext());
            cursor.close();

            // Create a dataset and give it a type (if you're reusing the dataset, clear the previous data)
            LineDataSet dataSet = new LineDataSet(entries, "Breath Rate");
            // ... rest of your code to configure the dataset and refresh the chart
        } else if (cursor != null) {
            cursor.close();
        }

        Cursor cursor2 = database.getAllHeartRates();
        if (cursor2 != null && cursor2.moveToFirst()) {
            int columnIndexId = cursor2.getColumnIndex(DatabaseTest.DatabaseHelper.COLUMN_ID);
            int columnIndexRate = cursor2.getColumnIndex(DatabaseTest.DatabaseHelper.HEART_RATE);



            if(columnIndexId == -1 || columnIndexRate == -1) {
                // If either of the column indexes are -1, log an error or throw an exception
                Log.e("GraphManager", "Invalid column name");
                cursor2.close();
                return;
            }

            // Now that we've checked the column indexes, we can read from the cursor
            do {
                float xValue = cursor2.getFloat(columnIndexId);
                float yValue = cursor2.getFloat(columnIndexRate);
                entries.add(new Entry(xValue, yValue));
            } while (cursor2.moveToNext());
            cursor2.close();

            // Create a dataset and give it a type (if you're reusing the dataset, clear the previous data)
            LineDataSet dataSet = new LineDataSet(entries, "Heart Rate");
            // ... rest of your code to configure the dataset and refresh the chart
        } else if (cursor2 != null) {
            cursor2.close();
        }

        // Create a dataset and give it a type (if you're reusing the dataset, clear the previous data)
        LineDataSet dataSet = new LineDataSet(entries, "Breath Rate");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        // Create a data object with the dataset and set it to the chart
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged(); // let the chart know it's data changed
        lineChart.invalidate(); // refresh the chart


        // Configure chart settings
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);

        // Customize x-axis (e.g., set the position of the x-axis, formatting, etc.)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawLabels(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);


        // Add any other chart customization here

        // Refresh the chart
        lineChart.invalidate();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        // New code for back button initialization and click listener
        button = findViewById(R.id.backBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordSymptoms.this, MainActivity.class);
                startActivity(intent);
            }
        });



        //Get Graph from layout xml
    /*    GraphView graph1 = (GraphView)  findViewById(R.id.graph);
        GraphView graph2 = (GraphView)  findViewById(R.id.graph2);

        //Get Data Points
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();


        //Add Series to Graph
        graph1.addSeries(series);
        graph2.addSeries(series2);

        //Set Series 1 Attributes
        series.setColor(Color.RED);
        series.setTitle("Heart Rate Curve");
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(14);
        series.setThickness(8);

        //Set Graph 1 Attributes
        graph1.setTitle("Heart Rate (BPM)");
        graph1.setTitleTextSize(30);
        graph1.setTitleColor(Color.BLACK);

        graph1.getLegendRenderer().setVisible(true);
        graph1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GridLabelRenderer gridLabel = graph1.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time Since Incident");
        gridLabel.setVerticalAxisTitle("Beats Per Minute");

        //Set Series 1 Attributes
        series2.setColor(Color.BLUE);
        series2.setTitle("Breathing Rate Curve");
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(14);
        series2.setThickness(8);

        //Set Graph 2 Attributes
        graph2.setTitle("Breathing Rate (BPM)");
        graph2.setTitleTextSize(30);
        graph2.setTitleColor(Color.BLACK);

        graph2.getLegendRenderer().setVisible(true);
        graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GridLabelRenderer gridLabel2 = graph2.getGridLabelRenderer();
        gridLabel2.setHorizontalAxisTitle("Time Since Incident");
        gridLabel2.setVerticalAxisTitle("Breaths Per Minute");

    } */
}}


