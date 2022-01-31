package com.ayubo.life.ayubolife.channeling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.activity.VideoSessionActivity;
import com.ayubo.life.ayubolife.channeling.model.VideoCallSession;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_DAY_DEFAULT = R.layout.component_video_session_day_row;
    private static final int VIEW_TYPE_DAY_SELECTED = R.layout.component_video_session_day_selected_row;
    private static final int VIEW_TYPE_TIME_DEFAULT = R.layout.component_video_session_time_default_row;
    private static final int VIEW_TYPE_TIME_SELECTED = R.layout.component_video_session_time_selected_row;
    private static final int VIEW_TYPE_TIME_DISABLED = R.layout.component_video_session_time_disabled_row;
    private static final int VIEW_TYPE_SLOT_DEFAULT = R.layout.component_video_session_slot_default_row;
    private static final int VIEW_TYPE_SLOT_SELECTED = R.layout.component_video_session_slot_selected_row;
    private static final int VIEW_TYPE_SLOT_DISABLED = R.layout.component_video_session_slot_disabled_row;

    //instances
    private Context context;
    private List<Object> data;
    private String[] weeks;
    //    private String[] months;
    private OnItemSelected listener;
    private Object selectedItem;

    //primary data
    private int previousSelection = 0;

    public SessionAdapter(Context context, List<Object> data, Object selectedItem) {
        this.context = context;
        this.data = data;
        this.selectedItem = selectedItem;

        this.weeks = new DateFormatSymbols().getWeekdays();
//        this.months = new DateFormatSymbols().getMonths();
    }

    public void setOnItemClickListener(OnItemSelected listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        if (viewType == VIEW_TYPE_DAY_DEFAULT || viewType == VIEW_TYPE_DAY_SELECTED)
            return new DateViewHolder(view);
        else if (viewType == VIEW_TYPE_TIME_DEFAULT || viewType == VIEW_TYPE_TIME_SELECTED || viewType == VIEW_TYPE_TIME_DISABLED)
            return new TimeViewHolder(view);
        else if (viewType == VIEW_TYPE_SLOT_DEFAULT || viewType == VIEW_TYPE_SLOT_SELECTED || viewType == VIEW_TYPE_SLOT_DISABLED)
            return new SlotViewHolder(view);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DateViewHolder) {
            Calendar date = Calendar.getInstance();
            date.setTime(((VideoCallSession) data.get(position)).getStart());
            final DateViewHolder dateHolder = (DateViewHolder) holder;
            dateHolder.txtDate.setText(String.valueOf(date.get(Calendar.DATE)));
//            dateHolder.txtMonth.setText(months[date.get(Calendar.MONTH)].substring(0, 3));
            dateHolder.txtWeek.setText(weeks[date.get(Calendar.DAY_OF_WEEK)].substring(0, 3));
        } else if (holder instanceof TimeViewHolder) {
            Calendar videoSession = ((VideoSessionActivity.VideoCallTime) data.get(position)).getCalendar();
            TimeViewHolder timeHolder = (TimeViewHolder) holder;

            timeHolder.txtMeridiem.setText(TimeFormatter.millisecondsToString(videoSession.getTimeInMillis(), "aa").toUpperCase());
            timeHolder.txtTime.setText(TimeFormatter.millisecondsToString(videoSession.getTimeInMillis(), "hh:mm"));

        } else if (holder instanceof SlotViewHolder) {
            Calendar startCalendar = ((VideoSessionActivity.VideoCallSlot) data.get(position)).getStart();
            Calendar endCalendar = ((VideoSessionActivity.VideoCallSlot) data.get(position)).getEnd();
            SlotViewHolder slotHolder = (SlotViewHolder) holder;

            String startTime = TimeFormatter.millisecondsToString(startCalendar.getTimeInMillis(), "hh:mm aa");
            String endTime = TimeFormatter.millisecondsToString(endCalendar.getTimeInMillis(), "hh:mm aa");

            slotHolder.txtTimeSlots.setText(String.format(Locale.getDefault(), "%s - %s", startTime, endTime));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewType = getItemViewType(holder.getAdapterPosition());
                if (viewType != VIEW_TYPE_SLOT_DISABLED && viewType != VIEW_TYPE_TIME_DISABLED) {
                    selectedItem = data.get(holder.getAdapterPosition());
                    notifyItemChanged(previousSelection);
                    notifyItemChanged(holder.getAdapterPosition());
                    previousSelection = holder.getAdapterPosition();
                    if (listener != null)
                        listener.onItemClicked(data.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof VideoSessionActivity.VideoCallTime) {
            if (data.get(position) == selectedItem) {
                previousSelection = position;
                return VIEW_TYPE_TIME_SELECTED;
            } else if (((VideoSessionActivity.VideoCallTime) data.get(position)).isEnabled())
                return VIEW_TYPE_TIME_DEFAULT;
            else
                return VIEW_TYPE_TIME_DISABLED;
        } else if (data.get(position) instanceof VideoCallSession) {
            String dateString = TimeFormatter.millisecondsToString(((VideoCallSession) data.get(position)).getStart().getTime(), "yyyy-MM-dd");
            if (dateString.equals(TimeFormatter.millisecondsToString(((VideoCallSession) selectedItem).getStart().getTime(), "yyyy-MM-dd"))) {
                previousSelection = position;
                return VIEW_TYPE_DAY_SELECTED;
            } else
                return VIEW_TYPE_DAY_DEFAULT;
        } else if (data.get(position) instanceof VideoSessionActivity.VideoCallSlot) {
            if (data.get(position) == selectedItem) {
                previousSelection = position;
                return VIEW_TYPE_SLOT_SELECTED;
            } else {
                VideoSessionActivity.VideoCallSlot slot = (VideoSessionActivity.VideoCallSlot) data.get(position);
                if (slot.isEnabled())
                    return VIEW_TYPE_SLOT_DEFAULT;
                else
                    return VIEW_TYPE_SLOT_DISABLED;
            }
        } else
            return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        else
            return data.size();
    }

    class DateViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDate, txtWeek;

        DateViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_video_session_day_row_date);
            txtWeek = itemView.findViewById(R.id.txt_video_session_day_row_week);
//            txtMonth = itemView.findViewById(R.id.txt_video_session_day_row_month);
        }
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMeridiem, txtTime;

        TimeViewHolder(View itemView) {
            super(itemView);
            txtMeridiem = itemView.findViewById(R.id.txt_video_session_time_row_meridiem);
            txtTime = itemView.findViewById(R.id.txt_video_session_time_row_time);
        }
    }

    class SlotViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTimeSlots;

        SlotViewHolder(View itemView) {
            super(itemView);
            txtTimeSlots = itemView.findViewById(R.id.txt_video_session_slot_row);
        }
    }

    public interface OnItemSelected {
        void onItemClicked(Object item);
    }
}
