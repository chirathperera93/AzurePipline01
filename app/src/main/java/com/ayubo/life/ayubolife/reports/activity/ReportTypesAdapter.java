package com.ayubo.life.ayubolife.reports.activity;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;

import java.util.ArrayList;
import java.util.List;

public class ReportTypesAdapter extends RecyclerView.Adapter<ReportTypesAdapter.ExampleViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<ReportTypeObject> mReportTypeItemList;
    private ArrayList<ReportTypeObject> mReportTypeItemListFull;
    private ReportTypesAdapter.OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return ReportTypeFilter;
    }

    private Filter ReportTypeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ReportTypeObject> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {

                ArrayList<ReportTypeObject> list = mReportTypeItemListFull;

                filteredList = list;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ReportTypeObject reportTypeObject : mReportTypeItemListFull) {
                    if (reportTypeObject.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(reportTypeObject);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mReportTypeItemList.clear();
            mReportTypeItemList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(ReportTypeObject reportTypeObject);
    }

    public void setOnItemClickListener(ReportTypesAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public ReportTypesAdapter(View progressDialog, Context context, ArrayList<ReportTypeObject> reportTypeObjectItemList) {
        mContext = context;
        mReportTypeItemList = reportTypeObjectItemList;
        mReportTypeItemListFull = new ArrayList<>(reportTypeObjectItemList);

    }

    @NonNull
    @Override
    public ReportTypesAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.report_spinner_row, parent, false);
        return new ReportTypesAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportTypesAdapter.ExampleViewHolder holder, int position) {
        ReportTypeObject reportTypeObject = mReportTypeItemList.get(position);
        holder.itemName.setText(reportTypeObject.name);


    }

    @Override
    public int getItemCount() {
        return mReportTypeItemList.size();
    }


    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.reportSpinnerRowTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(mReportTypeItemList.get(position));
                        }
                    }
                }
            });
        }
    }
}