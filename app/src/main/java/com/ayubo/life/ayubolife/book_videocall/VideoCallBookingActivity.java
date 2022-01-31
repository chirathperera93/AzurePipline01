package com.ayubo.life.ayubolife.book_videocall;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.SimpleAdapter;

import com.ayubo.life.ayubolife.R;

import java.util.ArrayList;
import java.util.List;

public class VideoCallBookingActivity extends AppCompatActivity {
    SimpleAdapterNew mAdapter;
    RecyclerView mRecyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_booking);

        //Your RecyclerView
        mRecyclerView = findViewById(R.id.recycleview_video_today);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));



        final int COUNT = 100;

         final Context mContext;

         int mCurrentItemId = 0;

        List<Integer> mItems = new ArrayList<Integer>(COUNT);

        mItems.add(1);
        mItems.add(2);
        mItems.add(3);
        mItems.add(6);
        mItems.add(4);
        mItems.add(5);

        //Your RecyclerView.Adapter
        mAdapter = new SimpleAdapterNew(this,mItems);

        //This is the code to provide a sectioned grid
        List<GridAdapter.Section> sections =
                new ArrayList<GridAdapter.Section>();

        //Sections
        sections.add(new GridAdapter.Section(0,"Section 1"));
        sections.add(new GridAdapter.Section(2,"Section 2"));
        sections.add(new GridAdapter.Section(4,"Section 3"));

        //Add your adapter to the sectionAdapter
        GridAdapter.Section[] dummy = new GridAdapter.Section[sections.size()];
        GridAdapter mSectionedAdapter = new GridAdapter(this,R.layout.raw_payment_cell_new,R.id.tv_payment_type_heading,mRecyclerView,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        mRecyclerView.setAdapter(mSectionedAdapter);
    }
}
