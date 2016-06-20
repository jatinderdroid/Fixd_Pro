package com.fixtconsumer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.beans.AddressesBean;
import com.fixtconsumer.listeners.AddressSettingsOperationListener;

import java.util.ArrayList;

/**
 * Created by sahil on 24-05-2016.
 */
public class SettingAddressesAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    AddressesBean tempValues=null;
    Typeface fontfamily;
    AddressSettingsOperationListener listener = null ;
    private boolean isChanged = false;

    /*************  CustomAdapter Constructor *****************/
    public SettingAddressesAdapter(Activity a, ArrayList d,Resources resLocal,AddressSettingsOperationListener listener) {

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
        this.listener = listener;

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

        public TextView txtZip;
        public TextView txtAddress;
        public ImageView btnRadio;
        public TextView btnDelete;
        public TextView btnEdit;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_address, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txtAddress = (TextView) vi.findViewById(R.id.txtAddress);
            holder.txtZip = (TextView) vi.findViewById(R.id.txtZip);
            holder.btnDelete = (TextView) vi.findViewById(R.id.btnDelete);
            holder.btnEdit = (TextView) vi.findViewById(R.id.btnEdit);
            holder.btnRadio = (ImageView) vi.findViewById(R.id.btnRadio);

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
            tempValues = ( AddressesBean ) data.get( position );
//            holder.lbldescription.setText(tempValues.getName());
            holder.txtAddress.setText(tempValues.getAddress());
            holder.txtZip.setText(tempValues.getZip());
            if (isChanged){
                if (tempValues.isChecked())
                    holder.btnRadio.setBackgroundResource(R.drawable.radio_button);
                else
                    holder.btnRadio.setBackgroundResource(R.drawable.white_radiobutton);
            }else {
                if (tempValues.getDefault_address().equals("1"))
                    holder.btnRadio.setBackgroundResource(R.drawable.radio_button);
                else
                    holder.btnRadio.setBackgroundResource(R.drawable.white_radiobutton);
            }

            holder.btnRadio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.handleOperation(position,"select");
                    isChanged = true ;
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.handleOperation(position,"delete");
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.handleOperation(position,"edit");
                }
            });
        }
        return vi;
    }
    public void alreadyLoaded(){
        isChanged = true ;
    }
}






