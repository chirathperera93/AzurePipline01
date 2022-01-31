package com.ayubo.life.ayubolife.insurances.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.insurances.GenericItems.PolicyDetailCoversClaimItem;

import java.util.ArrayList;

public class PolicyDetailCoversClaimAdapter extends RecyclerView.Adapter<PolicyDetailCoversClaimAdapter.ExampleViewHolder> {

    private Context dContext;
    private ArrayList<PolicyDetailCoversClaimItem> dClaimCoverList;
    private PolicyDetailCoversClaimAdapter.OnItemClickListener dListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(PolicyDetailCoversClaimAdapter.OnItemClickListener listener) {
        dListener = listener;
    }

    public PolicyDetailCoversClaimAdapter(Context context, ArrayList<PolicyDetailCoversClaimItem> exampleList) {
        dContext = context;
        dClaimCoverList = exampleList;

    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(dContext).inflate(R.layout.policy_detail_covers_claim_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        PolicyDetailCoversClaimItem currentItem = dClaimCoverList.get(position);
        String name = currentItem.getName();
        String value = currentItem.getValue();
        holder.dTextViewName.setText(name);
        holder.dTextViewValue.setText(value);
    }

    @Override
    public int getItemCount() {
        return dClaimCoverList.size();
    }


    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView dTextViewName;
        public TextView dTextViewValue;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            dTextViewName = itemView.findViewById(R.id.policy_detail_covers_claims_name);
            dTextViewValue = itemView.findViewById(R.id.policy_detail_covers_claims_value);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            dListener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
}
