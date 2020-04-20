package com.worlasystems.pdscomplaintsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MySettings extends AppCompatActivity {

    FirebaseAuth mAuth;


    TextView tv_name,tv_email,tv_hse_num,tv_mtr_num,tv_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        mAuth = FirebaseAuth.getInstance();


        tv_name = findViewById(R.id.name_);
        tv_email = findViewById(R.id.email_);
        tv_hse_num = findViewById(R.id.hse_number_);
        tv_mtr_num= findViewById(R.id.meter_number_);
        tv_tel = findViewById(R.id.phone_);



    }

    public void details_editing(View v){
        switch (v.getId())
        {

            case R.id.name_:
                 mAlertDialog("Edit Name:", ((TextView) v).getText().toString());
                 break;
            case R.id.email_:
                mAlertDialog("Edit Email:", ((TextView) v).getText().toString());
                break;
            case R.id.hse_number_:
                mAlertDialog("Edit House Number:", ((TextView) v).getText().toString());
                break;
            case R.id.meter_number_:
                mAlertDialog("Edit Meter Number:", ((TextView) v).getText().toString());
                break;
            case R.id.phone_:
                mAlertDialog("Edit Telephone Number:", ((TextView) v).getText().toString());
                break;

                default:
                    Toast.makeText(MySettings.this,"Unknown Error",Toast.LENGTH_LONG).show();
        }
    }


    public void account_mgt(View view)
    {
        switch (view.getId())
        {
            case R.id.signout:

                mAuth.signOut();
                Intent loginIntent = new Intent(MySettings.this,MainActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                break;

        }
    }

    private void mAlertDialog(String title,String value)
    {

        try {
            final View view = getLayoutInflater().inflate(R.layout.edit_details,null);
            final EditText editText = view.findViewById(R.id.edit_details_edt);
            editText.setText(value);
            if (title.equals("Edit Email:"))
            {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
            if (title.equals("Edit Meter Number:"))
            {
                editText.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
            }
            if (title.equals("Edit Telephone Number:"))
            {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(title)
                    .setView(view)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton("Make Change", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
            dialog.create().show();
        }catch (Exception e)
        {
            Toast.makeText(MySettings.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
