package com.ayubo.life.ayubolife.channeling.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;

import java.util.List;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.activity.DetailActivity;
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;

/**
 * Created by Sabri on 3/16/2018. Adapter for Search
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    public static final int TITLE_TYPE = 1;
    public static final int DETAIL_TYPE = 2;
    public static final int SUMMARY_TYPE = 3;
    public static final int SINGLE_TYPE = 4;

    //instances
    private Activity activity;
    private List<Object> values;
    private OnItemClickListener listener;
    private SearchActivity.SearchActions searchActions;

    public SearchAdapter(Activity activity, List<Object> values, SearchActivity.SearchActions searchActions) {
        this.activity = activity;
        this.values = values;
        this.searchActions = searchActions;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == DETAIL_TYPE) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_search_row, parent, false);
            return new DetailViewHolder(view);
        } else if (viewType == SUMMARY_TYPE) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_summary_row, parent, false);
            return new DetailViewHolder(view);
        } else if (viewType == SINGLE_TYPE) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_simple_search_row, parent, false);
            return new DetailViewHolder(view);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.component_search_title_row, parent, false);
            return new TitleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        System.out.println("========0=============="+position);
        String name = "";
        String value = "";
        String imageUrl = "";

        if (searchActions != null) {
            name = searchActions.getName(values.get(position));
            value = searchActions.getValue(values.get(position));
            imageUrl = searchActions.getImageUrl(values.get(position));
        }

        if (holder instanceof DetailViewHolder) {
            DetailViewHolder detailViewHolder = (DetailViewHolder) holder;

            detailViewHolder.txtTitle.setText(Utility.upperCaseWords(name));

            if(detailViewHolder.txtSubTitle != null) {
                detailViewHolder.txtSubTitle.setText(value);
                if (isNumeric(imageUrl))
                    detailViewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(activity, Integer.parseInt(imageUrl)));
                else
                    Glide.with(activity).load(imageUrl).into(detailViewHolder.imageView);
            }
        } else {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.txtTitle.setText(name);
        }

        final int pos = holder.getAdapterPosition();
        System.out.println("========1=============="+pos);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && holder instanceof DetailViewHolder)
                    listener.onItemClick(values.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (values == null)
            return 0;
        else
            return values.size();
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println("===================5==============="+position);
            return searchActions.getViewType();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtSubTitle;
        ImageView imageView;

        DetailViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_name_search_row);
            txtSubTitle = itemView.findViewById(R.id.txt_sub_name_search_row);
            imageView = itemView.findViewById(R.id.img_search_row);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title_search_row);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Object object);
    }

    private static boolean isNumeric(String str) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
