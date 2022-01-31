package com.ayubo.life.ayubolife.insurances.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.insurances.GenericItems.ClaimUploadItem;

import java.util.ArrayList;

public class ClaimUploadPointsAdapter extends RecyclerView.Adapter<ClaimUploadPointsAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<ClaimUploadItem> mClaimUploadItemList;
    private ClaimUploadPointsAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String action, String meta);
    }

    public void setOnItemClickListener(ClaimUploadPointsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public ClaimUploadPointsAdapter(Context context, ArrayList<ClaimUploadItem> exampleList) {
        mContext = context;
        mClaimUploadItemList = exampleList;

    }

    @NonNull
    @Override
    public ClaimUploadPointsAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.claim_upload_point, parent, false);
        return new ClaimUploadPointsAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimUploadPointsAdapter.ExampleViewHolder holder, int position) {
        ClaimUploadItem currentItem = mClaimUploadItemList.get(position);
        String entitleName = currentItem.getTitle();
        holder.dTextViewEntitleName.setText(entitleName);

    }

    @Override
    public int getItemCount() {
        return mClaimUploadItemList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView dTextViewEntitleName;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            dTextViewEntitleName = itemView.findViewById(R.id.claim_upload_point_item);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mListener.onItemClick(mPolicyDetailEntitleList.get(position).getAction(), mPolicyDetailEntitleList.get(position).getMeta());
//                        }
//                    }
//                }
//            });
        }
    }
}
