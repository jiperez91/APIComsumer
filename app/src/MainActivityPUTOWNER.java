package com.example.jiperez.apiconsumer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

@SuppressWarnings("ALL")
public class MainActivityPUTOWNER extends MainActivity {
    EditText etJson, etName, etLastName, etDNI, etNationality;
    Button btnPost;
    Owner owner;
    private static final String TAG = "LOGGED:::MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_put_owner);

        etJson = (EditText) findViewById(R.id.etJson);
        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etDNI = (EditText) findViewById(R.id.etDNI);
        etNationality = (EditText) findViewById(R.id.etNationality);
        btnPost = (Button) findViewById(R.id.btnPost);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");

        new RetrieveSiteData().execute(url);
    }

    public static String PUT(String url, Owner owner) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make PUT request to the given URL
            HttpPut httpPut = new HttpPut(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("nombre", owner.getName());
            jsonObject.accumulate("apellido", owner.getLastname());
            jsonObject.accumulate("dni", owner.getDni());
            jsonObject.accumulate("nacionalidad", owner.getNationality());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPut.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPut);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
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

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnPost:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                Bundle bundle = getIntent().getExtras();
                String url = bundle.getString("url");
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute(url);
                break;
        }

    }

    private boolean validate(){
        if(etName.getText().toString().trim().equals(""))
            return false;
        else if(etLastName.getText().toString().trim().equals(""))
            return false;
        else if(etDNI.getText().toString().trim().equals(""))
            return false;
        else if(etNationality.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            owner = new Owner();
            owner.setName(etName.getText().toString());
            owner.setLastname(etLastName.getText().toString());
            owner.setDni(etDNI.getText().toString());
            owner.setNationality(etNationality.getText().toString());

            return PUT(urls[0], owner);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Updated!", Toast.LENGTH_LONG).show();
        }
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

    private class RetrieveSiteData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                Iterator x = jsonObject.keys();
                JSONArray jsonArray = new JSONArray();

                while (x.hasNext()) {
                    String key = (String) x.next();
                    jsonArray.put(jsonObject.get(key));
                }
                etJson.setText(jsonArray.toString(1));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }