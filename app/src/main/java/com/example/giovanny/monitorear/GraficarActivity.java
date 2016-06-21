package com.example.giovanny.monitorear;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;

public class GraficarActivity extends AppCompatActivity {

    ArrayList<Censado> results;
    LineChart lineChart ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficar);
        Intent intent = getIntent();
        results = intent.getParcelableArrayListExtra("censados");
        lineChart = (LineChart) findViewById(R.id.chart);
        graficar();
    }


    public void graficar(){
        ArrayList<Entry> entries = new ArrayList<>();
        Censado cen;
        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0;i<results.size();i++){
            cen=results.get(i);
            entries.add(new Entry(cen.getValue(), i));
            labels.add(cen.getHora());
        }
        LineDataSet dataset = new LineDataSet(entries, "ºC");
        LineData data = new LineData(labels, dataset);
        lineChart.setData(data); // set the data and list of lables into chart
        lineChart.setDescription("Seminario Tesis I");  // set the description

    }
}