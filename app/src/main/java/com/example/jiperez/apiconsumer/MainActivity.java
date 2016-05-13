package com.example.jiperez.apiconsumer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    EditText editurl;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.spinner);
        String[] valores = {"Cars", "Owners"};
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, valores));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent = new Intent(this, About.class);
                startActivity(intent);
                break;
            case R.id.contact_me:
                intent = new Intent(this, ContactME.class);
                startActivity(intent);
                break;
            case R.id.exit:
                this.finishAffinity();
                break;
        }
        return true;
    }

    public void getexe(View view) {
        String url, api = spinner.getSelectedItem().toString();
        if (api.equals("Cars")) url = "http://192.168.1.112:8080/cars/api";
        else url = "http://192.168.1.112:8080/cars/apiOwner";
        Intent i = new Intent(this, MainActivityGET.class);
        i.putExtra("url", url);
        startActivity(i);
    }

    public void postexe(View view) {
        String url, api = spinner.getSelectedItem().toString();
        if (api.equals("Cars")) url = "http://192.168.1.112:8080/cars/api";
        else url = "http://192.168.1.112:8080/cars/apiOwner";
        if(url.contains("Owner")){
            Intent i = new Intent(this, MainActivityPOSTOWNER.class);
            i.putExtra("url", url);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, MainActivityPOSTCAR.class);
            i.putExtra("url", url);
            startActivity(i);
        }
    }

    public void searchexe(View view) {
        String url, api = spinner.getSelectedItem().toString();
        if (api.equals("Cars")) url = "http://192.168.1.112:8080/cars/api";
        else url = "http://192.168.1.112:8080/cars/apiOwner";
        if(url.contains("Owner")){
            Intent i = new Intent(this, MainActivitySEARCHOWNER.class);
            i.putExtra("url", url);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, MainActivitySEARCHCAR.class);
            i.putExtra("url", url);
            startActivity(i);
        }
    }
}