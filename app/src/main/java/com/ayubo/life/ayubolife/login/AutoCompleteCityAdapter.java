package com.ayubo.life.ayubolife.login;

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
import com.ayubo.life.ayubolife.login.model.CityDataInfo;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteCityAdapter extends ArrayAdapter<CityDataInfo> {

    private List<CityDataInfo> cityInfoArrayListFull;
    private AutoCompleteCityAdapter.OnItemClickListener mListener;
    private Context mContext;

    public AutoCompleteCityAdapter(@NonNull Context context, @NonNull ArrayList<CityDataInfo> cityInfoArrayList) {
        super(context, 0, cityInfoArrayList);
        mContext = context;
        cityInfoArrayListFull = cityInfoArrayList;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return cityFilter;
    }

    public void setOnItemClickListener(AutoCompleteCityAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onCityItemClick(CityDataInfo cityDataInfo);
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
        CityDataInfo cityDataInfo = getItem(position);
        if (cityDataInfo != null) {
            textViewName.setText(cityDataInfo.getCity());
            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onCityItemClick(cityDataInfo);
                    }
                }
            });
        }
        return convertView;
    }

    private Filter cityFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<CityDataInfo> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(cityInfoArrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CityDataInfo item : cityInfoArrayListFull) {
                    if (item.getCity().toLowerCase().contains(filterPattern)) {
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
            return ((CityDataInfo) resultValue).getCity();
        }
    };
}
