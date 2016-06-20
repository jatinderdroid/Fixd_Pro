package com.fixtconsumer.adapters;

/**
 * Created by sahil on 04-05-2016.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fixtconsumer.R;

import java.util.ArrayList;
public class ImageViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    ImageView imgflag;
    LayoutInflater inflater;
    Activity activity;
    ArrayList<Integer> ary;
    public ImageViewPagerAdapter(Activity activity, ArrayList<Integer> ary) {
        this.activity = activity;
        this.ary = ary ;
    }

    @Override
    public int getCount() {

        return ary.size();
    }

    public float getPageWidth(int position) {
        return 1.0f;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = null;
        try{

            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            itemView = inflater.inflate(R.layout.login_signup_pager_item, container,
                    false);
            imgflag = (ImageView) itemView.findViewById(R.id.imgPager);
            imgflag.setBackgroundResource(ary.get(position));

        }catch(Exception e){
            e.printStackTrace();
        }
        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
