package com.ayubo.life.ayubolife.reports.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.reports.model.FamilyMemberData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class FamilyMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_FAMILY_MEMBER = 1;


    //instances
    // Declare Variables
    private List<FamilyMemberData> familyMemberlist = null;
    private Activity activity;
    private OnItemClickListener listener;
    private PrefManager pref;


    private OnClickAssignUserImage listener_delete;


    public void setOnSelectAssignUserListener(OnClickAssignUserImage listener) {
        this.listener_delete = listener;
    }
    //    Interface For Events Handing ...........


    public interface OnClickAssignUserImage {
        void onSelectAssignUser(FamilyMemberData obj, int pos);
    }

    public FamilyMemberAdapter(Activity activity, List<FamilyMemberData> worldpopulationlist) {
        this.familyMemberlist = worldpopulationlist;
        this.activity = activity;
        pref = new PrefManager(activity);
    }


    class FamilyMemberViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        ImageView user_icon, member_user_delete;
        LinearLayout member_user_delete_lay;

        FamilyMemberViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            user_icon = itemView.findViewById(R.id.member_user_list_image);
            member_user_delete = itemView.findViewById(R.id.member_user_delete);
            member_user_delete_lay = itemView.findViewById(R.id.member_user_delete_lay);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        System.out.println("========================onCreateViewHolder");
        view = LayoutInflater.from(activity).inflate(R.layout.search_familymember_raw_layout, parent, false);
        return new FamilyMemberViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FamilyMemberViewHolder) {
            FamilyMemberViewHolder imgViewHolder = (FamilyMemberViewHolder) holder;
            FamilyMemberData expert = familyMemberlist.get(position);

            String name = familyMemberlist.get(position).getName();
            String image = familyMemberlist.get(position).getProfilePicture();

            imgViewHolder.text_name.setText(name);
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).transforms(new CircleTransform(activity));
            Glide.with(activity).load(image)
                    .transition(withCrossFade())
                    .thumbnail(0.5f)
                    .apply(requestOptions)
                    .into(imgViewHolder.user_icon);

//            imgViewHolder.user_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (listener_delete != null)
//                        listener_delete.onSelectAssignUser(familyMemberlist.get(position),position);
//                }
//            });
            imgViewHolder.member_user_delete_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener_delete != null)
                        listener_delete.onSelectAssignUser(familyMemberlist.get(position), position);
                }
            });
            imgViewHolder.member_user_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener_delete != null)
                        listener_delete.onSelectAssignUser(familyMemberlist.get(position), position);
                }
            });

        }

        final int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(familyMemberlist.get(pos));
            }
        });

    }


    @Override
    public int getItemCount() {
        if (familyMemberlist == null)
            return 0;

        else
            return familyMemberlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (familyMemberlist == null)
            return VIEW_TYPE_FAMILY_MEMBER;
        else
            return VIEW_TYPE_FAMILY_MEMBER;
    }

    public interface OnItemClickListener {
        void onItemClick(Object object);
    }


    public interface OnDoctorClickListener {
        void onDoctorClick(Object object);

//        void onNewExpertFocused(Expert expert);
    }


}
