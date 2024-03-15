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

        TextInputLayout xInputLayout = findViewById(R.id.inputTextX);
        TextInputLayout yInputLayout = findViewById(R.id.inputTextY);

        // Now get the EditText from the TextInputLayout
        xInput = xInputLayout.getEditText();
        yInput = yInputLayout.getEditText();

        //Get Graph from layout xml
        graph = (GraphView) findViewById(R.id.graph);
        myHelper = new MyHelper(this);
        sqLiteDatabase = myHelper.getWritableDatabase();

        exqButton();

        // New code for back button initialization and click listener
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

                // Replace newlines and spaces with a single space or remove them, then trim
                String xValStr = xInput.getText().toString().replaceAll("[\n\\s]+", "").trim();
                String yValStr = yInput.getText().toString().replaceAll("[\n\\s]+", "").trim();

                // Parse the cleaned-up strings to integers
                int xVal = Integer.parseInt(xValStr);
                int yVal = Integer.parseInt(yValStr);
                myHelper.insertData(xVal,yVal);

                series = new LineGraphSeries<DataPoint>(getData());
                graph.addSeries(series);
            }
        });
    }

    private DataPoint[] getData() {
        //Read data from database
        String[] columns = {"xValues", "yValues"};
        Cursor cursor = sqLiteDatabase.query("MyTable", columns, null, null, null, null, null);

        DataPoint[] dp = new DataPoint[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getInt(0), cursor.getInt(1));
        }
        return dp;
    }
}


