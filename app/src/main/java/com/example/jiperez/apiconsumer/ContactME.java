package com.example.jiperez.apiconsumer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactME extends MainActivity {
    EditText etNameContact, etMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);

        etNameContact = (EditText) findViewById(R.id.etNameContact);
        etMessage = (EditText) findViewById(R.id.etMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
    }

    public void onClick(View view) {
        if(!validate())
            Toast.makeText(getBaseContext(), "Please enter all fields!", Toast.LENGTH_LONG).show();
        else {
            Intent itSend = new Intent(Intent.ACTION_SEND);
            itSend.putExtra(Intent.EXTRA_EMAIL, new String[]{"juanignacioperez91@gmail.com"});
            itSend.putExtra(Intent.EXTRA_TEXT, "Hi! I'm " + etNameContact.getText().toString() + ":" + "\n" + "\n" + etMessage.getText().toString());
            itSend.setType("message/rfc822");
            try {
                startActivity(Intent.createChooser(itSend, "Send message using..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "No email clients installed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validate(){
        if(etNameContact.getText().toString().trim().equals(""))
            return false;
        else if(etMessage.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }
}
