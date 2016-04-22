package com.example.jiperez.apiconsumer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    EditText editurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editurl = (EditText) findViewById(R.id.editText);
    }

    public void getexe(View view) {
        if(editurl.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Enter URL!", Toast.LENGTH_LONG).show();
        }
        else {
            Intent i = new Intent(this, MainActivityGET.class);
            i.putExtra("url", editurl.getText().toString());
            startActivity(i);
        }
    }

    public void postexe(View view) {
        String url = editurl.getText().toString();
        if(url.isEmpty()){
            Toast.makeText(getBaseContext(), "Enter URL!", Toast.LENGTH_LONG).show();
        }
        else {
            int index = url.indexOf("/", 31);
            if(url.contains("Owner")){
                if(index == -1) {
                    Intent i = new Intent(this, MainActivityPOSTOWNER.class);
                    i.putExtra("url", editurl.getText().toString());
                    startActivity(i);
                }
                else {
                    Toast.makeText(getBaseContext(), "Delete the ID from the URL!", Toast.LENGTH_LONG).show();
                }
            }
            else {
                if(index == -1) {
                    Intent i = new Intent(this, MainActivityPOSTCAR.class);
                    i.putExtra("url", editurl.getText().toString());
                    startActivity(i);
                }
                else {
                    Toast.makeText(getBaseContext(), "Delete the ID from the URL!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void deleteexe(View view) {
        String url = editurl.getText().toString();
        if(url.isEmpty()){
            Toast.makeText(getBaseContext(), "Enter URL!", Toast.LENGTH_LONG).show();
        }
        else {
            int index = url.indexOf("/",31);
            if(index != -1){
                new HttpAsyncTask().execute(url);
            }
            else {
                Toast.makeText(getBaseContext(), "Enter an ID in the URL!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void putexe(View view) {
        String url = editurl.getText().toString();
        if(url.isEmpty()){
            Toast.makeText(getBaseContext(), "Enter URL!", Toast.LENGTH_LONG).show();
        }
        else {
            int index = url.indexOf("/", 31);
            if(url.contains("Owner")){
                if(index != -1){
                    Intent i = new Intent(this, MainActivityPUTOWNER.class);
                    i.putExtra("url", editurl.getText().toString());
                    startActivity(i);
                }
                else {
                    Toast.makeText(getBaseContext(), "Enter an ID in the URL!", Toast.LENGTH_LONG).show();
                }
            }
            else {
                if(index != -1){
                    Intent i = new Intent(this, MainActivityPUTCAR.class);
                    i.putExtra("url", editurl.getText().toString());
                    startActivity(i);
                }
                else {
                    Toast.makeText(getBaseContext(), "Enter an ID in the URL!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public static String DELETE(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make DELETE request to the given URL
            HttpDelete httpDelete = new HttpDelete(url);

            // 3. Set some headers to inform server about the type of the content
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json");

            // 4. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpDelete);

            // 5. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 6. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 7. return result
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
            return DELETE(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Deleted!", Toast.LENGTH_LONG).show();
        }
    }
}