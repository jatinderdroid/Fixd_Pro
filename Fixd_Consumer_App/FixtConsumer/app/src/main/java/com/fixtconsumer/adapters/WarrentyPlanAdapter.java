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

import com.fixtconsumer.R;
import com.fixtconsumer.beans.WarrentyPlanBean;

import java.util.ArrayList;

/**
 * Created by sahil on 06-05-2016.
 */
public class WarrentyPlanAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    WarrentyPlanBean tempValues=null;
    Typeface fontfamily;


    /*************  CustomAdapter Constructor *****************/
    public WarrentyPlanAdapter(Activity a, ArrayList d,Resources resLocal) {

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

        public TextView lblprice;
        public TextView lblname;
        public TextView lbldescription;
        public LinearLayout container_layout;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_warrenty_plans, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.lblprice = (TextView) vi.findViewById(R.id.lblprice);
            holder.lblname = (TextView) vi.findViewById(R.id.lblname);
            holder.lbldescription = (TextView) vi.findViewById(R.id.lbldescription);
            holder.container_layout = (LinearLayout) vi.findViewById(R.id.container_layout);

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
            tempValues=null;
            tempValues = ( WarrentyPlanBean ) data.get( position );
//            holder.lbldescription.setText(tempValues.getName());
            holder.lblname.setText(tempValues.getName());
            holder.lblprice.setText("$"+tempValues.getPrice_inclusive_tax()+"/mo.");
            if (tempValues.getName().equals("Complete")){
                holder.container_layout.setBackgroundResource(R.drawable.img_complete_bg);

            }else if (tempValues.getName().equals("System")){
                holder.container_layout.setBackgroundResource(R.drawable.img_system_bg);
            }else if (tempValues.getName().equals("Electronics")){
                holder.container_layout.setBackgroundResource(R.drawable.img_electronics_bg);
            }else if (tempValues.getName().equals("Appliance")){
                holder.container_layout.setBackgroundResource(R.drawable.img_appliance_bg);
            }
            if (tempValues.getName().equals("Complete")){
                String plan = "";
                for (int i = 0 ; i < tempValues.getSimplePlansBeanArrayList().size() ; i++){
                    plan = tempValues.getSimplePlansBeanArrayList().get(i).getName() +" + "+plan;
                }
                holder.lbldescription.setText(plan);
            }else {
                String plan = "";
                for (int i = 0 ; i < tempValues.getApplianceTypeBeansArrayList().size() ; i++){
                    plan = tempValues.getApplianceTypeBeansArrayList().get(i).getName() + ", " +plan;
                }
                holder.lbldescription.setText(plan);
            }

//            holder.lblprice.setTypeface(fontfamily);
//            holder.lblname.setTypeface(fontfamily);
//            holder.lbldescription.setTypeface(fontfamily);
            /************  Set Model values in Holder elements ***********/

        }
        return vi;
    }

}



