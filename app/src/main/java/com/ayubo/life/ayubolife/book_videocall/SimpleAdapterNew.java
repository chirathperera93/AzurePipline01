package com.ayubo.life.ayubolife.book_videocall;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SimpleAdapterNew extends RecyclerView.Adapter<SimpleAdapterNew.SimpleViewHolder> {
    private static final int COUNT = 100;

    private final Context mContext;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;

        public SimpleViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    public SimpleAdapterNew(Context context,List<Integer> mItem) {
        mContext = context;
      //  mItems = new ArrayList<Integer>(COUNT);
        mItems =mItem;
//        for (int i = 0; i < COUNT; i++) {
//            addItem(i);
//        }
    }
    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.raw_challenge_leaderboard, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
       // holder.title.setText("tetsdfsdfsdf");
    }



    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}