package fixtpro.com.fixtpro.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paging.listview.PagingBaseAdapter;

import java.util.ArrayList;

import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.beans.AvailableJobModal;
import fixtpro.com.fixtpro.utilites.Utilities;

/**
 * Created by sahil on 09-03-2016.
 */
public class PaymentsJobsPagingAdaper extends PagingBaseAdapter<AvailableJobModal> {
    ArrayList<AvailableJobModal> list;
    Activity activity;
    private static LayoutInflater inflater=null;
    public Resources res;
    AvailableJobModal tempValues=null;
    Typeface fontfamily;

    public  PaymentsJobsPagingAdaper(Activity  activity, ArrayList<AvailableJobModal> list, Resources res){
        this.list = list ;
        this.activity = activity;
        this.res = res ;
        fontfamily = Typeface.createFromAsset(activity.getAssets(), "HelveticaNeue-Thin.otf");
//
//            Log.i("", "lstheight"+lstheight);
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        if(items.size()<=0)
            return items.size();
        return items.size();
    }
    @Override
    public AvailableJobModal getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView txtName, txtJobNo, txtRate;


    }
    /****** Depends upon items size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.payment_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();

            holder.txtName = (TextView) vi.findViewById(R.id.txtName);
            holder.txtRate = (TextView) vi.findViewById(R.id.txtRate);
            holder.txtJobNo = (TextView) vi.findViewById(R.id.txtJobNo);
            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(items.size()<=0)
        {
//            holder.BName.setText("No items");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues =  items.get( position );
            /************  Set Typeface ***********/
            holder.txtName.setTypeface(fontfamily);
            holder.txtJobNo.setTypeface(fontfamily);
            holder.txtRate.setTypeface(fontfamily);
            /************  Set Model values in Holder elements ***********/
            holder.txtName.setText(tempValues.getContact_name());
            holder.txtJobNo.setText("Job # "+tempValues.getId());
            holder.txtRate.setText("$" + tempValues.getCost_details_pro_earned());

        }
        return vi;
    }

}

