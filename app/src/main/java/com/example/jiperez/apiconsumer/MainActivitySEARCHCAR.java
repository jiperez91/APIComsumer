package com.example.jiperez.apiconsumer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivitySEARCHCAR extends MainActivity {
    EditText etMake, etModel, etYear, etPlate;
    Button btnSearch;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);

        etMake = (EditText) findViewById(R.id.etMake);
        etModel = (EditText) findViewById(R.id.etModel);
        etYear = (EditText) findViewById(R.id.etYear);
        etPlate = (EditText) findViewById(R.id.etPlate);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        spinner = (Spinner) findViewById(R.id.spinner);

        new HttpAsyncGet().execute("http://192.168.1.112:8080/cars/apiOwner");
    }

    public void onClick(View view) {
        ArrayList<String> array = new ArrayList<String>();
        if(!etMake.getText().toString().trim().equals("")){
            array.add(etMake.getText().toString().toLowerCase());
        }
        if(!etModel.getText().toString().trim().equals("")){
            array.add(etModel.getText().toString().toLowerCase());
        }
        if(!etYear.getText().toString().trim().equals("")){
            array.add(etYear.getText().toString());
        }
        if(!etPlate.getText().toString().trim().equals("")){
            array.add(etPlate.getText().toString());
        }
        if(!spinner.getSelectedItem().toString().equals("")){
            array.add(spinner.getSelectedItem().toString().toLowerCase());
        }
        Intent intent = new Intent(this, SearchRESULTS.class);
        intent.putExtra("url", "http://192.168.1.112:8080/cars/api");
        intent.putExtra("array", array);
        startActivity(intent);
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    private class HttpAsyncGet extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                String[] valores = new String[length+1];
                valores[0] = "";
                for (int i = 1; i < length+1; i++) {
                    JSONObject owner = jsonArray.getJSONObject(i-1);
                    valores[i] = owner.getString("nombre") + " " + owner.getString("apellido");
                }
                spinner.setAdapter(new ArrayAdapter<String>(MainActivitySEARCHCAR.this, android.R.layout.simple_spinner_item, valores));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
