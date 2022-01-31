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
import com.ayubo.life.ayubolife.insurances.GenericItems.ClaimHistoryItem;

import java.util.ArrayList;

public class ClaimHistoryAdapter extends RecyclerView.Adapter<ClaimHistoryAdapter.ExampleViewHolder> {
    private Context cContext;
    private ArrayList<ClaimHistoryItem> cClaimHistoryList;
    private ClaimHistoryAdapter.OnItemClickListener cListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ClaimHistoryAdapter.OnItemClickListener listener) {
        cListener = listener;
    }

    public ClaimHistoryAdapter(Context context, ArrayList<ClaimHistoryItem> exampleList) {
        cContext = context;
        cClaimHistoryList = exampleList;

    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(cContext).inflate(R.layout.policy_claim_history_item, parent, false);
        return new ClaimHistoryAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimHistoryAdapter.ExampleViewHolder holder, int position) {
        ClaimHistoryItem currentItem = cClaimHistoryList.get(position);
        String title = currentItem.getClaimTitle();
        String amount = currentItem.getClaimAmount();
        String date = currentItem.getClaimDate();
        String status = currentItem.getClaimStatus();

        holder.cTextViewClaimTitle.setText(title);
        holder.cTextViewClaimAmount.setText(amount);
        holder.cTextViewClaimDate.setText(date);
        holder.cTextViewClaimStatus.setText(status);

        if (currentItem.getClaimStatus().equals("Active") || currentItem.getClaimStatus().equals("Complete")) {
            holder.cTextViewClaimStatus.setTextColor(ContextCompat.getColor(cContext, R.color.green));
        } else {
            holder.cTextViewClaimStatus.setTextColor(ContextCompat.getColor(cContext, R.color.orange));
        }

    }

    @Override
    public int getItemCount() {
        return cClaimHistoryList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView cTextViewClaimTitle;
        public TextView cTextViewClaimAmount;
        public TextView cTextViewClaimDate;
        public TextView cTextViewClaimStatus;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            cTextViewClaimTitle = itemView.findViewById(R.id.policy_detail_claim_history_title);
            cTextViewClaimAmount = itemView.findViewById(R.id.policy_detail_claim_history_amount);
            cTextViewClaimDate = itemView.findViewById(R.id.policy_detail_claim_history_date);
            cTextViewClaimStatus = itemView.findViewById(R.id.policy_detail_claim_history_status);

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
