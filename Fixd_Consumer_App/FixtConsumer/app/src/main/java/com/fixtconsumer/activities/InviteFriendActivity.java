package com.fixtconsumer.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.utils.CheckAlertDialog;

public class InviteFriendActivity extends AppCompatActivity {
    Context _context = InviteFriendActivity.this;
    String TAG = "InviteFriendActivity";
    TextView txtEmailInvite,txtTextInvite,details;
    ImageView cancel;
    CheckAlertDialog checkALert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        checkALert = new CheckAlertDialog();
        setWidgetsIds();
        setListeners();

    }
    private void setListeners(){
        txtEmailInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InviteFriendActivity.this,InviteByEmailActivity.class);
                startActivity(intent);
            }
        });
        txtTextInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InviteFriendActivity.this,InviteByPhoneActivity.class);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkALert.showcheckAlert(InviteFriendActivity.this,getResources().getString(R.string.alert_title),"Every time you refer friend, and they use the Fixd app, you will receive $25 off on your next service, which will be applied to your account automatically. Any credit is not used will be applied to future services.");
            }
        });
    }
    private void setWidgetsIds() {
        txtEmailInvite = (TextView)findViewById(R.id.txtEmailInvite);
        details = (TextView)findViewById(R.id.details);
        txtTextInvite = (TextView)findViewById(R.id.txtTextInvite);
        cancel = (ImageView)findViewById(R.id.cancel);
    }
}
