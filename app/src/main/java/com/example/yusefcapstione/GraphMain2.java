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
import com.google.android.material.textfield.TextInputLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphMain2 extends AppCompatActivity {

    // UI elements
    private Button button;
    private Button button2;
    EditText xInput2, yInput2;
    GraphView graph2;
    TextInputLayout xInputLayout2, yInputLayout2;

    // Database variables
    LineGraphSeries<DataPoint> series2;
    MyHelper2 myHelper2;
    SQLiteDatabase sqLiteDatabase2;
//gay guy yusef
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
    }

    // Method to retrieve data from database
    private DataPoint[] getData() {
        // Query database for x and y values
        Cursor cursor = sqLiteDatabase2.query("MyTable2", new String[]{"xValues2", "yValues2"}, null, null, null, null, null);
        // Initialize array to store data points
        DataPoint[] dp = new DataPoint[cursor.getCount()];

        // Iterate over cursor to populate data points array
        for (int i = 0; cursor.moveToNext(); i++) {
            dp[i] = new DataPoint(cursor.getInt(0), cursor.getInt(1));
        }
        // Close the cursor after use
        cursor.close();
        return dp;
    }
}