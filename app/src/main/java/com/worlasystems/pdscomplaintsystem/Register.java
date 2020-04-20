package com.worlasystems.pdscomplaintsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    TextView existing_user;
    EditText house_number,meter_number,edt_register_password,edt_register_email,fname,lname,phone;
    RadioButton male_radio,female_radio;
    Button btn_register;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();


        existing_user = findViewById(R.id.existing_user);
        house_number = findViewById(R.id.edt_register_house_no);
        meter_number = findViewById(R.id.edt_register_meter_no);
        edt_register_password = findViewById(R.id.edt_register_password);
        edt_register_email = findViewById(R.id.edt_register_email);
        phone = findViewById(R.id.edt_register_phone);
        lname = findViewById(R.id.edt_register_lname);
        fname = findViewById(R.id.edt_register_fname);
        male_radio = findViewById(R.id.male_radio);
        female_radio = findViewById(R.id.female_radio);

        btn_register = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    progressDialog.setTitle("Registration");
                    progressDialog.setMessage("Please wait while we setup your account.");
                    progressDialog.setCanceledOnTouchOutside(false);
                    String first_name = fname.getText().toString();
                    String last_name = lname.getText().toString();
                    String email = edt_register_email.getText().toString();
                    String contact = phone.getText().toString();
                    String password = edt_register_password.getText().toString();
                    String hse_no = house_number.getText().toString();
                    String mtr_no = meter_number.getText().toString();
                    String gender = "";
                    if (male_radio.isChecked())
                        gender = "Male";
                    if (female_radio.isChecked())
                        gender = "Female";

                    if (first_name.equals("") || last_name.equals("") ||
                            email.equals("") || contact.equals("") ||
                            password.equals("") || hse_no.equals("")
                            || mtr_no.equals("")) {
                        Snackbar.make(btn_register, "Field is empty!", Snackbar.LENGTH_LONG).show();
                        return;
                    }


                    if (isEmailValid(email)){
                        if (password.length() > 6) {

                            registerUser(first_name,last_name,gender,contact,hse_no,mtr_no,email,password);
                            Intent homeIntent = new Intent(Register.this, Home.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Snackbar.make(btn_register, "Password is too short!", Snackbar.LENGTH_LONG).show();
                            return;
                        }

                    } else {
                        Snackbar.make(btn_register, "Invalid email address!", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }catch (Exception e)
                {
                    Toast.makeText(Register.this, e.toString(), Toast.LENGTH_LONG).show();

                }
            }

        });


        existing_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, MainActivity.class));
                finish();
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null)
        {
            Intent homeIntent = new Intent(Register.this, Home.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    private void registerUser(final String f_name, final String l_name, final String gen_der, final String tel_nu, final String hse_nu, final String mtr_nu, final String e_mail, String p_word) {
            progressDialog.show();
        mAuth.createUserWithEmailAndPassword(e_mail, p_word)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            String uid = user.getUid();
                            myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("firstname",f_name);
                            userMap.put("lname",l_name);
                            userMap.put("gender",gen_der);
                            userMap.put("email",e_mail);
                            userMap.put("phone",tel_nu);
                            userMap.put("house_number",hse_nu);
                            userMap.put("meter_number",mtr_nu);

                            myRef.setValue(userMap);
                            progressDialog.dismiss();
                            Intent homeIntent = new Intent(Register.this, Home.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(homeIntent);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    public boolean isEmailValid(String email)
    {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

}
