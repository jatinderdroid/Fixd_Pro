package com.fixtconsumer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.fixtconsumer.R;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView cancel, email, call, feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setWidgets();
        setListeners();
    }

    public void setWidgets(){
        cancel = (ImageView)findViewById(R.id.cancel);
        email = (ImageView)findViewById(R.id.email);
        call = (ImageView)findViewById(R.id.call);
        feedback = (ImageView)findViewById(R.id.feedback);
    }

    public void setListeners(){
        cancel.setOnClickListener(this);
        email.setOnClickListener(this);
        call.setOnClickListener(this);
        feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "support@fixdrepair.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fixd-Comsumer App Support(Ver: 1.0)");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.call:
                String number = "tel:" + "80-01111111";
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                startActivity(callIntent);
                break;
            case R.id.feedback:
                Intent feedbackemailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "feedback@fixdrepair.com", null));
                feedbackemailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fixd-Consumer App Feedback(Ver: 1.0)");
                startActivity(Intent.createChooser(feedbackemailIntent, "Send email..."));
                break;
        }
    }
}
