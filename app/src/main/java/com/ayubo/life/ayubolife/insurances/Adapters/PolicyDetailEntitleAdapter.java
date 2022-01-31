package com.ayubo.life.ayubolife.insurances.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.insurances.GenericItems.PolicyDetailEntitleItem;

import java.util.ArrayList;

public class PolicyDetailEntitleAdapter extends RecyclerView.Adapter<PolicyDetailEntitleAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<PolicyDetailEntitleItem> mPolicyDetailEntitleList;
    private PolicyDetailEntitleAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String action, String meta);
    }

    public void setOnItemClickListener(PolicyDetailEntitleAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public PolicyDetailEntitleAdapter(Context context, ArrayList<PolicyDetailEntitleItem> exampleList) {
        mContext = context;
        mPolicyDetailEntitleList = exampleList;

    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.policy_entitlement_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        PolicyDetailEntitleItem currentItem = mPolicyDetailEntitleList.get(position);
        String entitleName = currentItem.getTitle();
        holder.dTextViewEntitleName.setText(entitleName);

    }

    @Override
    public int getItemCount() {
        return mPolicyDetailEntitleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView dTextViewEntitleName;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            dTextViewEntitleName = itemView.findViewById(R.id.policy_detail_entitle_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(mPolicyDetailEntitleList.get(position).getAction(), mPolicyDetailEntitleList.get(position).getMeta());
                        }
                    }
                }
            });
        }
    }
}
