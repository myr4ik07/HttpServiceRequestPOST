package com.example.jaros.httpservicerequestpost;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MyDownloadTask().execute();

    }
}

class MyDownloadTask extends AsyncTask<String, String, String> {

    String line = null;
    String jsonString = "";

    ArrayList<String> unitArray = new ArrayList<String>();
    JSONObject dataJsonObj;

    @Override
    protected String doInBackground(String... strings) {

        try {

            URL url = new URL("http://192.168.1.102/http_1/hs/http_1/Commission");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);//5 secs
            connection.setReadTimeout(5000);//5 secs

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

            JSONObject obj = new JSONObject();
            obj.put("value", "Користувач №1");

            out.write(obj.toString());
            out.flush();
            out.close();

            int res = connection.getResponseCode();

            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                jsonString = jsonString + line;
            }
            connection.disconnect();

            dataJsonObj = new JSONObject(jsonString);
            JSONArray users = dataJsonObj.getJSONArray("unit");
            for (int i = 0; i < users.length(); i++) {
                unitArray.add(users.getString(i));
            }

        } catch (Exception e) {
            return e.toString();
        }

        return null;
    }
}