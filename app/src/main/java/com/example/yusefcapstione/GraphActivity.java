package com.example.yusefcapstione;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;

public class GraphActivity extends AppCompatActivity {

    private RecordSymptoms graphManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        LineChart lineChart = findViewById(R.id.chart);
        graphManager = new RecordSymptoms(this, lineChart);
        graphManager.updateGraph(); // Call this to display the graph when the activity starts
    }
}
