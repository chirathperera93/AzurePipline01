package com.ayubo.life.ayubolife.insurances.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.insurances.GenericItems.BankAccountItem;

import java.util.ArrayList;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.ExampleViewHolder> {
    private Context bContext;
    private ArrayList<BankAccountItem> bBankAccountList;
    private BankAccountAdapter.OnItemClickListener bListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(BankAccountAdapter.OnItemClickListener listener) {
        bListener = listener;
    }

    public BankAccountAdapter(Context context, ArrayList<BankAccountItem> exampleList) {
        bContext = context;
        bBankAccountList = exampleList;

    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(bContext).inflate(R.layout.bank_account_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        BankAccountItem currentItem = bBankAccountList.get(position);
        String accountHolderName = currentItem.getAccHolderName();
        String bankName = currentItem.getBankName();
        String accountNumber = currentItem.getAccountNo();

        holder.bTextViewAccountHolderName.setText(accountHolderName);
        holder.bTextViewBankName.setText(bankName);
        holder.bTextViewBankAccountNo.setText(accountNumber);
    }

    @Override
    public int getItemCount() {
        return bBankAccountList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView bTextViewAccountHolderName;
        public TextView bTextViewBankName;
        public TextView bTextViewBankAccountNo;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            bTextViewAccountHolderName = itemView.findViewById(R.id.policy_bank_account_holder_name);
            bTextViewBankName = itemView.findViewById(R.id.policy_bank_name);
            bTextViewBankAccountNo = itemView.findViewById(R.id.policy_bank_account_no);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            bListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
