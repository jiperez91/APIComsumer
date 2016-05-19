package com.example.jiperez.apiconsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("ALL")
public class MainActivityPOSTOWNER extends MainActivity {
    EditText etName, etLastName, etDNI, etNationality;
    Button btnPost;
    Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_post_owner);

        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etDNI = (EditText) findViewById(R.id.etDNI);
        etNationality = (EditText) findViewById(R.id.etNationality);
        btnPost = (Button) findViewById(R.id.btnPost);
    }

    public static String POST(String url, Owner owner) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

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
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

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
        if(!validate_not_empty())
            Toast.makeText(getBaseContext(), "Please enter all fields!", Toast.LENGTH_LONG).show();
        else if (!validate_name(etName.getText().toString()))
            Toast.makeText(getBaseContext(), "Invalid name! (letters only, max size = 25)", Toast.LENGTH_LONG).show();
        else if (!validate_lastname(etLastName.getText().toString()))
            Toast.makeText(getBaseContext(), "Invalid last name! (letters only, max size = 25)", Toast.LENGTH_LONG).show();
        else if (!validate_nationality(etNationality.getText().toString()))
            Toast.makeText(getBaseContext(), "Invalid nationality! (letters only, max size = 20)", Toast.LENGTH_LONG).show();
        else if (!validate_dni(etDNI.getText().toString()))
            Toast.makeText(getBaseContext(), "Invalid DNI! (numeric only, size = 7 or 8)", Toast.LENGTH_LONG).show();
        else {
            Bundle bundle = getIntent().getExtras();
            String url = bundle.getString("url");
            new ValidateUniqueDNI().execute(url);
        }
    }

    private boolean validate_not_empty() {
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

    private boolean validate_name(String name) {
        if (name.length() > 25 || !name.matches("[a-záéíóúñüÁÉÍÓÚÑÜA-Z ]+"))
            return false;
        return true;
    }

    private boolean validate_lastname(String last_name) {
        if (last_name.length() > 25 || !last_name.matches("[a-záéíóúñüÁÉÍÓÚÑÜA-Z ]+"))
            return false;
        return true;
    }

    private boolean validate_nationality(String nationality) {
        if (nationality.length() > 20 || !nationality.matches("[a-zA-Z ]+"))
            return false;
        return true;
    }

    private boolean validate_dni(String dni) {
        if (dni.length() < 7 || dni.length() > 8 || !dni.matches("[0-9]+"))
            return false;
        return true;
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
            owner = new Owner();
            owner.setName(etName.getText().toString());
            owner.setLastname(etLastName.getText().toString());
            owner.setDni(etDNI.getText().toString());
            owner.setNationality(etNationality.getText().toString());

            return POST(urls[0], owner);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Posted!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivityPOSTOWNER.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private class ValidateUniqueDNI extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) { return GET(urls[0]); }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                String dni;
                Boolean flag = true;
                for (int i = 0; i < length; i++) {
                    JSONObject owner = jsonArray.getJSONObject(i);
                    dni = owner.getString("dni");
                    if(etDNI.getText().toString().equals(dni)) {
                        flag = false;
                        break;
                    }
                }
                if(flag)
                    new HttpAsyncTask().execute("http://192.168.1.112:8080/cars/apiOwner");
                else
                    Toast.makeText(getBaseContext(), "DNI is already in used and must be unique!", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}