package com.ayubo.life.ayubolife.quick_links.experts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.home_popup_menu.ChatObj;
import com.ayubo.life.ayubolife.reports.adapters.FamilyMemberAdapter;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //constants
    private static final int VIEW_TYPE_TITLE = R.layout.component_cell_default_title_row;
    private static final int VIEW_TYPE_ITEM = R.layout.raw_chat_people_listview;

    //instances
    // Declare Variables
    private ArrayList<Object> familyMemberlist = null;
    private Activity activity;
    private FamilyMemberAdapter.OnItemClickListener listener;
    private PrefManager pref;
    private List<Object> contactListFiltered;

    private FamilyMemberAdapter.OnClickAssignUserImage listener_delete;


    public AskAdapter(Activity activity, ArrayList<Object> worldpopulationlist) {
        this.familyMemberlist = worldpopulationlist;
        this.activity = activity;
        pref = new PrefManager(activity);

        this.contactListFiltered = worldpopulationlist;
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_history_title_row_heading);
        }
    }

    class AskViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txt_specility;
        ImageView image;

        AskViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_doctorName9);
            txt_specility = itemView.findViewById(R.id.txt_specility);
            image = itemView.findViewById(R.id.list_image);
        }
    }

    public void setOnItemClickListener(FamilyMemberAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_ITEM:
                View viewItem = inflater.inflate(R.layout.raw_chat_people_listview, parent, false);
                viewHolder = new AskViewHolder(viewItem);
                break;
            case VIEW_TYPE_TITLE:
                View viewLoading = inflater.inflate(R.layout.component_cell_default_title_row, parent, false);
                viewHolder = new TitleViewHolder(viewLoading);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AskViewHolder) {
            AskViewHolder imgViewHolder = (AskViewHolder) holder;
            ChatObj expert = (ChatObj) contactListFiltered.get(position);

            imgViewHolder.txtName.setText(expert.getName());
            imgViewHolder.txt_specility.setText(expert.getSpeciality());

            String imageURL = ApiClient.BASE_URL + expert.getImage();

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(activity));
            Glide.with(activity).load(imageURL)
                    .transition(withCrossFade())
                    .thumbnail(0.5f)
                    .apply(requestOptions)
                    .into(imgViewHolder.image);


        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.txtTitle.setText((String) contactListFiltered.get(position));
        }


        final int pos = holder.getAdapterPosition();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(contactListFiltered.get(pos));
            }
        });

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = familyMemberlist;
                } else {
                    List<Object> filteredList = new ArrayList<>();
                    for (Object row : familyMemberlist) {

                        if (row instanceof ChatObj) {
                            ChatObj obb = (ChatObj) row;

                            if (obb.getName().toLowerCase().contains(charString.toLowerCase()) || obb.getSpeciality().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                ArrayList<Object> songsLis = (ArrayList<Object>) filterResults.values;

                contactListFiltered = (ArrayList<Object>) Quick_Utility.getSortedAskDataList(songsLis);

                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        if (contactListFiltered == null)
            return 0;

        else
            return contactListFiltered.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (contactListFiltered.get(position) instanceof String)
            return VIEW_TYPE_TITLE;
        else if (contactListFiltered.get(position) instanceof ChatObj) {
            return VIEW_TYPE_ITEM;
        }
        return super.getItemViewType(position);
    }


    public interface OnItemClickListener {
        void onItemClick(Object object);
    }

//    public Object getItem(int position) {
//        return contactListFiltered.get(position);
//    }

    public interface OnDoctorClickListener {
        void onDoctorClick(Object object);

//        void onNewExpertFocused(Expert expert);
    }


}
