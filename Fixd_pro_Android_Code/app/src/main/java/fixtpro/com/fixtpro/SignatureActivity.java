package fixtpro.com.fixtpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import fixtpro.com.fixtpro.utilites.CurrentScheduledJobSingleTon;
import fixtpro.com.fixtpro.utilites.Singleton;

public class SignatureActivity extends AppCompatActivity {
    ImageView imgClose,imgAccept,imgClear;
    CurrentScheduledJobSingleTon singleTon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_signature);
        singleTon = CurrentScheduledJobSingleTon.getInstance();
        setWidgets();
        setListeners();
    }
    private void setWidgets(){
        imgClose = (ImageView)findViewById(R.id.imgClose);
        imgClear = (ImageView)findViewById(R.id.imgClear);
        imgAccept = (ImageView)findViewById(R.id.imgAccept);
    }
    private void setListeners(){
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignatureActivity.this, SignatureActivity.class);
                startActivity(intent);
                finish();
            }
        });imgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleTon.getCurrentReapirInstallProcessModal().setIsCompleted(true);
                singleTon.getInstallOrRepairModal().getSignature().setSignature_path("");
                Intent intent = new Intent();
                setResult(200,intent);
                finish();
            }
        });
    }
}
