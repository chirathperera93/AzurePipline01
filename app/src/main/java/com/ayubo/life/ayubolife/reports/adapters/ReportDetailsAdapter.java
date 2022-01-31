package com.ayubo.life.ayubolife.reports.adapters;

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
import com.ayubo.life.ayubolife.reports.Reports_MainActivity;
import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;
import com.ayubo.life.ayubolife.reports.utility.Utility_Report;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class ReportDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_TITLE = R.layout.component_history_title_row;
    private static final int VIEW_TYPE_ITEM = R.layout.raw_reports_details_cell;
    private static final int VIEW_TYPE_HEADER = 0;

    private boolean isDownloadActive = false;

    private boolean shouldShow_Title = false;
    private boolean shouldShow_Name = true;
    private boolean shouldShow_ReportName = true;
    private boolean shouldShow_Date = true;

    private boolean isHospital_ID_Null = false;

    private boolean shouldShow_Hospital_Logo = true;
    private boolean shouldShow_Is_New = false;

    private boolean shouldShow_Is_Not_Assign = false;

    private boolean shouldShow_User_Image = false;
    private boolean shouldShow_User_Name = false;


    private Context context;


    private int cellType;
    private String selectedUuserId;
    private List<Object> reportsList;
    private List<Object> contactListFiltered;

    private String firstReportID;
    private String errorMsg;
    private List<AllRecordsMainResponse.AllRecordsMember> memberList;


    private OnClickAssignUserImage listener;
    private OnClickNotAssignIcon listener_notAssign;
    private OnClickONFamilyMemberIcon listener_familymember;
    private OnClickDownloadReportListner listener_downloadReport;
    private OnClickViewReportListner listener_viewReport;
    private OnClickShareReportListner listener_shareReport;
    private OnClickDeleteReportListner listener_deleteReport;


    public ReportDetailsAdapter(Context context, List<Object> reportsListt, List<AllRecordsMainResponse.AllRecordsMember> memberLis, String firstID, String selecteUuserId) {
        this.context = context;
        reportsList = new ArrayList<>();
        reportsList = reportsListt;
        reportsList.add("");
        memberList = memberLis;
        selectedUuserId = selecteUuserId;
        firstReportID = firstID;

        this.contactListFiltered = reportsListt;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        switch (viewType) {
            case VIEW_TYPE_ITEM:
                View viewItem = inflater.inflate(R.layout.raw_reports_details_cell, parent, false);
                viewHolder = new ReportCellViewHolder(viewItem);
                break;
            case VIEW_TYPE_TITLE:
                View viewLoading = inflater.inflate(R.layout.component_history_title_row, parent, false);
                viewHolder = new TitleViewHolder(viewLoading);
                break;
            case VIEW_TYPE_HEADER:
                View viewHeading = inflater.inflate(R.layout.raw_reports_details_heading_cell, parent, false);
                viewHolder = new HeaderViewHolder(viewHeading);
                break;

        }
        return viewHolder;
    }


    public void remove(int position) {
        reportsList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fromholde, final int pposition) {
        LayoutInflater inflater = null;
        if (fromholde instanceof ReportCellViewHolder) {

            final AllRecordsMainResponse.AllRecordsReport reportObj = (AllRecordsMainResponse.AllRecordsReport) contactListFiltered.get(pposition); // PostCell
            final ReportCellViewHolder holder = (ReportCellViewHolder) fromholde;

            //======   SETUP CELL STATUS ============================================
            setupCellStatus(reportObj);
            //======   SETUP CELL VIEW =======================Glide=====================
            setupCellViewForPost(holder, reportObj);

            holder.layout_main_view.setTag(reportObj);
            holder.img_btn_download.setTag(reportObj);
            holder.layout_btn_download.setTag(reportObj);
            holder.txt_btn_download.setTag(reportObj);

            holder.layout_btn_chart.setTag(reportObj);
            holder.img_btn_chart.setTag(reportObj);
            holder.txt_btn_chart.setTag(reportObj);

            holder.layout_btn_share.setTag(reportObj);
            holder.img_btn_share.setTag(reportObj);
            holder.txt_btn_share.setTag(reportObj);

            holder.layout_btn_delete.setTag(reportObj);
            holder.img_btn_delete.setTag(reportObj);
            holder.txt_btn_delete.setTag(reportObj);

            // holder.txt_user_name.setText(reportObj.getFullName());
            holder.txt_user_name.setText(Utility_Report.makeFirstLetterCapital(reportObj.getFullName()));
            holder.txt_rreport_name.setText(Utility_Report.makeFirstLetterCapital(reportObj.getReportName()));
            holder.txt_report_date.setText(reportObj.getReportDate());


            final int pos = holder.getAdapterPosition();

            holder.img_not_assign_ic.setTag(reportObj);
            holder.img_user_pic_ic.setTag(reportObj);

            holder.img_user_pic_ic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onSelectAssignUser((AllRecordsMainResponse.AllRecordsReport) v.getTag());
                }
            });
            holder.img_not_assign_ic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_notAssign != null)
                        listener_notAssign.onSelectNotAssignIcon((AllRecordsMainResponse.AllRecordsReport) v.getTag());
                }
            });


            //=======DOWNLOAD EVENT FINISHING=====================================================

            holder.layout_btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_downloadReport != null)
                        listener_downloadReport.onSelectDownload((AllRecordsMainResponse.AllRecordsReport) v.getTag(), pos);
                }
            });
            holder.img_btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_downloadReport != null)
                        listener_downloadReport.onSelectDownload((AllRecordsMainResponse.AllRecordsReport) v.getTag(), pos);
                }
            });
            holder.txt_btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_downloadReport != null)
                        listener_downloadReport.onSelectDownload((AllRecordsMainResponse.AllRecordsReport) v.getTag(), pos);
                }
            });
            //=======DOWNLOAD EVENT FINISHING=====================================================


            //=======CHART EVENT FINISHING=====================================================
            holder.layout_btn_chart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_viewReport != null)
                        listener_viewReport.onSelectView((AllRecordsMainResponse.AllRecordsReport) v.getTag(), pos);
                }
            });
            holder.img_btn_chart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_viewReport != null)
                        listener_viewReport.onSelectView((AllRecordsMainResponse.AllRecordsReport) v.getTag(), pos);
                }
            });
            holder.txt_btn_chart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_viewReport != null)
                        listener_viewReport.onSelectView((AllRecordsMainResponse.AllRecordsReport) v.getTag(), pos);
                }
            });
            //=======CHART EVENT FINISHING=====================================================


            //=======SHARE EVENT FINISHING=====================================================
            holder.layout_btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_shareReport != null)
                        listener_shareReport.onSelectShare((AllRecordsMainResponse.AllRecordsReport) v.getTag());
                }
            });
            holder.img_btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_shareReport != null)
                        listener_shareReport.onSelectShare((AllRecordsMainResponse.AllRecordsReport) v.getTag());
                }
            });
            holder.txt_btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_shareReport != null)
                        listener_shareReport.onSelectShare((AllRecordsMainResponse.AllRecordsReport) v.getTag());
                }
            });
            //=======SHARE EVENT FINISHING=====================================================


            //=======DELETE EVENT STARTING=====================================================
            holder.layout_btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_deleteReport != null)
                        listener_deleteReport.onSelectDelete((AllRecordsMainResponse.AllRecordsReport) v.getTag(), pos);
                }
            });
            holder.img_btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_deleteReport != null)
                        listener_deleteReport.onSelectDelete((AllRecordsMainResponse.AllRecordsReport) v.getTag(), pos);
                }
            });
            holder.txt_btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener_deleteReport != null)
                        listener_deleteReport.onSelectDelete((AllRecordsMainResponse.AllRecordsReport) v.getTag(), pos);
                }
            });
            //=======DELETE EVENT FINISHING=====================================================


            holder.layout_main_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AllRecordsMainResponse.AllRecordsReport reportObj = (AllRecordsMainResponse.AllRecordsReport) v.getTag();

                    if (reportObj.getAssign_user_id() != null) {
                        if (holder.layout_bottom_view.isShown()) {
                            holder.layout_bottom_view.setVisibility(View.GONE);
                        } else {
                            holder.layout_bottom_view.setVisibility(View.VISIBLE);
                        }

                    }

                }
            });


        } else if (fromholde instanceof TitleViewHolder) {

            TitleViewHolder titleViewHolder = (TitleViewHolder) fromholde;

            titleViewHolder.txtTitle.setText((String) contactListFiltered.get(pposition));
        } else if (fromholde instanceof HeaderViewHolder) {

            HeaderViewHolder titleViewHolder = (HeaderViewHolder) fromholde;

            titleViewHolder.dateView_programs.removeAllViews();

            for (int num = 0; num < memberList.size(); num++) {
                AllRecordsMainResponse.AllRecordsMember obj = memberList.get(num);
                inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View myView = inflater.inflate(R.layout.component_familymember_image_row, null);
                final TextView txt_goalname = (TextView) myView.findViewById(R.id.txt_name_doctor_row);
                final CircleImageView img_family_member_image = myView.findViewById(R.id.img_profile_doctor_row);
                final ImageView img_profile_shadow = myView.findViewById(R.id.img_profile_doctor_row2);

                img_family_member_image.setTag(obj);

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

                txt_goalname.setText(obj.getName());
                String imgUrl = obj.getProfilePictureUrl();

                if (imgUrl.contains("zoom_level")) {
                    imgUrl = imgUrl.replace("zoom_level", "xxxhdpi");
                }

                if (obj.getId().equals("Add")) {
                    img_family_member_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.add_new_doc));
                } else {
                    Glide.with(context).load(imgUrl).into(img_family_member_image);
                }


                titleViewHolder.dateView_programs.addView(myView);
            }
        }

    }

    @Override
    public int getItemCount() {

        int finalCount;

        finalCount = contactListFiltered == null ? 0 : contactListFiltered.size();

        return finalCount;

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = reportsList;
                } else {

                    List<Object> filteredList = new ArrayList<>();
                    for (Object row : reportsList) {

                        if (row instanceof AllRecordsMainResponse.AllRecordsReport) {
                            AllRecordsMainResponse.AllRecordsReport obb = (AllRecordsMainResponse.AllRecordsReport) row;

                            if (obb.getFullName().toLowerCase().contains(charString.toLowerCase()) || obb.getReportName().toLowerCase().contains(charString.toLowerCase()) || obb.getReportDate().toLowerCase().contains(charString.toLowerCase())) {
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
                notifyDataSetChanged();
            }
        };
    }


    private void setupCellStatus(AllRecordsMainResponse.AllRecordsReport reportObj) {
        cellType = reportObj.getRead();

        if (reportObj != null) {

            if (cellType == 1) {
                shouldShow_Title = true;
                shouldShow_Name = true;
                shouldShow_ReportName = true;
                shouldShow_Date = true;

                shouldShow_Hospital_Logo = true;
                shouldShow_Is_New = false;

            } else if (cellType == 0) {
                //New Cell .................
                shouldShow_Title = true;
                shouldShow_Name = true;
                shouldShow_ReportName = true;
                shouldShow_Date = true;

                shouldShow_Hospital_Logo = true;
                shouldShow_Is_New = true;
            }

            if (reportObj.getAssign_user_id() == null) {
                shouldShow_Is_Not_Assign = true;
            } else {
                shouldShow_Is_Not_Assign = false;
            }

            if (reportObj.getHosUid() == null) {
                isHospital_ID_Null = true;
            } else {
                isHospital_ID_Null = false;
            }
            // hos_uid

            // Is need to show download Active ...

            if (reportObj.getDownload_url() == null) {
                isDownloadActive = false;
                System.out.println("isDownloadActive=====0========================" + reportObj.getDownload_url());
            } else if ((reportObj.getDownload_url() != null) && (reportObj.getDownload_url().length() > 5)) {

                if (reportObj.getDownload_url().contains("pdf")) {
                    isDownloadActive = true;
                    System.out.println("isDownloadActive=====1========================" + reportObj.getDownload_url());
                } else {
                    isDownloadActive = false;
                    System.out.println("isDownloadActive=====0========================" + reportObj.getDownload_url());
                }


            }


        }
    }

    private void setupCellViewForPost(ReportCellViewHolder holder, AllRecordsMainResponse.AllRecordsReport reportObj) {

        shouldShow_User_Image = false;
        shouldShow_User_Name = false;

        // Default Content visibility ..........................
        holder.txt_user_name.setVisibility(View.VISIBLE);
        holder.txt_rreport_name.setVisibility(View.VISIBLE);
        holder.txt_report_date.setVisibility(View.VISIBLE);
        holder.img_hospital_logo_ic.setVisibility(View.VISIBLE);

        if (cellType == 1) {
            shouldShow_Title = true;
        }


        // New Logo and Not assign Icon visibility ..........................
        if (shouldShow_Is_New) {
            holder.img_btn_assgin_new.setVisibility(View.VISIBLE);

        } else {
            holder.img_btn_assgin_new.setVisibility(View.GONE);

            holder.assign_user_name.setVisibility(View.VISIBLE);
            holder.img_user_pic_ic.setVisibility(View.VISIBLE);
            holder.img_user_pic_ic2.setVisibility(View.VISIBLE);
        }


        if (shouldShow_Is_Not_Assign) {
            holder.img_not_assign_ic.setVisibility(View.VISIBLE);
            holder.assign_user_name.setVisibility(View.GONE);
            holder.img_user_pic_ic.setVisibility(View.GONE);
            holder.img_user_pic_ic2.setVisibility(View.GONE);

        } else {
            holder.img_not_assign_ic.setVisibility(View.GONE);
        }


        //-------IS this a Vital Report----------------------------------------
        if (isHospital_ID_Null) {
            String assignUserId = reportObj.getAssign_user_id();
            int ssss = reportObj.getRead();

            for (int ii = 0; ii < memberList.size(); ii++) {
                AllRecordsMainResponse.AllRecordsMember mem = (AllRecordsMainResponse.AllRecordsMember) memberList.get(ii);

                if (mem.getId().equals(assignUserId)) {
                    holder.assign_user_name.setText(mem.getName());

                    String imageURL = mem.getProfilePictureUrl();
                    RequestOptions requestOptions1 = new RequestOptions().transform(new CircleTransform(context));
                    Glide.with(context).load(imageURL)
                            .apply(requestOptions1)
                            .into(holder.img_user_pic_ic);
                }
            }

        } else {
            String assignUserId = reportObj.getAssign_user_id();
            int ssss = reportObj.getRead();

            for (int ii = 0; ii < memberList.size(); ii++) {
                AllRecordsMainResponse.AllRecordsMember mem = (AllRecordsMainResponse.AllRecordsMember) memberList.get(ii);

                if (mem.getId().equals(assignUserId)) {
                    holder.assign_user_name.setText(mem.getName());

                    String imageURL = mem.getProfilePictureUrl();
                    RequestOptions requestOptions1 = new RequestOptions()
                            .transform(new CircleTransform(context));
                    Glide.with(context).load(imageURL)
                            .apply(requestOptions1)
                            .into(holder.img_user_pic_ic);
                }
            }
        }


        if (reportObj.getFullName() != null) {
            holder.txt_user_name.setVisibility(View.VISIBLE);
            holder.txt_user_name.setText(reportObj.getFullName());
        } else {
            holder.txt_user_name.setVisibility(View.GONE);

        }

        holder.img_btn_review.setAlpha((float) 0.2);
        holder.txt_btn_review.setTextColor(Color.parseColor("#999999"));


        //-------Ended  IS this a Vital Report----------------------------------------

        if (isDownloadActive) {
            holder.img_btn_download.setAlpha((float) 1.0);
            holder.txt_btn_download.setTextColor(Color.parseColor("#747473"));

            holder.img_btn_share.setAlpha((float) 1.0);
            holder.txt_btn_share.setTextColor(Color.parseColor("#747473"));

        } else {
            holder.img_btn_download.setAlpha((float) 0.2);
            holder.txt_btn_download.setTextColor(Color.parseColor("#999999"));

            holder.img_btn_share.setAlpha((float) 0.2);
            holder.txt_btn_share.setTextColor(Color.parseColor("#999999"));
        }

        // configure view holder
        holder.layout_bottom_view.setVisibility(View.GONE);

        // Only show New Title when New once only.......... visibility ..........................
//        if((cellType==0) &&(firstReportID.equals(reportObj.getId()))){
//            shouldShow_Title=true;
//        }
        if (cellType == 0) {
            shouldShow_Title = false;
        }

        if (reportObj.getHospital() != null) {
            holder.img_hospital_logo_ic.setVisibility(View.VISIBLE);
            String hosLogo = reportObj.getHospital();
            //  https://livehappy.ayubo.life/custom/include/images/ech_hospitals/hemas.jpg
            Glide.with(context).load(hosLogo)
                    .into(holder.img_hospital_logo_ic);
        } else {
            holder.img_hospital_logo_ic.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_HEADER;

        if (contactListFiltered.get(position) instanceof String)
            return VIEW_TYPE_TITLE;
        else if (contactListFiltered.get(position) instanceof AllRecordsMainResponse.AllRecordsReport) {
            return VIEW_TYPE_ITEM;
        }
        return super.getItemViewType(position);
    }

    public void add(AllRecordsMainResponse.AllRecordsReport r) {
        reportsList.add(r);
        notifyItemInserted(reportsList.size() - 1);
    }

    public void addAll(List<AllRecordsMainResponse.AllRecordsReport> moveResults) {
        for (AllRecordsMainResponse.AllRecordsReport result : moveResults) {
            add(result);
        }
    }

    public void remove(AllRecordsMainResponse.AllRecordsReport r) {
        int position = reportsList.indexOf(r);
        if (position > -1) {
            reportsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public Object getItem(int position) {
        return contactListFiltered.get(position);
    }


    class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_history_title_row);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {


        LinearLayout dateView_programs;

        HeaderViewHolder(View itemView) {
            super(itemView);
            dateView_programs = (LinearLayout) itemView.findViewById(R.id.dateView_programs);

        }
    }

    class ReportCellViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_bottom_view, layout_main_view, uaser_assign_layout;
        TextView txt_user_name, txt_rreport_name, txt_report_date, assign_user_name, txt_btn_review;
        ImageView img_hospital_logo_ic, img_not_assign_ic, img_btn_assgin_new, img_user_pic_ic, img_user_pic_ic2;
        ImageView img_btn_download, img_btn_chart, img_btn_share, img_btn_delete, img_btn_review;
        LinearLayout layout_btn_download, layout_btn_chart, layout_btn_share, layout_btn_delete;
        TextView txt_btn_download, txt_btn_chart, txt_btn_share, txt_btn_delete;

        public ReportCellViewHolder(View rowView) {
            super(rowView);

            // configure view holder
            txt_user_name = (TextView) rowView.findViewById(R.id.txt_user_name);
            txt_rreport_name = (TextView) rowView.findViewById(R.id.txt_rreport_name);
            txt_report_date = (TextView) rowView.findViewById(R.id.txt_report_date);
            assign_user_name = (TextView) rowView.findViewById(R.id.assign_user_name);

            img_hospital_logo_ic = rowView.findViewById(R.id.img_hospital_logo_ic);
            img_not_assign_ic = rowView.findViewById(R.id.img_not_assign_ic);
            img_btn_assgin_new = rowView.findViewById(R.id.img_btn_assgin_new);
            img_user_pic_ic = rowView.findViewById(R.id.img_user_pic_ic);

            img_user_pic_ic2 = rowView.findViewById(R.id.img_user_pic_ic2);
            //lblListHeader =  rowView.findViewById(R.id.lblListHeader);

            img_btn_download = rowView.findViewById(R.id.img_btn_download);
            layout_btn_download = rowView.findViewById(R.id.layout_btn_download);
            txt_btn_download = rowView.findViewById(R.id.txt_btn_download);

            layout_btn_chart = rowView.findViewById(R.id.layout_btn_chart);
            img_btn_chart = rowView.findViewById(R.id.img_btn_chart);
            txt_btn_chart = rowView.findViewById(R.id.txt_btn_chart);

            layout_btn_share = rowView.findViewById(R.id.layout_btn_share);
            img_btn_share = rowView.findViewById(R.id.img_btn_share);
            txt_btn_share = rowView.findViewById(R.id.txt_btn_share);

            img_btn_review = rowView.findViewById(R.id.img_btn_review);
            txt_btn_review = rowView.findViewById(R.id.txt_btn_review);

            layout_btn_delete = rowView.findViewById(R.id.layout_btn_delete);
            img_btn_delete = rowView.findViewById(R.id.img_btn_delete);
            txt_btn_delete = rowView.findViewById(R.id.txt_btn_delete);

            layout_bottom_view = rowView.findViewById(R.id.layout_bottom_view);
            layout_main_view = rowView.findViewById(R.id.layout_main_view);
        }

    }


    public void setOnSelectAssignUserListener(OnClickAssignUserImage listener) {
        this.listener = listener;
    }

    public void setOnClickNotAssignIconListener(OnClickNotAssignIcon listener) {
        this.listener_notAssign = listener;
    }

    public void setOnClickFamilyMemberListener(OnClickONFamilyMemberIcon listener) {
        this.listener_familymember = listener;
    }

    public void setOnClickDownloadReportListener(OnClickDownloadReportListner listener) {
        this.listener_downloadReport = listener;
    }

    public void setOnClickViewReportListener(OnClickViewReportListner listener) {
        this.listener_viewReport = listener;
    }

    public void setOnClickShareReportListener(OnClickShareReportListner listener) {
        this.listener_shareReport = listener;
    }

    public void setOnClickDeleteReportListener(OnClickDeleteReportListner listener) {
        this.listener_deleteReport = listener;
    }

//    Interface For Events Handing ...........

    public interface OnClickAssignUserImage {
        void onSelectAssignUser(AllRecordsMainResponse.AllRecordsReport history);
    }

    public interface OnClickNotAssignIcon {
        void onSelectNotAssignIcon(AllRecordsMainResponse.AllRecordsReport history);
    }

    public interface OnClickONFamilyMemberIcon {
        void onSelectFamilyMember(AllRecordsMainResponse.AllRecordsMember history);
    }

    public interface OnClickDownloadReportListner {
        void onSelectDownload(AllRecordsMainResponse.AllRecordsReport history, int position);
    }

    public interface OnClickViewReportListner {
        void onSelectView(AllRecordsMainResponse.AllRecordsReport history, int position);
    }

    public interface OnClickShareReportListner {
        void onSelectShare(AllRecordsMainResponse.AllRecordsReport history);
    }

    public interface OnClickDeleteReportListner {
        void onSelectDelete(AllRecordsMainResponse.AllRecordsReport history, int position);
    }


}
