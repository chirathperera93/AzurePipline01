package com.ayubo.life.ayubolife.reports.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;
import com.ayubo.life.ayubolife.reports.utility.Utility_Report;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class AddReportForReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_TITLE = R.layout.component_history_title_row;
    private static final int VIEW_TYPE_ITEM = R.layout.raw_reports_details_cell;
    private static final int VIEW_TYPE_HEADER =0;

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

    //    Interface For Events Handing ...........OnClickAssignUserImage
    public interface OnClickViewReportListner {
        void onSelectView(AllRecordsMainResponse.AllRecordsReport history);
        void onOpenPDF(AllRecordsMainResponse.AllRecordsReport history);
        void onSelectFamilyMember(AllRecordsMainResponse.AllRecordsMember history);
    }


    private OnClickViewReportListner listener_viewReport;




    public void setOnClickViewReportListener(OnClickViewReportListner listener) {
        this.listener_viewReport = listener;
    }




    public AddReportForReviewAdapter(Context context, List<Object> reportsListt,List<AllRecordsMainResponse.AllRecordsMember> memberLis,String firstID,String selecteUuserId) {
        this.context = context;
        reportsList = new ArrayList<>();
        reportsList=reportsListt;
        reportsList.add("");
        memberList=memberLis;
        selectedUuserId=selecteUuserId;
        firstReportID=firstID;

        this.contactListFiltered = reportsListt;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());



        switch (viewType) {
            case VIEW_TYPE_ITEM:
                View viewItem = inflater.inflate(R.layout.cell_report_review_add, parent, false);
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
        LayoutInflater inflater =null;
        if (fromholde instanceof ReportCellViewHolder) {

            final AllRecordsMainResponse.AllRecordsReport reportObj = (AllRecordsMainResponse.AllRecordsReport) contactListFiltered.get(pposition); // PostCell
            final ReportCellViewHolder holder = (ReportCellViewHolder) fromholde;


            //======   SETUP CELL STATUS ============================================
            setupCellStatus(reportObj);
            //======   SETUP CELL VIEW =======================Glide=====================
            setupCellViewForPost(holder, reportObj);

            if(reportObj.getHospital()==null){
                reportObj.setHospital("no");
            }

            if(reportObj.getHospital().equals("green")){
                holder.img_selection.setImageResource(R.drawable.selected_report_green_icon);
            }else{
                holder.img_selection.setImageResource(R.drawable.add_report_selectable);
            }
            holder.txt_report_name.setText(reportObj.getReportName());
            holder.txt_report_description.setText(reportObj.getReportDate());

            holder.btn_view.setTag(reportObj);
            holder.txt_report_name.setTag(reportObj);
            holder.txt_report_description.setTag(reportObj);
            holder.btn_view.setTag(reportObj);
            holder.img_selection.setTag(reportObj);
            holder.layout_main.setTag(reportObj);

            final int pos = holder.getAdapterPosition();




            holder.btn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllRecordsMainResponse.AllRecordsReport obj=null;
                    obj= (AllRecordsMainResponse.AllRecordsReport) v.getTag();


                    listener_viewReport.onOpenPDF((AllRecordsMainResponse.AllRecordsReport) v.getTag());

                }
            });
            holder.txt_report_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllRecordsMainResponse.AllRecordsReport obj=null;
                    if (listener_viewReport != null)
                        obj= (AllRecordsMainResponse.AllRecordsReport) v.getTag();

                    if(obj.getHospital()!=null) {
                        if (obj.getHospital().equals("green")) {
                            obj.setHospital("no");
                        } else {
                            obj.setHospital("green");
                        }
                    }
                    notifyDataSetChanged();
                    listener_viewReport.onSelectView((AllRecordsMainResponse.AllRecordsReport) v.getTag());

                }
            });

            holder.layout_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllRecordsMainResponse.AllRecordsReport obj=null;
                    if (listener_viewReport != null)
                        obj= (AllRecordsMainResponse.AllRecordsReport) v.getTag();

                    if(obj.getHospital()!=null) {
                        if (obj.getHospital().equals("green")) {
                            obj.setHospital("no");
                        } else {
                            obj.setHospital("green");
                        }
                    }
                    notifyDataSetChanged();
                    listener_viewReport.onSelectView((AllRecordsMainResponse.AllRecordsReport) v.getTag());

                }
            });


            holder.txt_report_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllRecordsMainResponse.AllRecordsReport obj=null;
                    if (listener_viewReport != null)
                        obj= (AllRecordsMainResponse.AllRecordsReport) v.getTag();
                    if(obj.getHospital()!=null) {
                    if(obj.getHospital().equals("green")){
                        obj.setHospital("no");
                    }else{
                        obj.setHospital("green");
                    }}
                    notifyDataSetChanged();
                    listener_viewReport.onSelectView((AllRecordsMainResponse.AllRecordsReport) v.getTag());

                }
            });


            holder.img_selection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllRecordsMainResponse.AllRecordsReport obj=null;
                    if (listener_viewReport != null)
                        obj= (AllRecordsMainResponse.AllRecordsReport) v.getTag();
                    if(obj.getHospital()!=null) {
                    if(obj.getHospital().equals("green")){
                        obj.setHospital("no");
                    }else{
                        obj.setHospital("green");
                    }}
                    notifyDataSetChanged();
                    listener_viewReport.onSelectView((AllRecordsMainResponse.AllRecordsReport) v.getTag());

                }
            });

        }
        else if (fromholde instanceof TitleViewHolder) {

            TitleViewHolder titleViewHolder = (TitleViewHolder) fromholde;

            titleViewHolder.txtTitle.setText((String) contactListFiltered.get(pposition));
        }
        else if (fromholde instanceof HeaderViewHolder) {

            HeaderViewHolder titleViewHolder = (HeaderViewHolder) fromholde;

            titleViewHolder.dateView_programs.removeAllViews();

            for (int num = 0; num < memberList.size(); num++) {
                AllRecordsMainResponse.AllRecordsMember obj = memberList.get(num);
                inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
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

                        img_profile_shadow.setBackgroundResource(R.drawable.orange_circle_background_image);
                        //  img_profile_shadow.setBackgroundResource(R.drawable.orange_circle_background);
                        txt_goalname.setTextColor(Color.parseColor("#ff860b"));
                        //   }
                        listener_viewReport.onSelectFamilyMember(obj);


//                        if(selectedUuserId.equals(obj.getId())) {
//                        img_profile_shadow.setBackgroundResource(R.drawable.orange_circle_background_image);
//                        img_profile_shadow.setBackgroundResource(R.drawable.orange_circle_background);
//                        txt_goalname.setTextColor(Color.parseColor("#ff860b"));
//                        }

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

                        if(row instanceof  AllRecordsMainResponse.AllRecordsReport){
                            AllRecordsMainResponse.AllRecordsReport obb= (AllRecordsMainResponse.AllRecordsReport) row;

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

            if (cellType==1) {
                shouldShow_Title = true;
                shouldShow_Name = true;
                shouldShow_ReportName = true;
                shouldShow_Date = true;

                shouldShow_Hospital_Logo = true;
                shouldShow_Is_New = false;

            }
            else if (cellType==0) {
                //New Cell .................
                shouldShow_Title = true;
                shouldShow_Name = true;
                shouldShow_ReportName = true;
                shouldShow_Date = true;

                shouldShow_Hospital_Logo = true;
                shouldShow_Is_New = true;
            }

            if(reportObj.getAssign_user_id()==null){
                shouldShow_Is_Not_Assign=true;
            }else{
                shouldShow_Is_Not_Assign=false;
            }

            if(reportObj.getHosUid()==null){
                isHospital_ID_Null=true;
            }else{
                isHospital_ID_Null=false;
            }
            // hos_uid

            // Is need to show download Active ...

            if(reportObj.getDownload_url()==null){
                isDownloadActive=false;
                System.out.println("isDownloadActive=====0========================"+reportObj.getDownload_url());
            }else if((reportObj.getDownload_url()!=null) && (reportObj.getDownload_url().length()>5)){

                if(reportObj.getDownload_url().contains("pdf")){
                    isDownloadActive=true;
                    System.out.println("isDownloadActive=====1========================"+reportObj.getDownload_url());
                }else{
                    isDownloadActive=false;
                    System.out.println("isDownloadActive=====0========================"+reportObj.getDownload_url());
                }


            }





        }
    }

    private void setupCellViewForPost(ReportCellViewHolder holder, AllRecordsMainResponse.AllRecordsReport reportObj) {

        shouldShow_User_Image = false;
        shouldShow_User_Name = false;

        // Default Content visibility ..........................
//        holder.txt_user_name.setVisibility(View.VISIBLE);
//        holder.txt_rreport_name.setVisibility(View.VISIBLE);
//        holder.txt_report_date.setVisibility(View.VISIBLE);
//        holder.img_hospital_logo_ic.setVisibility(View.VISIBLE);

        if(cellType==1){
            shouldShow_Title=true;
        }



//        // New Logo and Not assign Icon visibility ..........................
//        if (shouldShow_Is_New) {
//            holder.img_btn_assgin_new.setVisibility(View.VISIBLE);
//
//        } else {
//            holder.img_btn_assgin_new.setVisibility(View.GONE);
//
//            holder.assign_user_name.setVisibility(View.VISIBLE);
//            holder.img_user_pic_ic.setVisibility(View.VISIBLE);
//            holder.img_user_pic_ic2.setVisibility(View.VISIBLE);
//        }
//
//
//        if (shouldShow_Is_Not_Assign) {
//            holder.img_not_assign_ic.setVisibility(View.VISIBLE);
//            holder.assign_user_name.setVisibility(View.GONE);
//            holder.img_user_pic_ic.setVisibility(View.GONE);
//            holder.img_user_pic_ic2.setVisibility(View.GONE);
//
//        } else {
//            holder.img_not_assign_ic.setVisibility(View.GONE);
//        }
//
//
//
//        //-------IS this a Vital Report----------------------------------------
//        if(isHospital_ID_Null){
//            String assignUserId=  reportObj.getAssign_user_id();
//            int ssss= reportObj.getRead();
//
//            for (int ii=0;ii <memberList.size(); ii++){
//                AllRecordsMainResponse.AllRecordsMember mem= (AllRecordsMainResponse.AllRecordsMember) memberList.get(ii);
//
//                if(mem.getId().equals(assignUserId)){
//                    holder.assign_user_name.setText(mem.getName());
//
//                    String imageURL=mem.getProfilePictureUrl();
//                    RequestOptions requestOptions1 = new RequestOptions().transform(new CircleTransform(context));
//                    Glide.with(context).load(imageURL)
//                            .apply(requestOptions1)
//                            .into(holder.img_user_pic_ic);
//                }
//            }
//
//        }else{
//            String assignUserId=  reportObj.getAssign_user_id();
//            int ssss= reportObj.getRead();
//
//            for (int ii=0;ii <memberList.size(); ii++){
//                AllRecordsMainResponse.AllRecordsMember mem= (AllRecordsMainResponse.AllRecordsMember) memberList.get(ii);
//
//                if(mem.getId().equals(assignUserId)){
//                    holder.assign_user_name.setText(mem.getName());
//
//                    String imageURL=mem.getProfilePictureUrl();
//                    RequestOptions requestOptions1 = new RequestOptions()
//                            .transform(new CircleTransform(context));
//                    Glide.with(context).load(imageURL)
//                            .apply(requestOptions1)
//                            .into(holder.img_user_pic_ic);
//                }
//            }
//        }
//
//
//        if(reportObj.getFullName()!=null){
//            holder.txt_user_name.setVisibility(View.VISIBLE);
//            holder.txt_user_name.setText(reportObj.getFullName());
//        }else{
//            holder.txt_user_name.setVisibility(View.GONE);
//
//        }
//
//        holder.img_btn_review.setAlpha((float) 0.2);
//        holder.txt_btn_review.setTextColor(Color.parseColor("#999999"));
//
//
//        //-------Ended  IS this a Vital Report----------------------------------------
//
//        if(isDownloadActive){
//            holder.img_btn_download.setAlpha((float)1.0);
//            holder.txt_btn_download.setTextColor(Color.parseColor("#747473"));
//
//            holder.img_btn_share.setAlpha((float)1.0);
//            holder.txt_btn_share.setTextColor(Color.parseColor("#747473"));
//
//        }else{
//            holder.img_btn_download.setAlpha((float) 0.2);
//            holder.txt_btn_download.setTextColor(Color.parseColor("#999999"));
//
//            holder.img_btn_share.setAlpha((float) 0.2);
//            holder.txt_btn_share.setTextColor(Color.parseColor("#999999"));
//        }

        // configure view holder
      // holder.layout_bottom_view.setVisibility(View.GONE);

        // Only show New Title when New once only.......... visibility ..........................
//        if((cellType==0) &&(firstReportID.equals(reportObj.getId()))){
//            shouldShow_Title=true;
//        }
        if(cellType==0){
            shouldShow_Title=false;
        }

//        if(reportObj.getHospital()!=null){
//            holder.img_hospital_logo_ic.setVisibility(View.VISIBLE);
//            String hosLogo = reportObj.getHospital();
//            //  https://livehappy.ayubo.life/custom/include/images/ech_hospitals/hemas.jpg
//            Glide.with(context).load(hosLogo)
//                    .into(holder.img_hospital_logo_ic);
//        }else{
//            holder.img_hospital_logo_ic.setVisibility(View.GONE);
//        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position==0)
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

       TextView txt_report_name,txt_report_description,btn_view;
       ImageView img_selection;
        LinearLayout layout_main;
        public ReportCellViewHolder(View rowView) {
            super(rowView);

            btn_view =  rowView.findViewById(R.id.btn_view);

            layout_main =  rowView.findViewById(R.id.layout_main);
            img_selection =  rowView.findViewById(R.id.img_selection);
            txt_report_name =  rowView.findViewById(R.id.txt_report_name);
            txt_report_description =  rowView.findViewById(R.id.txt_report_description);

        }

    }








}
