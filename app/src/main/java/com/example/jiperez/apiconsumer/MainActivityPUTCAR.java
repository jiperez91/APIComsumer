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
import java.util.Calendar;
import java.util.Iterator;

@SuppressWarnings("ALL")
public class MainActivityPUTCAR extends MainActivity {
    EditText etMake, etModel, etYear, etPlate;
    TextView tvJson;
    Button btnPost;
    Car car;
    Spinner spinner;
    private static String aux2, smake, smodel, syear, splate, sowner;

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

    public static String PUT(String url, Car car, Long owner_id) {
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
            jsonObject.accumulate("owner", owner_id);

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
        if(!validate_not_empty())
            Toast.makeText(getBaseContext(), "Please enter all fields!", Toast.LENGTH_LONG).show();
        else if (!validate_make(etMake.getText().toString()))
            Toast.makeText(getBaseContext(), "Invalid make! (max size = 50)", Toast.LENGTH_LONG).show();
        else if (!validate_model(etModel.getText().toString()))
            Toast.makeText(getBaseContext(), "Invalid model! (max size = 50)", Toast.LENGTH_LONG).show();
        else if (!validate_year(etYear.getText().toString()))
            Toast.makeText(getBaseContext(), "Invalid year! (range = (1900, current year), numeric only)", Toast.LENGTH_LONG).show();
        else if (!validate_plate(etPlate.getText().toString()))
            Toast.makeText(getBaseContext(), "Invalid plate! (format = 3 uppercase letters and 3 numbers (e.g. GDK432))", Toast.LENGTH_LONG).show();
        else if (!validate_changes())
            Toast.makeText(getBaseContext(), "No changes were made!", Toast.LENGTH_LONG).show();
        else {
            new ValidateUniquePlate().execute("http://192.168.1.112:8080/cars/api");
        }
    }

    private boolean validate_not_empty(){
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

    private boolean validate_changes() {
        if (smake.toLowerCase().equals(etMake.getText().toString().toLowerCase()) && smodel.toLowerCase().equals(etModel.getText().toString().toLowerCase()) && syear.equals(etYear.getText().toString()) && splate.equals(etPlate.getText().toString()) && sowner.toLowerCase().equals(spinner.getSelectedItem().toString().toLowerCase()))
            return false;
        return true;
    }

    private boolean validate_make(String make) {
        if (make.length() > 50)
            return false;
        return true;
    }

    private boolean validate_model(String model) {
        if (model.length() > 50)
            return false;
        return true;
    }

    private boolean validate_year(String year) {
        if (!year.matches("[0-9]+") || Integer.parseInt(year) < 1900 || Integer.parseInt(year) > Calendar.getInstance().get(Calendar.YEAR))
            return false;
        return true;
    }

    private boolean validate_plate(String plate) {
        if (plate.length() > 6 || !plate.matches("[A-Z]{3}[0-9]{3}"))
            return false;
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
                aux2 = jsonObject2.getString("dni");
                tvJson.setText("Make: " + upperCaseAllFirst(jsonObject.getString("make")) + "\n" + "Model: " + upperCaseAllFirst(jsonObject.getString("model")) + "\n" + "Year: " + jsonObject.getString("year") + "\n" + "Plate: " + jsonObject.getString("plate") + "\n" + "Owner: " + aux2);
                etMake.setText(upperCaseAllFirst(jsonObject.getString("make")));
                etModel.setText(upperCaseAllFirst(jsonObject.getString("model")));
                etYear.setText(jsonObject.getString("year"));
                etPlate.setText(jsonObject.getString("plate"));
                smake = etMake.getText().toString();
                smodel = etModel.getText().toString();
                syear = etYear.getText().toString();
                splate = etPlate.getText().toString();
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
                    valores[i] = owner.getString("id") + " - " + upperCaseAllFirst(owner.getString("nombre")) + " " + upperCaseAllFirst(owner.getString("apellido"));
                    if (owner.getString("dni").equals(aux2)){
                        pos = i;
                    }
                }
                spinner.setAdapter(new ArrayAdapter<String>(MainActivityPUTCAR.this, android.R.layout.simple_spinner_item, valores));
                spinner.setSelection(pos);
                sowner = spinner.getSelectedItem().toString();
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

            String the_owner = spinner.getSelectedItem().toString();
            int pos = 0;
            for (int i = 0; i < the_owner.length(); i++) {
                if (the_owner.charAt(i) == ' ') {
                    pos = i;
                    break;
                }
            }
            String id_owner = the_owner.substring(0, pos);
            return PUT(urls[0], car, Long.parseLong(id_owner));
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Updated!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivityPUTCAR.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private class ValidateUniquePlate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) { return GET(urls[0]); }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                if (!splate.equals(etPlate.getText().toString())) {
                    JSONArray jsonArray = new JSONArray(result);
                    int length = jsonArray.length();
                    String plate;
                    Boolean flag = true;
                    for (int i = 0; i < length; i++) {
                        JSONObject car = jsonArray.getJSONObject(i);
                        plate = car.getString("plate");
                        if(etPlate.getText().toString().equals(plate)) {
                            flag = false;
                            break;
                        }
                    }
                    if(flag) {
                        Bundle bundle = getIntent().getExtras();
                        String url = bundle.getString("url");
                        new HttpAsyncTask().execute(url);
                    }
                    else
                        Toast.makeText(getBaseContext(), "Plate is already in used and must be unique!", Toast.LENGTH_LONG).show();
                }
                else {
                    Bundle bundle = getIntent().getExtras();
                    String url = bundle.getString("url");
                    new HttpAsyncTask().execute(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

