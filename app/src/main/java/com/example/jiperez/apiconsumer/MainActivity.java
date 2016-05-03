package com.example.jiperez.apiconsumer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    EditText editurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editurl = (EditText) findViewById(R.id.editText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                break;
            case R.id.exit:
                this.finishAffinity();
                break;
        }
        return true;
    }

    public void getexe(View view) {
        String url = editurl.getText().toString();
        if(url.isEmpty()){
            Toast.makeText(getBaseContext(), "Enter URL!", Toast.LENGTH_LONG).show();
        }
        else {
            int index = url.indexOf("/", 31);
            if(url.contains("Owner") && index != -1){
                String own = "one owner";
                Intent intent = new Intent(this, DetailsOWNER.class);
                intent.putExtra("own", own);
                intent.putExtra("url", url);
                startActivity(intent);
            }
            else{
                if(!url.contains("Owner") && index != -1) {
                    String car = "one car";
                    Intent intent = new Intent(this, DetailsCAR.class);
                    intent.putExtra("car", car);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                else {
                    Intent i = new Intent(this, MainActivityGET.class);
                    i.putExtra("url", url);
                    startActivity(i);
                }
            }
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
                    i.putExtra("url", url);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getBaseContext(), "Delete the ID from the URL!", Toast.LENGTH_LONG).show();
                }
            }
            else {
                if(index == -1) {
                    Intent i = new Intent(this, MainActivityPOSTCAR.class);
                    i.putExtra("url", url);
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
                Intent i = new Intent(this, MainActivityDELETE.class);
                i.putExtra("url", url);
                startActivity(i);
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
                    i.putExtra("url", url);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getBaseContext(), "Enter an ID in the URL!", Toast.LENGTH_LONG).show();
                }
            }
            else {
                if(index != -1){
                    Intent i = new Intent(this, MainActivityPUTCAR.class);
                    i.putExtra("url", url);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getBaseContext(), "Enter an ID in the URL!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}