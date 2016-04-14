package fixtpro.com.fixtpro.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import fixtpro.com.fixtpro.R;
import fixtpro.com.fixtpro.beans.JobAppliancesModal;

/**
 * Created by sahil on 16-03-2016.
 */
public class HorizontalScrollApplianceAdapter extends ArrayAdapter<JobAppliancesModal> {
    private LayoutInflater mInflater;
    private ArrayList<JobAppliancesModal> list ;
    Context context = null ;
    ImageLoader imageLoader ;
    public HorizontalScrollApplianceAdapter(Context context,ArrayList<JobAppliancesModal> list) {
        super(context, R.layout.jobs_image_title_item, list);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context ;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//        / Load image, decode it to Bitmap and return Bitmap to callback

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.jobs_image_title_item, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.textView = (TextView) convertView.findViewById(R.id.txtTypeTitle);
            holder.imageView  = (ImageView)convertView.findViewById(R.id.imgType);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text

        imageLoader.loadImage(getItem(position).getImg_original(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Do whatever you want with Bitmap
                holder.textView.setText(getItem(position).getAppliance_type_name());
                holder.imageView.setImageBitmap(loadedImage);
            }
        });
        // Set the color


        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView textView;
        public ImageView imageView ;
    }
}
