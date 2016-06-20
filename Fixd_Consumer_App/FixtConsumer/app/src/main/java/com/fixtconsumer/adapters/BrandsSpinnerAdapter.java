package com.fixtconsumer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.beans.Brands;

import java.util.ArrayList;

/**
 * Created by sahil on 20-06-2016.
 */
public class BrandsSpinnerAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList data;
    public Resources res;
    Brands tempValues=null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public BrandsSpinnerAdapter(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList objects,
            Resources resLocal
    )
    {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        res      = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.item_brands, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (Brands) data.get(position);

        TextView label  = (TextView)row.findViewById(R.id.txtBrandName);
               // Set values for spinner each row
            label.setText(tempValues.getBrand_name());

        return row;
    }
}

