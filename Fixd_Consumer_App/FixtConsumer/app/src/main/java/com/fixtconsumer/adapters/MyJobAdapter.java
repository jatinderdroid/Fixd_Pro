package com.fixtconsumer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixtconsumer.FixdConsumerApplication;
import com.fixtconsumer.R;
import com.fixtconsumer.beans.JobAppliancesModal;
import com.fixtconsumer.beans.JobModal;

import java.util.ArrayList;

/**
 * Created by sahil on 03-06-2016.
 */
public class MyJobAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    JobModal tempValues=null;
    Typeface fontfamily;


    /*************  CustomAdapter Constructor *****************/
    public MyJobAdapter(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;
        fontfamily = Typeface.createFromAsset(activity.getAssets(), "HelveticaNeue-Thin.otf");
//
//            Log.i("", "lstheight"+lstheight);
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return data.size();
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView txt_Problem;
        public TextView txt_JobDate;
        public TextView txt_JobTime;
        public TextView txt_tech_status;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_myjobs, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txt_Problem = (TextView) vi.findViewById(R.id.txt_Problem);
            holder.txt_JobDate = (TextView) vi.findViewById(R.id.txt_JobDate);
            holder.txt_JobTime = (TextView) vi.findViewById(R.id.txt_JobTime);
            holder.txt_tech_status = (TextView) vi.findViewById(R.id.txt_tech_status);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
//            holder.BName.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
        try {
            tempValues = null;
            tempValues = (JobModal) data.get(position);
            holder.txt_Problem.setText(getProblemName(tempValues.getJob_appliances_arrlist()));
            holder.txt_JobDate.setText(tempValues.getRequest_date());
            holder.txt_JobTime.setText(tempValues.getTimeslot_start() + "-" + tempValues.getTimeslot_end());
            if (!tempValues.getStatus().equals("Open") || !tempValues.getStatus().equals("Cancelled"))
                holder.txt_tech_status.setText("Tech Assigned");
            else {

            }
//            holder.txt_Problem.setText(tempValues.);
        }catch (Exception e){
            e.printStackTrace();
        }
            /************  Set Model values in Holder elements ***********/

        }
        return vi;
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




