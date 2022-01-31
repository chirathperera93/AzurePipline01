package com.ayubo.life.ayubolife.lifeplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.ayubo.life.ayubolife.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Chirath Perera on 2021-08-03.
 */
public class SliderAdapter extends PagerAdapter {
    private Context context;
    private List<SliderData> theSlideItemsModelClassList;
    private SliderAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(SliderAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public SliderAdapter(Context context, List<SliderData> theSlideItemsModelClassList) {
        this.context = context;
        this.theSlideItemsModelClassList = theSlideItemsModelClassList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sliderLayout = inflater.inflate(R.layout.the_items_layout, null);
        ImageView featured_image = sliderLayout.findViewById(R.id.my_featured_image);
        LinearLayout main_slide = sliderLayout.findViewById(R.id.main_slide);

        Glide.with(context).load(theSlideItemsModelClassList.get(position).getImage_url()).centerCrop().into(featured_image);

        main_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SliderData sliderData = theSlideItemsModelClassList.get(position);
                System.out.println(sliderData);

                try {

                    if (listener != null) {
                        listener.onItemClick(sliderData.getAction(), sliderData.getMeta());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        container.addView(sliderLayout);
        return sliderLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return theSlideItemsModelClassList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    public interface OnItemClickListener {
        void onItemClick(String action, String meta);
    }
}
