package com.example.jiperez.apiconsumer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;

public class SearchRESULTS extends  MainActivity {
    ListView lsvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

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
                    ArrayList<String> array = bundle.getStringArrayList("array");
                    final JSONArray jsonArray = new JSONArray(result);
                    JSONObject owner;
                    int length = jsonArray.length();
                    int length2 = array.size();
                    final List<String> listContents = new ArrayList<String>();
                    ArrayList<String> owner_det = new ArrayList<String>(length);
                    final ArrayList<String> ids = new ArrayList<String>();
                    boolean contains;
                    for (int i = 0; i < length; i++) {
                        owner_det.clear();
                        contains = true;
                        owner = jsonArray.getJSONObject(i);
                        owner_det.add(owner.getString("nombre").toLowerCase());
                        owner_det.add(owner.getString("apellido").toLowerCase());
                        owner_det.add(owner.getString("dni").toLowerCase());
                        owner_det.add(owner.getString("nacionalidad").toLowerCase());
                        for (int j = 0; j < length2; j++) {
                            if(!owner_det.contains(array.get(j))){
                                contains = false;
                                break;
                            }
                        }
                        if (contains) {
                            ids.add(owner.getString("id"));
                            listContents.add("ID " + owner.getString("id") + " - " + upperCaseAllFirst(owner.getString("nombre")) + " " + upperCaseAllFirst(owner.getString("apellido")));
                        }
                    }
                    if (listContents.isEmpty()) {
                        Toast.makeText(getBaseContext(), "No results!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SearchRESULTS.this, MainActivitySEARCHOWNER.class);
                        startActivity(intent);
                    }
                    else {
                        lsvw.setAdapter(new ArrayAdapter<String>(SearchRESULTS.this, android.R.layout.simple_list_item_1, listContents));
                        Toast.makeText(getBaseContext(), "Search results!", Toast.LENGTH_LONG).show();
                        lsvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(SearchRESULTS.this, DetailsOWNER.class);
                                intent.putExtra("id", ids.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    ArrayList<String> array = bundle.getStringArrayList("array");
                    final JSONArray jsonArray = new JSONArray(result);
                    JSONObject car, car2;
                    String aux2;
                    int length = jsonArray.length();
                    int length2 = array.size();
                    final List<String> listContents = new ArrayList<String>();
                    ArrayList<String> car_det = new ArrayList<String>(length);
                    final ArrayList<String> ids = new ArrayList<String>();
                    boolean contains;
                    for (int i = 0; i < length; i++) {
                        car_det.clear();
                        contains = true;
                        car = jsonArray.getJSONObject(i);
                        String aux = car.getString("owner");
                        car2 = new JSONObject(aux);
                        aux2 = car2.getString("nombre") + " " + car2.getString("apellido");
                        car_det.add(car.getString("make").toLowerCase());
                        car_det.add(car.getString("model").toLowerCase());
                        car_det.add(car.getString("year").toLowerCase());
                        car_det.add(car.getString("plate").toLowerCase());
                        car_det.add(aux2.toLowerCase());
                        for (int j = 0; j < length2; j++) {
                            if(!car_det.contains(array.get(j))){
                                contains = false;
                                break;
                            }
                        }
                        if (contains) {
                            ids.add(car.getString("id"));
                            listContents.add("ID " + car.getString("id") + " - " + upperCaseAllFirst(car.getString("make")) + " " + upperCaseAllFirst(car.getString("model")));
                        }
                    }
                    if (listContents.isEmpty()) {
                        Toast.makeText(getBaseContext(), "No results!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SearchRESULTS.this, MainActivitySEARCHCAR.class);
                        startActivity(intent);
                    }
                    else {
                        lsvw.setAdapter(new ArrayAdapter<String>(SearchRESULTS.this, android.R.layout.simple_list_item_1, listContents));
                        Toast.makeText(getBaseContext(), "Search results!", Toast.LENGTH_LONG).show();
                        lsvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(SearchRESULTS.this, DetailsCAR.class);
                                intent.putExtra("id", ids.get(position));
                                startActivity(intent);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
