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
        ArrayList<Integer> index = new ArrayList<Integer>();
        if (!etName.getText().toString().trim().equals("")) {
            array.add(etName.getText().toString().toLowerCase());
            index.add(0);
        }
        if (!etLastName.getText().toString().trim().equals("")) {
            array.add(etLastName.getText().toString().toLowerCase());
            index.add(1);
        }
        if (!etDNI.getText().toString().trim().equals("")) {
            array.add(etDNI.getText().toString());
            index.add(2);
        }
        if (!etNationality.getText().toString().trim().equals("")) {
            array.add(etNationality.getText().toString().toLowerCase());
            index.add(3);
        }
        Intent intent = new Intent(this, SearchRESULTS.class);
        intent.putExtra("url", "http://192.168.1.112:8080/cars/apiOwner");
        intent.putExtra("array", array);
        intent.putExtra("index", index);
        startActivity(intent);
    }
}
