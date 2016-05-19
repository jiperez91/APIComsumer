package com.example.jiperez.apiconsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("ALL")
public class MainActivityGET extends MainActivity {
    ListView lsvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_get);

        lsvw = (ListView) findViewById(R.id.listView);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");

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
            Bundle bundle = getIntent().getExtras();
            String url = bundle.getString("url");
            if(url.contains("Owner")){
                try {
                    Toast.makeText(getBaseContext(), "Owners!", Toast.LENGTH_LONG).show();
                    final JSONArray jsonArray = new JSONArray(result);
                    int length = jsonArray.length();
                    List<String> listContents = new ArrayList<String>(length);
                    for (int i = 0; i < length; i++) {
                        JSONObject owner = jsonArray.getJSONObject(i);
                        listContents.add("ID " + owner.getString("id") + " - " + upperCaseAllFirst(owner.getString("nombre")) + " " + upperCaseAllFirst(owner.getString("apellido")));
                    }
                    lsvw.setAdapter(new ArrayAdapter<String>(MainActivityGET.this, android.R.layout.simple_list_item_1, listContents));

                    lsvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivityGET.this, DetailsOWNER.class);
                            try {
                                JSONObject owner = jsonArray.getJSONObject(position);
                                intent.putExtra("id", owner.getString("id"));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Toast.makeText(getBaseContext(), "Cars!", Toast.LENGTH_LONG).show();
                    final JSONArray jsonArray = new JSONArray(result);
                    int length = jsonArray.length();
                    List<String> listContents = new ArrayList<String>(length);
                    for (int i = 0; i < length; i++) {
                        JSONObject car = jsonArray.getJSONObject(i);
                        listContents.add("ID " + car.getString("id") + " - " + upperCaseAllFirst(car.getString("make")) + " " + upperCaseAllFirst(car.getString("model")));
                    }
                    lsvw.setAdapter(new ArrayAdapter<String>(MainActivityGET.this, android.R.layout.simple_list_item_1, listContents));

                    lsvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivityGET.this, DetailsCAR.class);
                            try {
                                JSONObject car = jsonArray.getJSONObject(position);
                                intent.putExtra("id", car.getString("id"));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}