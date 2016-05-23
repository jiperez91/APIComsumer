package com.example.jiperez.apiconsumer;

import android.annotation.SuppressLint;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

public class DetailsOWNER extends MainActivity {
    TextView details;
    private static JSONObject ownerObject;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_owner);
        details = (TextView) findViewById(R.id.textdetails);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String url = "http://172.23.2.230:8080/cars/apiOwner/" + id;
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

    public static String GET(String url) {
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
            if (inputStream != null)
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
        while ((line = bufferedReader.readLine()) != null)
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
            Toast.makeText(getBaseContext(), "Owner Details!", Toast.LENGTH_LONG).show();
            try {
                ownerObject = new JSONObject(result);
                details.setText("Name: " + upperCaseAllFirst(ownerObject.getString("nombre")) + "\n" + "Last Name: " + upperCaseAllFirst(ownerObject.getString("apellido")) + "\n" + "DNI: " + ownerObject.getString("dni") + "\n" + "Nationality: " + upperCaseAllFirst(ownerObject.getString("nacionalidad")));
                new HttpCars2().execute("http://172.23.2.230:8080/cars/api");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpCars extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject, jsonObject2;
                String aux, owndet = ownerObject.getString("id");
                int tam = jsonArray.length();
                String owners[] = new String[tam];
                int count = 0;
                for (int i = 0; i < tam; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    aux = jsonObject.getString("owner");
                    jsonObject2 = new JSONObject(aux);
                    owners[i] = jsonObject2.getString("id");
                    if (owners[i].equals(owndet)) {
                        count++;
                        break;
                    }
                }
                if (count == 0) {
                    Bundle bundle = getIntent().getExtras();
                    String id = bundle.getString("id");
                    String url = "http://172.23.2.230:8080/cars/apiOwner/" + id;
                    Intent i = new Intent(DetailsOWNER.this, MainActivityDELETE.class);
                    i.putExtra("url", url);
                    startActivity(i);
                } else {
                    Toast.makeText(getBaseContext(), "Owner is in use, cannot be deleted!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpCars2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject, jsonObject2;
                String aux, owndet = ownerObject.getString("id");
                ArrayList<String> cars = new ArrayList<String>();
                String owner_cars = "";
                int tam = jsonArray.length();
                for (int i = 0; i < tam; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    aux = jsonObject.getString("owner");
                    jsonObject2 = new JSONObject(aux);
                    if (owndet.equals(jsonObject2.getString("id"))) {
                        cars.add(upperCaseAllFirst(jsonObject.getString("make")) + " " + upperCaseAllFirst(jsonObject.getString("model")));
                    }
                }
                int tam2 = cars.size();
                if (tam2 != 0) {
                    if (tam2 == 1) {
                        owner_cars = cars.get(0);
                        details.append("\nCar: " + owner_cars);
                    } else {
                        for (int i = 0; i < tam2; i++) {
                            if (i != tam2 - 1) owner_cars += cars.get(i) + ", ";
                            else owner_cars += cars.get(i);
                        }
                        details.append("\nCars: " + owner_cars);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteexe(View view) {
        new HttpCars().execute("http://172.23.2.230:8080/cars/api");
    }

    public void putexe(View view) {
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String url = "http://172.23.2.230:8080/cars/apiOwner/" + id;
        Intent i = new Intent(this, MainActivityPUTOWNER.class);
        i.putExtra("url", url);
        startActivity(i);
    }
}
