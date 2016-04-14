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
import fixtpro.com.fixtpro.beans.TechnicianModal;

/**
 * Created by sony on 11-02-2016.
 */
public class TechnicianAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    TechnicianModal tempValues=null;
    Typeface fontfamily;


    /*************  CustomAdapter Constructor *****************/
    public TechnicianAdapter(Activity a, ArrayList d,Resources resLocal) {

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

        public TextView txtTechname;
        public TextView txtPhone;
        public TextView txtEmail;
        public ImageView imgTech;
        public LinearLayout edit;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.technicion_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txtTechname = (TextView) vi.findViewById(R.id.txtTechname);
            holder.txtPhone = (TextView) vi.findViewById(R.id.txtPhone);
            holder.txtEmail = (TextView) vi.findViewById(R.id.txtEmail);
            holder.imgTech = (ImageView) vi.findViewById(R.id.imgTech);
            holder.edit = (LinearLayout)vi.findViewById(R.id.edit);
            holder.txtTechname.setTypeface(fontfamily);
            holder.txtPhone.setTypeface(fontfamily);
            holder.txtEmail.setTypeface(fontfamily);
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
            tempValues = ( TechnicianModal ) data.get( position );
            holder.txtTechname.setText(tempValues.getFirstName() +" " +tempValues.getLastName());
            holder.txtPhone.setText(tempValues.getPhone());
            holder.txtEmail.setText(tempValues.getEmail());

//            holder.TradeSkill.setTypeface(fontfamily);
//            if (tempValues.isChecked()){
//                holder.TradeSkill.setBackgroundColor(res.getColor(R.color.grey_skill_back));
//            }else{
//                holder.TradeSkill.setBackgroundColor(res.getColor(android.R.color.transparent));
//            }
            /************  Set Model values in Holder elements ***********/

        }
        return vi;
    }

}


