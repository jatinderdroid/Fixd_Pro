package fixtpro.com.fixtpro.adapters;

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

import java.util.ArrayList;

import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.beans.SkillTrade;

/**
 * Created by sahil on 05-04-2016.
 */
public class AddServiceAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    SkillTrade tempValues=null;
    Typeface fontfamily;


    /*************  CustomAdapter Constructor *****************/
    public AddServiceAdapter(Activity a, ArrayList d,Resources resLocal) {

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

        public TextView TradeSkill;
        public LinearLayout skill_container;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_addservices, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.TradeSkill = (TextView) vi.findViewById(R.id.txtInstaller);
            
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
            tempValues = ( SkillTrade ) data.get( position );
            holder.TradeSkill.setText(tempValues.getTitle());
            holder.TradeSkill.setTypeface(fontfamily);
            /************  Set Model values in Holder elements ***********/

        }
        return vi;
    }

}


