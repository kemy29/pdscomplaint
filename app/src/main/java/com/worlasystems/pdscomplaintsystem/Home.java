package com.worlasystems.pdscomplaintsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    TextView Welc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }


    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.User_settings:
                startActivity(new Intent(Home.this,MySettings.class));
                break;
            case R.id.Report:
                startActivity(new Intent(Home.this,ReportIssue.class));
                break;
            case R.id.find_office_complainants:
                startActivity(new Intent(Home.this,OfficeLocations.class));
                break;
            case R.id.faq:
                startActivity(new Intent(Home.this,FAQ.class));
                break;
            case R.id.contact:
                startActivity(new Intent(Home.this,Contact.class));
                break;
            case R.id.meter_mgt:
                startActivity(new Intent(Home.this,MeterMgt.class));
                break;
                default:
                    Toast.makeText(Home.this,"UnknownError",Toast.LENGTH_LONG).show();

        }
    }
}
