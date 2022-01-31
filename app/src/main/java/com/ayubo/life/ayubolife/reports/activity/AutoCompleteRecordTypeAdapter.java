package com.ayubo.life.ayubolife.reports.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ayubo.life.ayubolife.R;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteRecordTypeAdapter extends ArrayAdapter<ReportTypeObject> {

    private List<ReportTypeObject> reportTypeObjectListFull;
    private AutoCompleteRecordTypeAdapter.OnItemClickListener mListener;
    private Context mContext;

    public AutoCompleteRecordTypeAdapter(@NonNull Context context, @NonNull List<ReportTypeObject> reportTypeObjectList) {
        super(context, 0, reportTypeObjectList);
        mContext = context;
        reportTypeObjectListFull = new ArrayList<>(reportTypeObjectList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    public void setOnItemClickListener(AutoCompleteRecordTypeAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ReportTypeObject reportTypeObject);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.record_type_autocomplete, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.reportAutoCompleteTextView);
        ReportTypeObject reportTypeObject = getItem(position);
        if (reportTypeObject != null) {
            textViewName.setText(reportTypeObject.name);
            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(reportTypeObject);
                    }
                }
            });
        }
        return convertView;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<ReportTypeObject> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(reportTypeObjectListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ReportTypeObject item : reportTypeObjectListFull) {
                    if (item.name.toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((ReportTypeObject) resultValue).name;
        }
    };
}
