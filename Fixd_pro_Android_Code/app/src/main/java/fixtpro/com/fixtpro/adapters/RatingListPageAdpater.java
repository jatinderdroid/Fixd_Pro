package fixtpro.com.fixtpro.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paging.listview.PagingBaseAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.beans.RatingListModal;
import fixtpro.com.fixtpro.utilites.Utilities;
import fixtpro.com.fixtpro.views.RatingBarView;

/**
 * Created by sahil on 10-03-2016.
 */
public class RatingListPageAdpater extends PagingBaseAdapter<RatingListModal> {
    ArrayList<RatingListModal> list;
    Activity activity;
    private static LayoutInflater inflater=null;
    public Resources res;
    RatingListModal tempValues=null;
    Typeface fontfamily;

    public  RatingListPageAdpater(Activity  activity, ArrayList<RatingListModal> list, Resources res){
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
    public RatingListModal getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView txtUserName, txtComments ,txtRateingInterval;
        public fixtpro.com.fixtpro.views.RatingBarView rating;

    }
    /****** Depends upon items size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.ratinglistview_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.rating = (RatingBarView) vi.findViewById(R.id.custom_ratingbar);
            holder.txtComments = (TextView) vi.findViewById(R.id.txtComments);
            holder.txtRateingInterval = (TextView) vi.findViewById(R.id.txtRateingInterval);
            holder.txtUserName = (TextView) vi.findViewById(R.id.txtUserName);
            holder.rating.setClickable(false);
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
            holder.txtUserName.setText(tempValues.getCustomers_first_name() + " " + tempValues.getCustomers_last_name());
            holder.txtComments.setText(tempValues.getComments());
            holder.rating.setStar((int) Float.parseFloat(tempValues.getRatings()), true);
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            long time1 = cal.getTimeInMillis();
            long time2 = 0;
            try {
                time2 = Utilities.getTimeInMilliseconds(tempValues.getCreated_at()) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference = time1 - time2 ;
            Log.e("", "difference" + difference);
            if (!tempValues.getCreated_at().equals("0000-00-00 00:00:00"))
            holder.txtRateingInterval.setText(Utilities.timeMedthod(difference/1000l));
            else
            holder.txtRateingInterval.setVisibility(View.INVISIBLE);
            /************  Set Model values in Holder elements ***********/

        }
        return vi;
    }

}

