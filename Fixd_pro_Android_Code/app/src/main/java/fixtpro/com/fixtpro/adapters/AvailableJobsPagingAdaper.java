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


public class AvailableJobsPagingAdaper extends PagingBaseAdapter<AvailableJobModal> {
    ArrayList<AvailableJobModal> list;
    Activity  activity;
    private static LayoutInflater inflater=null;
    public Resources res;
    AvailableJobModal tempValues=null;
    Typeface fontfamily;

    public  AvailableJobsPagingAdaper(Activity  activity, ArrayList<AvailableJobModal> list, Resources res){
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

        public TextView contactName, address, appliance_types_name_and_service_type;
        public TextView day_request_date, timeinterval;

    }
    /****** Depends upon items size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.available_jobs_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.contactName = (TextView) vi.findViewById(R.id.contactName);
            holder.address = (TextView) vi.findViewById(R.id.address);
            holder.appliance_types_name_and_service_type = (TextView) vi.findViewById(R.id.appliance_types_name_and_service_type);
            holder.day_request_date = (TextView) vi.findViewById(R.id.day_request_date);
            holder.timeinterval = (TextView) vi.findViewById(R.id.timeinterval);
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
            /************  Set Model values in Holder elements ***********/
            holder.contactName.setText(tempValues.getContact_name());
            holder.day_request_date.setText(Utilities.convertDate(tempValues.getRequest_date()));
            holder.timeinterval.setText(Utilities.Am_PMFormat(tempValues.getTimeslot_start())+" - "+Utilities.Am_PMFormat(tempValues.getTimeslot_end()));
            holder.address.setText(tempValues.getJob_customer_addresses_address()+" - "+tempValues.getJob_customer_addresses_city()+","+tempValues.getJob_customer_addresses_state());
            String STR_appliance_types_name_and_service_type = "";
            for(int j = 0; j < tempValues.getJob_appliances_arrlist().size(); j++)
            {
                STR_appliance_types_name_and_service_type = STR_appliance_types_name_and_service_type + tempValues.getJob_appliances_arrlist().get(j).getAppliance_type_name()+" "+tempValues.getService_type()+" ";
            }
            holder.appliance_types_name_and_service_type.setText(STR_appliance_types_name_and_service_type);
            STR_appliance_types_name_and_service_type = "";
        }
        return vi;
    }

}
