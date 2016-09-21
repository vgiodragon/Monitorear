package com.example.giovanny.monitorear;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.giovanny.monitorear.Device.Dispositivo;
import com.example.giovanny.monitorear.Device.DispositivoAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeleccionaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner1;
    List<String> list ;
    ArrayAdapter<String> dataAdapter;
    RadioButton rb1,rb2;

    private static String LOG_TAG = "CardViewActivity2";
    private RecyclerView mRecyclerView;
    private DispositivoAdapter mDisAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList <Dispositivo>results;
    ArrayList<Censado> resultsCensado;
    int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecciona);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        list = new ArrayList<>();
        actualizaSpinner();
        rb1= (RadioButton)findViewById(R.id.radioBCansat);
        rb2= (RadioButton)findViewById(R.id.radioBSensor);

        rb1.setChecked(false);
        rb2.setChecked(false);

        results= new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDisAdapter = new DispositivoAdapter(results);
        mRecyclerView.setAdapter(mDisAdapter);

    }

    public void actualizaSpinner(){
        dataAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("aca","intem selected");
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();

        if(list.get(position).equals("Zona 2")){
            //new cargarDispositivos("consInf/").execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioBCansat:
                if (checked){
                    rb2.setChecked(false);
                    list.clear();
                    new cargarTiposSensor("consFiltro/cansat").execute();
                    break;
                }
            case R.id.radioBSensor:
                if (checked){
                    rb1.setChecked(false);
                    list.clear();
                    new cargarTiposSensor("consFiltro/sensor").execute();
                    break;
                }
        }
    }

    private class cargarTiposSensor extends AsyncTask<String, Void, String> {
        String url;
        public cargarTiposSensor(String url){
            this.url=url;
        }

        @Override
        protected String doInBackground(String... urls) {
            String respues="...";
            try {
                ConexionServer cs= new ConexionServer();
                list = cs.receiveFiltroDispositivo(url);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return respues;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("respuesta", result);
            //cargaCardViews(results);
            actualizaSpinner();
        }
    }
}
