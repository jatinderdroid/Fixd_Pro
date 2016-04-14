package fixtpro.com.fixtpro.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.beans.AssignTechModal;
import fixtpro.com.fixtpro.beans.RatingListModal;
import fixtpro.com.fixtpro.views.RatingBarView;

/**
 * Created by sahil on 05-03-2016.
 */
public class AssignTechAdapter extends BaseAdapter {
    Context context;
    ArrayList<AssignTechModal> modalList;
    Typeface fontfamily;

    public AssignTechAdapter(Context context, ArrayList<AssignTechModal> modalList) {
        this.context = context;
        this.modalList = modalList;
    }

    @Override
    public int getCount() {
        return modalList.size();
    }

    @Override
    public Object getItem(int position) {
        return modalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        CircleImageView circleImage;
        TextView textTitleName;
        RatingBarView cusRatingbar;
        TextView textAssign;
        TextView textJobSchedule;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.assigntech_listitem,null);

            holder.circleImage = (CircleImageView)convertView.findViewById(R.id.circleImage);
            holder.textTitleName = (TextView)convertView.findViewById(R.id.textTitleName);
            holder.cusRatingbar = (RatingBarView)convertView.findViewById(R.id.cusRatingbar);
            holder.textAssign = (TextView)convertView.findViewById(R.id.textAssign);
            holder.textJobSchedule = (TextView)convertView.findViewById(R.id.textJobSchedule);
            holder.cusRatingbar.setClickable(false);
            // set the typeface ...
            fontfamily = Typeface.createFromAsset(context.getAssets(), "HelveticaNeue-Thin.otf");
            holder.textTitleName.setTypeface(fontfamily);
            holder.textAssign.setTypeface(fontfamily);
            holder.textJobSchedule.setTypeface(fontfamily);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        AssignTechModal modal = (AssignTechModal)getItem(position);
//        holder.circleImage.setImageResource(modal.getImage());
        holder.textTitleName.setText(modal.getFirstName());
//        holder.cusRatingbar.setStar(modal.getRating());
        holder.textJobSchedule.setText(modal.getJobSchedule());
        holder.textAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Assigning", Toast.LENGTH_SHORT).show();
            }
        });
        holder.cusRatingbar.setStar((int)Float.parseFloat(modal.getRating()),true);

        return convertView;
    }
}
