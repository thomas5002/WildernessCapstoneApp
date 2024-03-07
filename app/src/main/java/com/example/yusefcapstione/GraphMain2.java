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
public class GraphMain2 extends AppCompatActivity{

    private Button button;
    private Button button2;
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

        button2 = (Button) findViewById(R.id.insertBTN);

        TextInputLayout xInputLayout2 = findViewById(R.id.inputTextX);
        TextInputLayout yInputLayout2 = findViewById(R.id.inputTextY);

        // Now get the EditText from the TextInputLayout
        xInput2 = xInputLayout2.getEditText();
        yInput2 = yInputLayout2.getEditText();

        //Get Graph from layout xml
        graph2 = (GraphView) findViewById(R.id.graph2);
        myHelper2 = new MyHelper2(this);
        sqLiteDatabase2 = myHelper2.getWritableDatabase();

        exqButton();

        // New code for back button initialization and click listener
        button = findViewById(R.id.backBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphMain2.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private void exqButton(){
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Replace newlines and spaces with a single space or remove them, then trim
                String xValStr2 = xInput2.getText().toString().replaceAll("[\n\\s]+", "").trim();
                String yValStr2 = yInput2.getText().toString().replaceAll("[\n\\s]+", "").trim();

                // Parse the cleaned-up strings to integers
                int xVal2 = Integer.parseInt(xValStr2);
                int yVal2 = Integer.parseInt(yValStr2);
                myHelper2.insertData(xVal2,yVal2);

                series2 = new LineGraphSeries<DataPoint>(getData());
                graph2.addSeries(series2);
            }
        });
    }

    private DataPoint[] getData() {
        //Read data from database
        String[] columns = {"xValues", "yValues"};
        Cursor cursor = sqLiteDatabase2.query("MyTable", columns, null, null, null, null, null);

        DataPoint[] dp = new DataPoint[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getInt(0), cursor.getInt(1));
        }
        return dp;
    }
}
