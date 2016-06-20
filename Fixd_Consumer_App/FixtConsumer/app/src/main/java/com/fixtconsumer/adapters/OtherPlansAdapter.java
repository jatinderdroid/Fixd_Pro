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
import com.fixtconsumer.beans.ApplianceTypeBeans;
import com.fixtconsumer.beans.SimplePlansBean;

import java.util.ArrayList;

/**
 * Created by sahil on 20-05-2016.
 */
public class OtherPlansAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ApplianceTypeBeans tempValues=null;
    Typeface fontfamily;


    /*************  CustomAdapter Constructor *****************/
    public OtherPlansAdapter(Activity a, ArrayList d, Resources resLocal) {

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

        public TextView txtEquipName;
//        public TextView txtItemCoverd;
//        public ImageView imgPlus;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_otherplan, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txtEquipName = (TextView) vi.findViewById(R.id.txtEquipName);
//            holder.txtItemCoverd = (TextView) vi.findViewById(R.id.txtItemCoverd);
//            holder.imgPlus = (ImageView) vi.findViewById(R.id.imgPlus);


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
            tempValues = (ApplianceTypeBeans) data.get( position );
            holder.txtEquipName.setText(tempValues.getName());

//            holder.txtItemCoverd.setText(getItems(tempValues.getArrayList()));
//            if (position == data.size()-1){
//                holder.imgPlus.setVisibility(View.GONE);
//            }else {
//                holder.imgPlus.setVisibility(View.VISIBLE);
//            }
//            holder.lblAppliance.setTypeface(fontfamily);
//            holder.value.setText(tempValues.getQuantity());

            /************  Set Model values in Holder elements ***********/

        }
        return vi;
    }
    private String  getItems(ArrayList<ApplianceTypeBeans> list) {
        String type = "";
        for (int i = 0 ; i <list.size() ; i++){
            type = list.get(i).getName()  +  "," + type;
        }
        return type ;
    }
}





