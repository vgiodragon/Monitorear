package com.example.giovanny.monitorear;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.giovanny.monitorear.Device.Dispositivo;
import com.example.giovanny.monitorear.Device.InfoDevicesActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Dispositivo> results;
    private final int code_request=1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, code_request);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case code_request:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MainActivity.this, "COARSE LOCATION permitido", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "COARSE LOCATION no permitido", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void BLogin(View view){
        //new cargarDispositivos("consInf/").execute();
        Intent intent = new Intent(this,SeleccionaActivity.class);
        startActivity(intent);
    }

    private class cargarDispositivos extends AsyncTask<String, Void, String> {
        String url;
        public cargarDispositivos(String url){
            this.url=url;
        }

        @Override
        protected String doInBackground(String... urls) {
            String respues="...";
            try {
                ConexionServer cs= new ConexionServer();
                results = cs.receiveJsonDispositivo(url);
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
        Intent intent = new Intent(this,InfoDevicesActivity.class);
        intent.putParcelableArrayListExtra("dispositivos",results);
        startActivity(intent);
    }
}
