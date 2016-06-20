package com.fixtconsumer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixtconsumer.R;
import com.fixtconsumer.beans.CreditCardBean;
import com.fixtconsumer.listeners.AddressSettingsOperationListener;

import java.util.ArrayList;

/**
 * Created by sahil on 30-05-2016.
 */
public class CardsAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    CreditCardBean tempValues=null;
    Typeface fontfamily;
    AddressSettingsOperationListener listener = null ;
    private boolean isChanged = false;

    /*************  CustomAdapter Constructor *****************/
    public CardsAdapter(Activity a, ArrayList d,Resources resLocal) {

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


        public TextView txtCardName;
        public ImageView check;
        


    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_cards, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txtCardName = (TextView) vi.findViewById(R.id.card_name);
            holder.check = (ImageView) vi.findViewById(R.id.check);

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
            tempValues = ( CreditCardBean ) data.get( position );
//            holder.lbldescription.setText(tempValues.getName());
            holder.txtCardName.setText(tempValues.getFirstname() +" " + tempValues.getLastname() +" "+ tempValues.getCard_number().substring(tempValues.getCard_number().length() - 8,tempValues.getCard_number().length()));
            if (tempValues.getPrimary().equals("true"))
                holder.check.setVisibility(View.VISIBLE);
            else
                holder.check.setVisibility(View.GONE);

        }
        return vi;
    }

}







