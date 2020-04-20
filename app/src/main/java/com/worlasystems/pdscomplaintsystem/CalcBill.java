package com.worlasystems.pdscomplaintsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class CalcBill extends AppCompatActivity {

    ImageView img;
    ImageButton retake_img;
    TextView hint_ocr,meter_header;
    Button btnCapture;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_bill);


        img = findViewById(R.id.img);
        hint_ocr = findViewById(R.id.hint_ocr);
        retake_img = findViewById(R.id.retake);
        btnCapture = findViewById(R.id.btnCapture);
        meter_header = findViewById(R.id.header_meter);

    }


    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnCapture:
            case R.id.retake:

                    dispatchTakePictureIntent();
                    hint_ocr.setText("");
                    meter_header.setText("");

                break;
            case R.id.btnDetect:
                detectTextFromImage();

                break;

        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    Bitmap imageBitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);

            retake_img.setVisibility(View.VISIBLE);
        }
    }

    private void detectTextFromImage() {
        try {

            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);


        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
            detector.processImage(FirebaseVisionImage.fromBitmap(imageBitmap))
                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText result) {
                            // Task completed successfully
                            // ...
                            String resultText = "";
                            for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
//                                if (block.getLines().size()==0)
//                                {
//
//                                }
                                resultText = resultText.concat(block.getText());
                            }
                            meter_header.setText(resultText);
                        }
                    })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    // ...
                                }
                            });


        } catch (Exception e) {
            Toast.makeText(CalcBill.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }



}
