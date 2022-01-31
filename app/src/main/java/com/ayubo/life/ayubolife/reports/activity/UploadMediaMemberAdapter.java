package com.ayubo.life.ayubolife.reports.activity;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.reports.adapters.ReportDetailsAdapter;
import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;
import com.ayubo.life.ayubolife.reports.utility.Utility_Report;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class UploadMediaMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;


    private Context context;


    private int cellType;
    private String selectedUuserId;
    private List<Object> contactListFiltered;
    private List<AllRecordsMainResponse.AllRecordsMember> memberList;
    private OnClickONFamilyMemberIcon listener_familymember;


    public UploadMediaMemberAdapter(
            Context context,
            List<AllRecordsMainResponse.AllRecordsMember> memberLis,
            String selecteUuserId) {
        this.context = context;
        memberList = memberLis;
        selectedUuserId = selecteUuserId;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewHeading = inflater.inflate(R.layout.raw_reports_details_heading_cell, parent, false);
        viewHolder = new UploadMediaMemberAdapter.HeaderViewHolder(viewHeading);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fromholde, final int pposition) {
        LayoutInflater inflater = null;

        if (fromholde instanceof UploadMediaMemberAdapter.HeaderViewHolder) {

            UploadMediaMemberAdapter.HeaderViewHolder titleViewHolder = (UploadMediaMemberAdapter.HeaderViewHolder) fromholde;

            titleViewHolder.dateView_programs.removeAllViews();

            if (memberList.size() > 0) {
                for (int num = 0; num < memberList.size(); num++) {

                    try {
                        AllRecordsMainResponse.AllRecordsMember obj = memberList.get(num);
                        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View myView = inflater.inflate(R.layout.component_familymember_image_row, null);
                        final TextView txt_goalname = (TextView) myView.findViewById(R.id.txt_name_doctor_row);
                        final CircleImageView img_family_member_image = myView.findViewById(R.id.img_profile_doctor_row);
                        final ImageView img_profile_shadow = myView.findViewById(R.id.img_profile_doctor_row2);


                        if (selectedUuserId.equals(obj.getId())) {
                            img_profile_shadow.setBackgroundResource(R.drawable.orange_circle_background_image);
                            txt_goalname.setTextColor(Color.parseColor("#ff860b"));
                            String imgUrl = obj.getProfilePictureUrl();

                            if (imgUrl.contains("zoom_level")) {
                                imgUrl = imgUrl.replace("zoom_level", "xxxhdpi");
                            }
                            Glide.with(context).load(imgUrl).into(img_family_member_image);
                        }

                        img_family_member_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AllRecordsMainResponse.AllRecordsMember obj = (AllRecordsMainResponse.AllRecordsMember) view.getTag();

                                //     if(selectedUuserId.equals(obj.getId())) {

                                img_profile_shadow.setBackgroundResource(R.drawable.orange_circle_background_image);
                                //  img_profile_shadow.setBackgroundResource(R.drawable.orange_circle_background);
                                txt_goalname.setTextColor(Color.parseColor("#ff860b"));
                                //   }


                                if (listener_familymember != null)
                                    listener_familymember.onSelectFamilyMember(obj);


                            }
                        });


//                if (imgUrl.contains("zoom_level")) {
//                    imgUrl = imgUrl.replace("zoom_level", "xxxhdpi");
//                } else


                        if (!obj.getId().equals("Add") && !obj.getId().equals("all")) {
                            img_family_member_image.setTag(obj);
                            txt_goalname.setText(obj.getName());
                            String imgUrl = obj.getProfilePictureUrl();
                            Glide.with(context).load(imgUrl).into(img_family_member_image);
                            titleViewHolder.dateView_programs.addView(myView);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }


        }

    }

    @Override
    public int getItemCount() {

        return 1;

    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {

        LinearLayout dateView_programs;

        HeaderViewHolder(View itemView) {
            super(itemView);
            dateView_programs = (LinearLayout) itemView.findViewById(R.id.dateView_programs);

        }
    }


    public void setOnClickFamilyMemberListener(UploadMediaMemberAdapter.OnClickONFamilyMemberIcon listener) {
        this.listener_familymember = listener;
    }


    public interface OnClickONFamilyMemberIcon {
        void onSelectFamilyMember(AllRecordsMainResponse.AllRecordsMember history);
    }


}

