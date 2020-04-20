package com.worlasystems.pdscomplaintsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Contact extends AppCompatActivity {

    FloatingActionButton call_office;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        call_office = findViewById(R.id.call_office);
        call_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_intent = new Intent(Intent.ACTION_CALL);
                call_intent.setData(Uri.parse("tel:0305660193"));


                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                   requestPermission();
                }else{
                    startActivity(call_intent);
                }

            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Contact.this,new String[]{
                Manifest.permission.CALL_PHONE
        },1);
    }
}
