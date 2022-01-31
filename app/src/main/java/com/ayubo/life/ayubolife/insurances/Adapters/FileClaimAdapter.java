package com.ayubo.life.ayubolife.insurances.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.insurances.GenericItems.FileClaimItem;

import java.util.ArrayList;

public class FileClaimAdapter extends RecyclerView.Adapter<FileClaimAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<FileClaimItem> mFileClaimList;
    private FileClaimAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(FileClaimAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public FileClaimAdapter(Context context, ArrayList<FileClaimItem> exampleList) {
        mContext = context;
        mFileClaimList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.file_claim_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        FileClaimItem currentItem = mFileClaimList.get(position);
        String heading = currentItem.getHeading();
        holder.mTextViewHeader.setText(heading);
    }

    @Override
    public int getItemCount() {
        return mFileClaimList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewHeader;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewHeader = itemView.findViewById(R.id.file_claim_item_header);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
