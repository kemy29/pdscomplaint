package com.worlasystems.pdscomplaintsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReportIssue extends AppCompatActivity {

    TextView add_image,website_link;
    RelativeLayout attached_image;
    EditText location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        attached_image = findViewById(R.id.img_lyt);
        website_link = findViewById(R.id.website_link);
        website_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse("https://www.ecgonline.info"));
                startActivity(webIntent);
            }
        });


        location = findViewById(R.id.Location);
        location.setEnabled(false);
        location.setVisibility(View.GONE);

        add_image = findViewById(R.id.add_image);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vis = attached_image.getVisibility();
                if (vis == View.GONE)
             attached_image.setVisibility(View.VISIBLE);
            }
        });
    }


    public void location_edt(View v)
    {
        switch(v.getId())
        {
            case R.id.default_radio:
                location.setText("");
                location.setVisibility(View.GONE);
                break;
            case R.id.new_radio:
                location.setEnabled(true);
                location.setVisibility(View.VISIBLE);
        }

    }
}
