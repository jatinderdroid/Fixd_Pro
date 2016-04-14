package fixtpro.com.fixtpro.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;
import fixtpro.com.fixtpro.beans.TechnicianModal;

/**
 * Created by sahil on 05-04-2016.
 */
public class InstallOrRepairJobAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    JobAppliancesModal tempValues=null;
    Typeface fontfamily;

    /*************  CustomAdapter Constructor *****************/
    public InstallOrRepairJobAdapter(Activity a, ArrayList d, Resources resLocal) {

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

        public TextView txtInstaller;
        public ImageView imgStatus;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_clickaddservices, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txtInstaller = (TextView) vi.findViewById(R.id.txtInstaller);
            holder.imgStatus = (ImageView) vi.findViewById(R.id.imgStatus);
            holder.txtInstaller.setTypeface(fontfamily);
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
            tempValues = ( JobAppliancesModal ) data.get( position );
            holder.txtInstaller.setText(tempValues.getAppliance_type_name() + " " +tempValues.getJob_appliances_service_type());
            if (tempValues.isProcessCompleted()){
                holder.imgStatus.setBackgroundResource(R.drawable.green_check);
            }else {
                holder.imgStatus.setBackgroundResource(R.drawable.explanation);
            }
        }
        return vi;
    }

}



