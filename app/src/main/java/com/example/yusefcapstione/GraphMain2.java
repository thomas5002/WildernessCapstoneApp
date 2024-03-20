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

    private Button button;
    private Button button2;
    private int dataPointIndex = 0;
    EditText xInput2, yInput2;
    GraphView graph2;

    TextInputLayout xInputLayout2, yInputLayout2;
    LineGraphSeries<DataPoint> series2;
    MyHelper2 myHelper2;
    SQLiteDatabase sqLiteDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);


        button2 = findViewById(R.id.insertBTN);
        xInputLayout2 = findViewById(R.id.inputTextX);
        yInputLayout2 = findViewById(R.id.inputTextY);

        xInput2 = xInputLayout2.getEditText();
        yInput2 = yInputLayout2.getEditText();

        graph2 = findViewById(R.id.graph2);
        myHelper2 = new MyHelper2(this);
        sqLiteDatabase2 = myHelper2.getWritableDatabase();

        int respiratoryRate = getIntent().getIntExtra("respiratoryRate", 0);

       // GraphView graph = findViewById(R.id.graph2);
        series2 = new LineGraphSeries<>();
        graph2.addSeries(series2);

        updateGraph(respiratoryRate);

        exqButton();
       // loadGraphData(); // Load graph data when activity is created

        button = findViewById(R.id.backBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphMain2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateGraph(int respiratoryRate) {
        series2.appendData(new DataPoint(dataPointIndex++, respiratoryRate), true, 60);
    }

    private void exqButton() {
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xValStr2 = xInput2.getText().toString().replaceAll("[\n\\s]+", "").trim();
                String yValStr2 = yInput2.getText().toString().replaceAll("[\n\\s]+", "").trim();

                int xVal2 = Integer.parseInt(xValStr2);
                int yVal2 = Integer.parseInt(yValStr2);
                myHelper2.insertData(xVal2, yVal2);

                series2 = new LineGraphSeries<DataPoint>(getData());
                graph2.addSeries(series2);

              //  loadGraphData(); // Reload graph data after new data insertion
            }
        });
    }

   /* private void loadGraphData() {
        series2 = new LineGraphSeries<>(getData());
        graph2.removeAllSeries(); // Clear the graph before adding the new series


        series2.setColor(Color.RED); // Example: Set the line color
        series2.setThickness(8); // Set the thickness of the line
        series2.setDrawDataPoints(true); // Enable drawing data points
        series2.setDataPointsRadius(10); // Set data points radius
        series2.setAnimated(true); // Animate the series

        graph2.addSeries(series2);

        graph2.setTitle("Breath Rate (BPM) v.s. Time (min)");
        graph2.setTitleColor(Color.BLUE);
        graph2.setTitleTextSize(48);

        series2.setTitle("Data Series");
        graph2.getLegendRenderer().setVisible(true);
        graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph2.getLegendRenderer().setTextSize(26);
    } */

    private DataPoint[] getData() {
        Cursor cursor = sqLiteDatabase2.query("MyTable2", new String[]{"xValues2", "yValues2"}, null, null, null, null, null);

        DataPoint[] dp = new DataPoint[cursor.getCount()];

        for (int i = 0; cursor.moveToNext(); i++) {
            dp[i] = new DataPoint(cursor.getInt(0), cursor.getInt(1));
        }
        cursor.close(); // Always close the cursor after use
        return dp;
    }
}
