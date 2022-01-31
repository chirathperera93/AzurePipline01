package com.ayubo.life.ayubolife.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CountryCodesListAdapter extends RecyclerView.Adapter<CountryCodesListAdapter.ExampleViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<CountryItem> mCountryItemList;
    private ArrayList<CountryItem> mCountryItemListFull;
    private CountryCodesListAdapter.OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return CountriesFilter;
    }

    private Filter CountriesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CountryItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {

                ArrayList<CountryItem> list = mCountryItemListFull;

                filteredList = list;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CountryItem countryItem : mCountryItemListFull) {
                    if (countryItem.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(countryItem);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mCountryItemList.clear();
            mCountryItemList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(CountryItem countryItem);
    }

    public void setOnItemClickListener(CountryCodesListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public CountryCodesListAdapter(View progressDialog, Context context, ArrayList<CountryItem> countryItemList) {
        mContext = context;
        mCountryItemList = countryItemList;
        mCountryItemListFull = new ArrayList<>(countryItemList);

    }

    @NonNull
    @Override
    public CountryCodesListAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.country_item, parent, false);
        return new CountryCodesListAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryCodesListAdapter.ExampleViewHolder holder, int position) {
        CountryItem currentItem = mCountryItemList.get(position);
        Glide.with(mContext).load(currentItem.getFlag()).into(holder.countryFlag);
        holder.countryName.setText(currentItem.getName() + " " + "(" + currentItem.getIsoCode() + ")");
        holder.country_code.setText(currentItem.getDialCode());


    }

    @Override
    public int getItemCount() {
        return mCountryItemList.size();
    }


    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView countryName;
        public ImageView countryFlag;
        public TextView country_code;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.country_name);
            countryFlag = itemView.findViewById(R.id.country_flag);
            country_code = itemView.findViewById(R.id.country_code);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(mCountryItemList.get(position));
                        }
                    }
                }
            });
        }
    }
}
