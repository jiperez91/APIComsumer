package com.example.jiperez.apiconsumer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivitySEARCHOWNER extends  MainActivity {
    EditText etName, etLastName, etDNI, etNationality;
    Button btnSearch;
    Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_owner);

        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etDNI = (EditText) findViewById(R.id.etDNI);
        etNationality = (EditText) findViewById(R.id.etNationality);
        btnSearch = (Button) findViewById(R.id.btnSearch);
    }

    public void onClick(View view) {
        ArrayList<String> array = new ArrayList<String>();
        if(!etName.getText().toString().trim().equals("")){
            array.add(etName.getText().toString().toLowerCase());
        }
        if(!etLastName.getText().toString().trim().equals("")){
            array.add(etLastName.getText().toString().toLowerCase());
        }
        if(!etDNI.getText().toString().trim().equals("")){
            array.add(etDNI.getText().toString());
        }
        if(!etNationality.getText().toString().trim().equals("")){
            array.add(etNationality.getText().toString());
        }
        Intent intent = new Intent(this, SearchRESULTS.class);
        intent.putExtra("url", "http://192.168.1.112:8080/cars/apiOwner");
        intent.putExtra("array", array);
        startActivity(intent);
    }
}
