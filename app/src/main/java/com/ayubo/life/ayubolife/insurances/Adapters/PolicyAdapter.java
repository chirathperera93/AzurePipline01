package com.ayubo.life.ayubolife.insurances.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.insurances.GenericItems.PolicyItem;

import java.util.ArrayList;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<PolicyItem> mPolicyList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public PolicyAdapter(Context context, ArrayList<PolicyItem> exampleList) {
        mContext = context;
        mPolicyList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.policy_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        PolicyItem currentItem = mPolicyList.get(position);
        String heading = currentItem.getHeading();
        String subHeading = currentItem.getSubHeading();
        String status = currentItem.getStatus();

        holder.mTextViewId.setText(currentItem.getPolicyNo());
        holder.mTextViewHeader.setText(heading);
        holder.mTextViewSubHeader.setText(subHeading);
        holder.mTextViewStatus.setText(status);
        if (currentItem.getStatus().equals("Active") || currentItem.getStatus().equals("Complete")) {
            holder.mTextViewStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            holder.mTextViewStatus.setTextColor(ContextCompat.getColor(mContext, R.color.orange));
        }

    }

    @Override
    public int getItemCount() {
        return mPolicyList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewId;
        public TextView mTextViewHeader;
        public TextView mTextViewSubHeader;
        public TextView mTextViewStatus;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewId = itemView.findViewById(R.id.policy_item_number);
            mTextViewHeader = itemView.findViewById(R.id.policy_header);
            mTextViewSubHeader = itemView.findViewById(R.id.policy_sub_header);
            mTextViewStatus = itemView.findViewById(R.id.policy_status);

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
