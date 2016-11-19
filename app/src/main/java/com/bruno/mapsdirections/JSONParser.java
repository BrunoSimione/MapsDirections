package com.bruno.mapsdirections;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Exchanger;

public class JSONParser extends AsyncTask<String, Void ,JSONObject>{

    static InputStream is = null;
    static JSONObject json = null;
    static String output = "";
    static String url = "http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&sensor=false";

    @Override
    protected JSONObject doInBackground(String... params) {

        if (params.length > 0) {

            URL _url;
            HttpURLConnection urlConnection;

            try {
                _url = new URL(params[0]);
                urlConnection = (HttpURLConnection) _url.openConnection();
            } catch (MalformedURLException e) {
                Log.e("JSON Parser", "Error due to a malformed URL " + e.toString());
                return null;
            } catch (IOException e) {
                Log.e("JSON Parser", "IO error " + e.toString());
                return null;
            }

            try {
                is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder(is.available());
                String line;
                while ((line = reader.readLine()) != null) {
                    total.append(line).append('\n');
                }
                output = total.toString();
            } catch (IOException e) {
                Log.e("JSON Parser", "IO error " + e.toString());
                return null;
            } catch (Exception e) {
                Log.e("ERROAQUI", e.toString());
            } finally {
                urlConnection.disconnect();
            }

            try {
                json = new JSONObject(output);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

        }

        return json;
    }
}