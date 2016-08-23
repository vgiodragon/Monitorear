package com.example.giovanny.monitorear.GraficaryMQTT;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.giovanny.monitorear.Censado;
import com.example.giovanny.monitorear.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.util.ArrayList;

public class GraficarActivity extends AppCompatActivity {

    ArrayList<Censado> results;
    LineChart lineChart ;
    TextView tipo;
    TextView id;
    private final String GioDBug = "GioDBug";
    private ArrayList<Entry> entries;
    ArrayList<String> labels;
    String topicos [] = new String[]{"temperatura","presion","monoxido","dioxido","amoniaco","altura"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficar);
        tipo=(TextView)findViewById(R.id.edTipoSensor);
        id=(TextView)findViewById(R.id.edIdSensor);
        Intent intent = getIntent();

        tipo.setText(intent.getStringExtra("tiposensor"));
        id.setText("id:"+intent.getStringExtra("idsensor"));

        results = intent.getParcelableArrayListExtra("censados");
        lineChart = (LineChart) findViewById(R.id.chart);
        graficar(intent.getStringExtra("unidad"));
        creoClienteMQTT(intent.getStringExtra("tiposensor"));
    }

    private void graficar(String unidad){
        entries = new ArrayList<>();
        Censado cen;
        labels = new ArrayList<>();
        for(int i=0;i<results.size();i++){
            cen=results.get(i);
            entries.add(new Entry(cen.getValue(), i));
            labels.add(cen.getHora());
        }
        LineDataSet dataset = new LineDataSet(entries, unidad);
        LineData data = new LineData(labels, dataset);
        lineChart.setData(data); // set the data and list of lables into chart
        lineChart.setDescription("Seminario Tesis I");  // set the description
    }

    private void refrescar(Censado censado){
        results.add(censado);
        entries.add(new Entry(censado.getValue(), results.size()-1));
        labels.add(censado.getHora());

        LineData data = new LineData(labels, new LineDataSet(entries, "ºC"));
        lineChart.setData(data); // set the data and list of lables into chart
        lineChart.invalidate(); // refresh
        Log.d(GioDBug,"Refrescado!");
    }

    private void creoClienteMQTT(final String sub){
        final MqttAndroidClient mqttAndroidClient
                = new MqttAndroidClient(this.getApplicationContext(), "tcp://52.10.199.174:1883", "GioID");
        mqttAndroidClient.setCallback(new MiCallBackMQTT() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(GioDBug,"Llego del topic " + topic + ": " + new String(message.getPayload()));
                Censado cen = converJson(new String(message.getPayload()));
                refrescar(cen);
            }
        });

        try {
            mqttAndroidClient.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(GioDBug, "Connection Success!");
                    try {
                        Log.d(GioDBug,"Subscribing to "+sub.toLowerCase());
                        mqttAndroidClient.unsubscribe(topicos);
                        mqttAndroidClient.subscribe(sub.toLowerCase(), 0);
                    } catch (MqttException ex) { }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(GioDBug,"Connection Failure!");
                }
            });
        } catch (MqttException ex) {

        }

    }

}