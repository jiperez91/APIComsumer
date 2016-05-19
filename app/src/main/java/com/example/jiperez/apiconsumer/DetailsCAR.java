package com.example.jiperez.apiconsumer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class DetailsCAR extends MainActivity {
    TextView details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_car);
        details = (TextView) findViewById(R.id.textdetails);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String url = "http://192.168.1.112:8080/cars/api/" + id;
        new HttpAsyncTask().execute(url);
    }

    public String upperCaseAllFirst(String value) {
        value = value.toLowerCase();
        char[] array = value.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            }
        }
        return new String(array);
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Car Details!", Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String aux = jsonObject.getString("owner");
                JSONObject jsonObject2 = new JSONObject(aux);
                String aux2 = jsonObject2.getString("nombre") + " " + jsonObject2.getString("apellido");
                details.setText("Make: " + upperCaseAllFirst(jsonObject.getString("make")) + "\n" + "Model: " + upperCaseAllFirst(jsonObject.getString("model")) + "\n" + "Year: " + jsonObject.getString("year") + "\n" + "Plate: " + jsonObject.getString("plate") + "\n" + "Owner: " + upperCaseAllFirst(aux2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteexe(View view) {
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String url = "http://192.168.1.112:8080/cars/api/" + id;
        Intent i = new Intent(this, MainActivityDELETE.class);
        i.putExtra("url", url);
        startActivity(i);
    }

    public void putexe(View view) {
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String url = "http://192.168.1.112:8080/cars/api/" + id;
        Intent i = new Intent(this, MainActivityPUTCAR.class);
        i.putExtra("url", url);
        startActivity(i);
    }
}


