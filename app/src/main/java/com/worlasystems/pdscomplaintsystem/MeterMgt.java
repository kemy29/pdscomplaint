package com.worlasystems.pdscomplaintsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MeterMgt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_mgt);
    }

    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.Calc_Bill:
                startActivity(new Intent(MeterMgt.this,CalcBill.class));
                break;

        }
    }
}
