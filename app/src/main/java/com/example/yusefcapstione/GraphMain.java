package com.example.yusefcapstione;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

        // Set onClickListener for insert button
        exqButton();

        // Set onClickListener for back button
        button = findViewById(R.id.backBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphMain.this, MainActivity.class);
                startActivity(intent);
            }
        });
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

                // Add new data to the graph
                series = new LineGraphSeries<DataPoint>(getData());
                graph.addSeries(series);
            }
        });
    }

    // Method to retrieve data from database
    private DataPoint[] getData() {
        // Read data from database
        String[] columns = {"xValues", "yValues"};
        Cursor cursor = sqLiteDatabase.query("MyTable", columns, null, null, null, null, null);

        // Initialize array to store data points
        DataPoint[] dp = new DataPoint[cursor.getCount()];

        // Iterate over cursor to populate data points array
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getInt(0), cursor.getInt(1));
        }
        return dp;
    }
}