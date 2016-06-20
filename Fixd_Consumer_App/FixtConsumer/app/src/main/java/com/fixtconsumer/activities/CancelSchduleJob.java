package com.fixtconsumer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.beans.JobAppliancesModal;
import com.fixtconsumer.beans.JobModal;
import com.fixtconsumer.singletons.MyJobsSingleton;

import java.util.ArrayList;

public class CancelSchduleJob extends AppCompatActivity {
    int position = 0 ;
    TextView txtCancelJob, txt_repair_type, txt_job_Date, txt_job_Time;
    ImageView cancel;
    MyJobsSingleton singleton = MyJobsSingleton.getInstance();
    JobModal modal = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_schdule_job);
        setWidgets();
        setListeners();
        if (getIntent() != null){
            position = getIntent().getIntExtra("position",0);
            modal = singleton.getSchedulejoblist().get(position);
            initLayout();
        }
    }
    private void setWidgets(){
        txtCancelJob = (TextView)findViewById(R.id.txtCancelJob);
        txt_repair_type = (TextView)findViewById(R.id.txt_repair_type);
        txt_job_Date = (TextView)findViewById(R.id.txt_job_Date);
        txt_job_Time = (TextView)findViewById(R.id.txt_job_Time);
        cancel = (ImageView)findViewById(R.id.cancel);
    }
    private void setListeners(){
        txtCancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CancelSchduleJob.this,ConfirmCancelSchduleJob.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initLayout(){
        txt_job_Time.setText(modal.getTimeslot_start() +" - " +modal.getTimeslot_end());
        txt_job_Date.setText(modal.getRequest_date());
        txt_repair_type.setText(getProblemName(modal.getJob_appliances_arrlist()));
    }
    private String getProblemName(ArrayList<JobAppliancesModal> list){
        String name = "";
        if (list.size() == 1)
            if (!list.get(0).getJob_appliances_service_type().equals("Re Key"))
                return list.get(0).getAppliance_type_name() +" "+list.get(0).getJob_appliances_service_type();
            else
                return list.get(0).getJob_appliances_service_type();
        else for (int i = 0 ; i < list.size() ; i++){
            if (!list.get(0).getJob_appliances_service_type().equals("Re Key"))
                name = list.get(i).getAppliance_type_name() +" "+list.get(i).getJob_appliances_service_type() + ", "+ name ;
            else  list.get(i).getJob_appliances_service_type();
        }
        return "Re Key";
    }
}
