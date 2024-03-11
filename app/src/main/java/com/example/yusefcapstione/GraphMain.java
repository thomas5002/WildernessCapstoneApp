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

public class GraphMain extends AppCompatActivity {

    private Button button;
    private Button button2;
    EditText xInput, yInput;
    GraphView graph;


    TextInputLayout xInputLayout, yInputLayout;
    LineGraphSeries<DataPoint> series;
    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        button2 = (Button) findViewById(R.id.insertBTN);

        xInputLayout = findViewById(R.id.inputTextX);
        yInputLayout = findViewById(R.id.inputTextY);

        xInput = xInputLayout.getEditText();
        yInput = yInputLayout.getEditText();

        graph = (GraphView) findViewById(R.id.graph);
        myHelper = new MyHelper(this);
        sqLiteDatabase = myHelper.getWritableDatabase();

        exqButton();
        updateGraph(); // Call this method to update the graph with existing data points

        button = findViewById(R.id.backBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphMain.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void exqButton(){
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xValStr = xInput.getText().toString().replaceAll("[\n\\s]+", "").trim();
                String yValStr = yInput.getText().toString().replaceAll("[\n\\s]+", "").trim();

                int xVal = Integer.parseInt(xValStr);
                int yVal = Integer.parseInt(yValStr);
                myHelper.insertData(xVal, yVal);

                updateGraph(); // Update the graph after inserting new data
            }
        });
    }

    private void updateGraph() {
        series = new LineGraphSeries<DataPoint>(getData());
        graph.removeAllSeries(); // Clear the old series before adding the new one

        series.setColor(Color.RED); // Example: Set the line color
        series.setThickness(8); // Set the thickness of the line
        series.setDrawDataPoints(true); // Enable drawing data points
        series.setDataPointsRadius(10); // Set data points radius
        series.setAnimated(true); // Animate the series

        graph.addSeries(series);

        graph.setTitle("Heart Rate (BPM) v.s. Time (min)");
        graph.setTitleColor(Color.BLUE);
        graph.setTitleTextSize(48);

        series.setTitle("Data Series");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setTextSize(26);
    }

    private DataPoint[] getData() {
        String[] columns = {"xValues", "yValues"};
        Cursor cursor = sqLiteDatabase.query("MyTable", columns, null, null, null, null, null);

        DataPoint[] dp = new DataPoint[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getInt(0), cursor.getInt(1));
        }
        cursor.close(); // It's a good practice to close the cursor
        return dp;
    }


}


