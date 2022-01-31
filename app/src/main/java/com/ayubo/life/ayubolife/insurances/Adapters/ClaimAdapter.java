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
import com.ayubo.life.ayubolife.insurances.GenericItems.ClaimItem;

import java.util.ArrayList;

public class ClaimAdapter extends RecyclerView.Adapter<ClaimAdapter.ExampleViewHolder> {
    private Context cContext;
    private ArrayList<ClaimItem> cClaimList;
    private ClaimAdapter.OnItemClickListener cListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ClaimAdapter.OnItemClickListener listener) {
        cListener = listener;
    }

    public ClaimAdapter(Context context, ArrayList<ClaimItem> exampleList) {
        cContext = context;
        cClaimList = exampleList;

    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(cContext).inflate(R.layout.claim_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ClaimItem currentItem = cClaimList.get(position);
        String type = currentItem.getType();
        String date = currentItem.getDate();
        String status = currentItem.getStatus();

        holder.cTextViewType.setText(type);
        holder.cTextViewDate.setText(date);
        holder.cTextViewStatus.setText(status);

        if (currentItem.getStatus().equals("Active") || currentItem.getStatus().equals("Complete")) {
            holder.cTextViewStatus.setTextColor(ContextCompat.getColor(cContext, R.color.green));
        } else {
            holder.cTextViewStatus.setTextColor(ContextCompat.getColor(cContext, R.color.orange));
        }
    }

    @Override
    public int getItemCount() {
        return cClaimList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView cTextViewType;
        public TextView cTextViewDate;
        public TextView cTextViewStatus;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            cTextViewType = itemView.findViewById(R.id.policy_claim_type);
            cTextViewDate = itemView.findViewById(R.id.policy_claim_date);
            cTextViewStatus = itemView.findViewById(R.id.policy_claim_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            cListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
