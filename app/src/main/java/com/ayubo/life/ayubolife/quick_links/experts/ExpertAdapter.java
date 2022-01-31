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
import com.ayubo.life.ayubolife.home_popup_menu.DoctorObj;
import com.ayubo.life.ayubolife.reports.adapters.FamilyMemberAdapter;
import com.ayubo.life.ayubolife.reports.model.FamilyMemberData;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ExpertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //constants
    private static final int VIEW_TYPE_TITLE = R.layout.component_cell_default_title_row;
    private static final int VIEW_TYPE_ITEM = R.layout.list_row;


    //instances
    // Declare Variables
    private ArrayList<Object> familyMemberlist = null;
    private ArrayList<Object> contactListFiltered = null;
    private Activity activity;
    private FamilyMemberAdapter.OnItemClickListener listener;
    private PrefManager pref;


    private FamilyMemberAdapter.OnClickAssignUserImage listener_delete;


    public void setOnSelectAssignUserListener(FamilyMemberAdapter.OnClickAssignUserImage listener) {
        this.listener_delete = listener;
    }
    //    Interface For Events Handing ...........


    public interface OnClickAssignUserImage {
        void onSelectAssignUser(FamilyMemberData obj, int pos);
    }

    public ExpertAdapter(Activity activity, ArrayList<Object> worldpopulationlist) {
        this.familyMemberlist = worldpopulationlist;
        this.activity = activity;
        pref = new PrefManager(activity);

        this.contactListFiltered = familyMemberlist;
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_history_title_row_heading);
        }
    }

    class ExpertViewHolder extends RecyclerView.ViewHolder {


        TextView txtName, txt_category;
        ImageView user_icon;


        ExpertViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_doctorName9);
            txt_category = itemView.findViewById(R.id.txt_category9);
            user_icon = itemView.findViewById(R.id.list_image);

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
                View viewItem = inflater.inflate(R.layout.list_row, parent, false);
                viewHolder = new ExpertViewHolder(viewItem);
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
        if (holder instanceof ExpertViewHolder) {
            ExpertViewHolder imgViewHolder = (ExpertViewHolder) holder;
            DoctorObj expert = (DoctorObj) contactListFiltered.get(position);

            holder.itemView.setTag(expert);
            imgViewHolder.txtName.setText(expert.getName());
            imgViewHolder.txt_category.setText(expert.getSpec());

            String imageURL = ApiClient.BASE_URL + expert.getImage();
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(activity));
            Glide.with(activity).load(imageURL)
                    .transition(withCrossFade())
                    .thumbnail(0.5f)
                    .apply(requestOptions)
                    .into(imgViewHolder.user_icon);
        } else if (holder instanceof TitleViewHolder) {

            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

            titleViewHolder.txtTitle.setText((String) contactListFiltered.get(position));
        }
        final int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(view.getTag());
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
                    ArrayList<Object> filteredList = new ArrayList<>();
                    for (Object row : familyMemberlist) {

                        if (row instanceof DoctorObj) {
                            DoctorObj obb = (DoctorObj) row;

                            if (obb.getName().toLowerCase().contains(charString.toLowerCase()) || obb.getSpec().toLowerCase().contains(charString.toLowerCase())) {
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

                contactListFiltered = (ArrayList<Object>) Quick_Utility.getSortedDoctorDataList(songsLis);

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
        else if (contactListFiltered.get(position) instanceof DoctorObj) {
            return VIEW_TYPE_ITEM;
        }
        return super.getItemViewType(position);
    }

    //    @Override
//    public int getItemViewType(int position) {
//        if (contactListFiltered == null)
//            return VIEW_TYPE_FAMILY_MEMBER;
//        else
//            return VIEW_TYPE_FAMILY_MEMBER;
//    }
    public interface OnItemClickListener {
        void onItemClick(Object object);
    }


    public interface OnDoctorClickListener {
        void onDoctorClick(Object object);

//        void onNewExpertFocused(Expert expert);
    }


}
