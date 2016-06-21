package com.example.giovanny.monitorear;

import android.util.Log;

import com.example.giovanny.monitorear.Device.Dispositivo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by giovanny on 19/06/16.
 */
public class ConexionServer {
    String urlp="http://52.40.252.10:8081/";
    public String LoginPac(String func,String user,String pass) throws IOException {
        InputStream is = null;
        int len = 100;

        try {
            String furl=urlp+func+(user+"@!"+pass).replace(" ","__");
            URL url = new URL(furl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("respuesta", "The response is: " + response);
            is = conn.getInputStream();
            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public ArrayList<Censado> receiveJson(String func)throws IOException {

        InputStream is = null;
        int len = 1000;
        ArrayList<Censado> censados=new ArrayList<>();
        try {
            String furl=urlp+func;
            URL url = new URL(furl);
            Log.d("respuesta", furl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

            int response = conn.getResponseCode();
            Log.d("respuesta", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            Log.d("respuesta", contentAsString);
            JSONObject jsonObject = new JSONObject(contentAsString);
            JSONArray jsonArray = jsonObject.getJSONArray("array_json");

            Log.d("respuesta", "Tamano json: " + jsonArray.length());

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject json_data = jsonArray.getJSONObject(i);
                Censado censado= new Censado(json_data.getString("id_cansat"),
                        json_data.getString("id_sensor"),
                        json_data.getString("tipo_sensor") ,
                        (float)json_data.getDouble("value"),
                        json_data.getString("fecha") ,
                        json_data.getString("hora") );
                censados.add(censado);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return censados;
    }

    public ArrayList<Dispositivo> receiveJsonDispositivo(String func)throws IOException {

        InputStream is = null;
        int len = 1000;
        ArrayList<Dispositivo> dispositivos=new ArrayList<>();

        try {
            String furl=urlp+func;
            URL url = new URL(furl);
            Log.d("respuesta", furl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // Starts the query
            conn.connect();


            int response = conn.getResponseCode();
            Log.d("respuesta", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            Log.d("respuesta", contentAsString);
            JSONObject jsonObject = new JSONObject(contentAsString);
            JSONArray jsonArray = jsonObject.getJSONArray("array_json");

            Log.d("respuesta", "Tamano json: " + jsonArray.length());
            JSONObject json_data;
            for(int i=0; i<jsonArray.length(); i++){
                json_data = jsonArray.getJSONObject(i);

                Dispositivo dispositivo = new Dispositivo(R.drawable.cansat,
                                json_data.getString("id_cansat"),
                                json_data.getString("modelo"),
                                json_data.getString("f_install")
                                );
                JSONArray jsonArraySensores = json_data.getJSONArray("sensores");
                dispositivos.add(dispositivo);

                for(int j=0; j<jsonArraySensores.length(); j++){
                    json_data = jsonArraySensores.getJSONObject(j);
                    Dispositivo sensor = new Dispositivo(R.drawable.sensor,
                            json_data.getString("id_sensor"),
                            json_data.getString("modelo"),
                            json_data.getString("f_install")
                            );
                    dispositivos.add(sensor);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return dispositivos;
    }

    private String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
