package com.example.yusefcapstione;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;

public class RecordSymptoms extends AppCompatActivity {

    private Button button;
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
        GraphView graph1 = (GraphView)  findViewById(R.id.graph);
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

    }
}


