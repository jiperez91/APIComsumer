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
import android.widget.TextView;
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
public class MainActivityPUTCAR extends MainActivity {
    EditText etMake, etModel, etYear, etPlate;
    TextView tvJson;
    Button btnPost;
    Car car;
    Spinner spinner;
    private static String aux2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_put_car);

        tvJson = (TextView) findViewById(R.id.tvJson);
        etMake = (EditText) findViewById(R.id.etMake);
        etModel = (EditText) findViewById(R.id.etModel);
        etYear = (EditText) findViewById(R.id.etYear);
        etPlate = (EditText) findViewById(R.id.etPlate);
        btnPost = (Button) findViewById(R.id.btnPost);
        spinner = (Spinner) findViewById(R.id.spinner);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");

        new RetrieveSiteData().execute(url);
        new RetrieveSiteData2().execute("http://192.168.1.112:8080/cars/apiOwner");
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

    public static String PUT(String url, Car car, Long spinner_id) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPut httpPut = new HttpPut(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("make", car.getMake());
            jsonObject.accumulate("model", car.getModel());
            jsonObject.accumulate("year", car.getYear());
            jsonObject.accumulate("plate", car.getPlate());
            jsonObject.accumulate("owner", spinner_id + 1);

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
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnPost:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Please enter all fields!", Toast.LENGTH_LONG).show();
                else {
                    Bundle bundle = getIntent().getExtras();
                    String url = bundle.getString("url");
                    // call AsynTask to perform network operation on separate thread
                    new HttpAsyncTask().execute(url);
                    break;
                }
        }
    }

    private boolean validate(){
        if(etMake.getText().toString().trim().equals(""))
            return false;
        else if(etModel.getText().toString().trim().equals(""))
            return false;
        else if(etYear.getText().toString().trim().equals(""))
            return false;
        else if(etPlate.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    private class RetrieveSiteData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String aux = jsonObject.getString("owner");
                JSONObject jsonObject2 = new JSONObject(aux);
                aux2 = jsonObject2.getString("nombre") + " " + jsonObject2.getString("apellido");
                tvJson.setText("Make: " + jsonObject.getString("make") + "\n" + "Model: " + jsonObject.getString("model") + "\n" + "Year: " + jsonObject.getString("year") + "\n" + "Plate: " + jsonObject.getString("plate") + "\n" + "Owner: " + aux2);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class RetrieveSiteData2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                int pos = 0, length = jsonArray.length();
                String[] valores = new String[length];
                for (int i = 0; i < length; i++) {
                    JSONObject owner = jsonArray.getJSONObject(i);
                    valores[i] = owner.getString("nombre") + " " + owner.getString("apellido");
                    if (valores[i].equals(aux2)){
                        pos = i;
                    }
                }
                spinner.setAdapter(new ArrayAdapter<String>(MainActivityPUTCAR.this, android.R.layout.simple_spinner_item, valores));
                spinner.setSelection(pos);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            car = new Car();
            car.setMake(etMake.getText().toString());
            car.setModel(etModel.getText().toString());
            car.setYear(etYear.getText().toString());
            car.setPlate(etPlate.getText().toString());

            return PUT(urls[0], car, spinner.getSelectedItemId());
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Updated!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivityPUTCAR.this, MainActivity.class);
            startActivity(intent);
        }
    }
}