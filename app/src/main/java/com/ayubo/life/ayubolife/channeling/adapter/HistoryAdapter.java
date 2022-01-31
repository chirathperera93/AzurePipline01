package com.ayubo.life.ayubolife.channeling.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.model.History;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;
import com.ayubo.life.ayubolife.utility.Utility;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_TITLE = R.layout.component_history_title_row;
    private static final int VIEW_TYPE_ITEM = R.layout.component_history_item_row;
    private static final int VIEW_TYPE_VIDEO = R.layout.component_history_item_video_row;

    //instances
    private List<Object> items;
    private Context context;
    private OnCancelAppointmentListener listener;

    public HistoryAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnAppointmentCancelListener(OnCancelAppointmentListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        if (viewType == VIEW_TYPE_ITEM)
            return new ItemViewHolder(view);
        else if (viewType == VIEW_TYPE_VIDEO)
            return new VideoViewHolder(view);
        else
            return new TitleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            History history = (History) items.get(position);

            String docName = history.getDocname();
            docName = Utility.upperCaseWords(docName);

            itemViewHolder.txtExpert.setText(String.format("%s %s", history.getTitle(), docName));

            Date startDate = TimeFormatter.stringToDate(history.getStarts(), "yyyy-MM-dd hh:mm:ss");
            if (!startDate.after(new Date())) {
                itemViewHolder.txtCancel.setVisibility(View.GONE);
            } else {
                itemViewHolder.txtCancel.setVisibility(View.GONE);
            }

            itemViewHolder.txtSource.setText(String.format(Locale.getDefault(), "Source : %s", history.getSource()));

            if (holder instanceof VideoViewHolder) {
                itemViewHolder.imgIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.video_call_small_selected));
                itemViewHolder.txtSource.setText(String.format(Locale.getDefault(), "Source : %s", "Ayubo"));
                itemViewHolder.txtDate.setText(TimeFormatter.millisecondsToString(startDate.getTime(), "EEEE, MMM dd, yyyy     h:mm aa"));
            } else {
                itemViewHolder.imgIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.hospital_appointment_icon));


                // public SimpleDateFormat ddMMMyyyyhhmmampmFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US);
                String appointmentDate = TimeFormatter.millisecondsToString(startDate.getTime(), "dd MMM yyyy hh:mm a");

                //   System.out.println("===================yy======================="+history.getChannel_number());
                String channlNumber = null;
                if (history.getChannel_number() == null) {
                    channlNumber = "";
                } else {
                    channlNumber = history.getChannel_number();
                }

                //  Html.fromHtml(String.format(Locale.getDefault(), "%s <b>#%d</b>", appointmentDate, 17));

                if ((history.getHospital_name() != null) && (history.getHospital_name().length() > 0)) {
                    String location = "Location : " + history.getHospital_name();
                    itemViewHolder.txtLocation.setText(location);
                } else {
                    itemViewHolder.txtLocation.setVisibility(View.GONE);
                }

                if ((history.getChannel_ref() != null) && (history.getChannel_ref().length() > 0)) {
                    String reference = "Reference : " + history.getChannel_ref();
                    itemViewHolder.txtReference.setText(reference);
                } else {
                    itemViewHolder.txtReference.setVisibility(View.GONE);
                }


                itemViewHolder.txtDate.setText(appointmentDate + "   #" + Html.fromHtml(channlNumber));

            }

            final int pos = itemViewHolder.getAdapterPosition();
            itemViewHolder.txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onCancel((History) items.get(pos));
                }
            });

        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

            titleViewHolder.txtTitle.setText((String) items.get(position));
        }

    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else
            return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String)
            return VIEW_TYPE_TITLE;
        else if (items.get(position) instanceof History) {
            History history = (History) items.get(position);
            if (history.getLocation().equals("Video Chat"))
                return VIEW_TYPE_VIDEO;
            else
                return VIEW_TYPE_ITEM;
        }
        return super.getItemViewType(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView txtExpert, txtDate, txtCancel, txtSource, txtLocation, txtReference;

        ItemViewHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_history_item_row);
            txtExpert = itemView.findViewById(R.id.txt_history_item_row_expert);
            txtDate = itemView.findViewById(R.id.txt_history_item_row_date);
            txtCancel = itemView.findViewById(R.id.txt_history_item_row_cancel);
            txtSource = itemView.findViewById(R.id.txt_history_item_row_source);
            txtLocation = itemView.findViewById(R.id.txt_history_item_row_location);
            txtReference = itemView.findViewById(R.id.txt_history_item_row_reference);

        }
    }

    class VideoViewHolder extends ItemViewHolder {

        TextView txtLocation;

        VideoViewHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_history_item_row);
            txtExpert = itemView.findViewById(R.id.txt_history_item_row_expert);
            txtDate = itemView.findViewById(R.id.txt_history_item_row_date);
            txtSource = itemView.findViewById(R.id.txt_history_item_row_source);
            txtLocation = itemView.findViewById(R.id.txt_history_item_row_location);
            txtCancel = itemView.findViewById(R.id.txt_history_item_row_cancel);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_history_title_row);
        }
    }

    public interface OnCancelAppointmentListener {
        void onCancel(History history);
    }
}
