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
import com.fixtconsumer.beans.AppliancesModal;

import java.util.ArrayList;



/**
 * Created by sahil on 06-04-2016.
 */
public class AppliancesAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    AppliancesModal tempValues=null;
    Typeface fontfamily;


    /*************  CustomAdapter Constructor *****************/
    public AppliancesAdapter(Activity a, ArrayList d, Resources resLocal) {

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

        public TextView lblAppliance;
        public TextView minus;
        public TextView value;
        public TextView plus;
        public LinearLayout quantity_container;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_appliances, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.lblAppliance = (TextView) vi.findViewById(R.id.lblname);
            holder.value = (TextView) vi.findViewById(R.id.values);
            holder.plus = (TextView) vi.findViewById(R.id.plus);
            holder.minus = (TextView) vi.findViewById(R.id.minus);
            holder.quantity_container = (LinearLayout) vi.findViewById(R.id.quantity_container);

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
            tempValues = (AppliancesModal) data.get( position );
            holder.lblAppliance.setText(tempValues.getName());
            holder.lblAppliance.setTypeface(fontfamily);
            holder.value.setText(tempValues.getQuantity());
            if (!FixdConsumerApplication.projectType.equals("Repair")){
                holder.quantity_container.setVisibility(View.VISIBLE);
            }else {
                holder.quantity_container.setVisibility(View.GONE);
            }
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.value.setText(getValue( ((AppliancesModal) data.get( position )).getQuantity(),"minus"));
                    ((AppliancesModal) data.get( position )).setQuantity(holder.value.getText().toString());
                }
            });
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.value.setText(getValue( ((AppliancesModal) data.get( position )).getQuantity(),"plus"));
                    tempValues.setQuantity(holder.value.getText().toString());
                    ((AppliancesModal) data.get( position )).setQuantity(holder.value.getText().toString());
                }
            });
            /************  Set Model values in Holder elements ***********/

        }
        return vi;
    }
private String getValue(String value,String operation){
    int newVal = Integer.parseInt(value);
    if (operation.equals("minus")){
        if (newVal > 0){
            newVal =  newVal - 1;
        }
    }else {
        newVal = newVal + 1;
    }
    return newVal+"";

}
}



