package com.ayubo.life.ayubolife.channeling.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;

import java.util.List;

public class DetailsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    public static final int HEADER_TYPE = 1;
    public static final int DETAIL_TYPE = 2;
    public static final int BOOTOM_TYPE = 3;


    //instances
    private Activity activity;
    private List<Object> values;
    private OnItemClickListener listener;
    private SearchActivity.SearchActions searchActions;
    DBString dataString;
    private OnClickBack listener_familymember;

    public interface OnClickBack {
        void onBackClick();
    }

    public void setOnClickFamilyMemberListener(OnClickBack listener) {
        this.listener_familymember = listener;
    }

    public DetailsViewAdapter(Activity activity, List<Object> values, SearchActivity.SearchActions searchActions, DBString data) {
        this.activity = activity;
        this.values = values;
        this.searchActions = searchActions;
        this.dataString=data;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == HEADER_TYPE) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_chanelling_detailsview_header_raw, parent, false);
            return new HeaderViewHolder(view);
        }
        else if (viewType == BOOTOM_TYPE) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_summary_row, parent, false);
            return new HeaderViewHolder(view);
        }
        else {
            view = LayoutInflater.from(activity).inflate(R.layout.component_summary_row, parent, false);
            return new DetailViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        String name = "";
        String value = "";
        String imageUrl = "";

        if (searchActions != null) {
            name = searchActions.getName(values.get(position));
            value = searchActions.getValue(values.get(position));
            imageUrl = searchActions.getImageUrl(values.get(position));
            System.out.println("=======getViewType============"+searchActions.getViewType());

        }

        if (holder instanceof DetailViewHolder) {
            DetailViewHolder detailViewHolder = (DetailViewHolder) holder;

            detailViewHolder.txtTitle.setText(Utility.upperCaseWords(name));

            if(detailViewHolder.txtSubTitle != null) {
                detailViewHolder.txtSubTitle.setText(value);
                if (isNumeric(imageUrl))
                    detailViewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(activity, Integer.parseInt(imageUrl)));
                else
                    Glide.with(activity).load(imageUrl).into(detailViewHolder.imageView);
            }
        } else {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.txt_DocName.setText(dataString.getId());
            headerViewHolder.txt_specialty_doctor_info.setText(dataString.getName());
            headerViewHolder.txt_note_doctor_info.setText(name);

            Glide.with(activity).load(Ram.getDoctorImage()).into(headerViewHolder.img_doc_doctor_info);

            headerViewHolder.btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener_familymember != null)
                        listener_familymember.onBackClick();
                }
            });
            headerViewHolder.btn_back_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener_familymember != null)
                        listener_familymember.onBackClick();
                }
            });
//            headerViewHolder.btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                  //  finish();
//                }
//            });
//
//            headerViewHolder.btn_back_Button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                  //  finish();
//                }
//            });
        }

        final int pos = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && holder instanceof DetailViewHolder)
                    listener.onItemClick(values.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (values == null)
            return 0;
        else
            return values.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if(position==0){
            type=1;
        }
        else if(position==values.size()){
            type=3;
        }else{
            type=2;
        }

        System.out.println("==================type=============="+type);
        return type;
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtSubTitle;
        ImageView imageView;

        DetailViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_name_search_row);
            txtSubTitle = itemView.findViewById(R.id.txt_sub_name_search_row);
            imageView = itemView.findViewById(R.id.img_search_row);
        }
    }
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txt_DocName,txt_specialty_doctor_info,txt_note_doctor_info;
        LinearLayout btn_backImgBtn_layout;
        ImageButton btn_back_Button;
        ImageView img_doc_doctor_info;

        HeaderViewHolder(View itemView) {
            super(itemView);
            txt_DocName = itemView.findViewById(R.id.txt_doctor_name_doctor_info);
            txt_specialty_doctor_info = itemView.findViewById(R.id.txt_specialty_doctor_info);
            txt_note_doctor_info = itemView.findViewById(R.id.txt_note_doctor_info);

            img_doc_doctor_info = itemView.findViewById(R.id.img_doc_doctor_info);
            btn_backImgBtn_layout = itemView.findViewById(R.id.btn_backImgBtn_layout);
            btn_back_Button = itemView.findViewById(R.id.btn_backImgBtn);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(Object object);
    }

    private static boolean isNumeric(String str) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

