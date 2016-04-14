package fixtpro.com.fixtpro.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.beans.install_repair_beans.RepairType;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by sahil on 07-04-2016.
 */
public class RepairTypeAdapter extends BaseAdapter implements Filterable {

    /**
     * ******** Declare Used Variables ********
     */
    private Activity activity;
    private ArrayList<RepairType> originalData;
    private ArrayList<RepairType> filteredData;
    // Typeface  Font = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Light.ttf");
    // private ArrayList<Product> filteredData; // Original Values
    //  private ArrayList<Product> mDisplayedValues;    // Values to be displayed
    // LayoutInflater inflater;
    private static LayoutInflater inflater = null;
    public Resources res;
    RepairType tempValues = null;
    ArrayList<RepairType> FilteredArrList;
    /**
     * **********  CustomAdapter Constructor ****************
     */
    public RepairTypeAdapter(Activity a, ArrayList<RepairType> d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        originalData = d;
        filteredData = d;
        res = resLocal;
//        Font = Typeface.createFromAsset(activity.getAssets(), "fonts/RobotoCondensed-Light.ttf");

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * ***** What is the size of Passed Arraylist Size ***********
     */
    public int getCount() {

        if (originalData.size() <= 0)
            return originalData.size();
        return originalData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    /**
     * ****** Create a holder Class to contain inflated xml file elements ********
     */
    public static class ViewHolder {
        public TextView txtType;
        public TextView txtPrice;
    }

    /**
     * *** Depends upon originalData size called for each row , Create each ListView row ****
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;


        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_installtype, null);


            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();


            holder.txtType = (TextView) vi.findViewById(R.id.txtType);
            holder.txtPrice = (TextView) vi.findViewById(R.id.txtPrice);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();
        if (originalData.size() <= 0) {
//           holder.Type.setText("No originalData");
        } else
        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = originalData.get(position);
        holder.txtType.setText(tempValues.getType());
        holder.txtPrice.setText("$"+((int)Float.parseFloat(tempValues.getPrice())+""));
        return vi;
    }


    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                originalData = (ArrayList<RepairType>) results.values; // has the filtered values
                notifyDataSetChanged();
                // notifies the originalData with new filtered value
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                FilteredArrList = new ArrayList<RepairType>();

                if (filteredData == null) {
                    filteredData = originalData; // saves the original originalData in filteredData

                    Log.e("", "" + filteredData);
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the filteredData(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = filteredData.size();
                    results.values = filteredData;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < filteredData.size(); i++) {
                        String originalData1 = filteredData.get(i).getType();
                        if (originalData1.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new RepairType (filteredData.get(i).getType()));
                        }
                    }
                    // set the Filtered result to return
                    //  results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
    public ArrayList<RepairType> filterArray(){
        return  FilteredArrList;
    }

}