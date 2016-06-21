package com.example.giovanny.monitorear.Device;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.giovanny.monitorear.Censado;
import com.example.giovanny.monitorear.ConexionServer;
import com.example.giovanny.monitorear.GraficarActivity;
import com.example.giovanny.monitorear.R;

import java.io.IOException;
import java.util.ArrayList;

public class InfoDevicesActivity extends AppCompatActivity {
    private static String LOG_TAG = "CardViewActivity2";
    private RecyclerView mRecyclerView;
    private DispositivoAdapter mDisAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList <Dispositivo>results;
    ArrayList<Censado> resultsCensado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_devices);
        Intent intent = getIntent();
        results= intent.getParcelableArrayListExtra("dispositivos");
        for (int i=0;i<results.size();i++)
            Log.d(LOG_TAG,"_"+results.get(i).toString()+"_");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDisAdapter = new DispositivoAdapter(results);
        mRecyclerView.setAdapter(mDisAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        (mDisAdapter).setOnItemClickListener(new DispositivoAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                new cargarCensado ("consCen/"+results.get(position).getId()).execute();
            }
        });
    }


    private class cargarCensado extends AsyncTask<String, Void, String> {
        String url;
        public cargarCensado(String url){
            this.url=url;
        }

        @Override
        protected String doInBackground(String... urls) {
            String respues="...";
            try {
                ConexionServer cs= new ConexionServer();
                resultsCensado = cs.receiveJson(url);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return respues;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("respuesta", result);
            graficar();
        }
    }

    public void graficar(){
        Intent intent = new Intent(this,GraficarActivity.class);
        intent.putParcelableArrayListExtra("censados",resultsCensado);
        startActivity(intent);
    }
}
