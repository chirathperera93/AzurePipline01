package com.ayubo.life.ayubolife.notification.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.notification.model.NotificationData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //instances
    private Context context;
    private List<NotificationData> reportsList;
    private List<NotificationData> contactListFiltered;
    private String currentUserId;
    private NotificationsAdapter.OnClickNotificationCell listener;


    public NotificationsAdapter(Context context, List<NotificationData> reportsListt, String currentUserId) {
        this.context = context;
        reportsList = new ArrayList<>();
        reportsList = reportsListt;
        this.contactListFiltered = reportsListt;
        this.currentUserId = currentUserId;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.raw_notification_details_cell, parent, false);
        viewHolder = new NotificationsAdapter.ViewHolder(viewItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fromholde, final int pposition) {

        if (fromholde instanceof NotificationsAdapter.ViewHolder) {


            final NotificationData reportObj = (NotificationData) contactListFiltered.get(pposition); // PostCell
            final NotificationsAdapter.ViewHolder holder = (NotificationsAdapter.ViewHolder) fromholde;

            holder.txt_name.setText(reportObj.getHeader());
            holder.txt_desc.setText(reportObj.getTitle());


            if (reportObj.getUnread_count() > 0) {
                holder.user_image_unread_count.setVisibility(View.VISIBLE);
                holder.user_image_unread_count.setText(reportObj.getUnread_count().toString());

                holder.txt_name.setTextColor(context.getResources().getColor(R.color.black));
                holder.txt_name.setTypeface(holder.txt_name.getTypeface(), Typeface.BOLD);

                holder.txt_desc.setTextColor(context.getResources().getColor(R.color.black));
                holder.txt_desc.setTypeface(holder.txt_desc.getTypeface(), Typeface.BOLD);

                holder.txt_notification_time.setTextColor(context.getResources().getColor(R.color.black));
                holder.txt_notification_time.setTypeface(holder.txt_notification_time.getTypeface(), Typeface.BOLD);
            } else {
                holder.user_image_unread_count.setVisibility(View.GONE);
            }


            if (reportObj.getStatus().equals("inactive")) {

                holder.txt_name.setTextColor(context.getResources().getColor(R.color.color_727272));

                holder.txt_desc.setTextColor(context.getResources().getColor(R.color.color_727272));

                holder.txt_notification_time.setTextColor(context.getResources().getColor(R.color.color_727272));
            }


            CharSequence timeFromNow = DateUtils.getRelativeTimeSpanString(
                    reportObj.getTimestamp(),
                    System.currentTimeMillis(),
                    0,
                    DateUtils.FORMAT_ABBREV_RELATIVE
            );

            holder.txt_notification_time.setText(timeFromNow);


            Glide.with(context).load(reportObj.getIcon())
                    .into(holder.img_user_notificaton);


            final int pos = holder.getAdapterPosition();

            holder.main_layer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onSelectNotificationCell((NotificationData) reportsList.get(pos));
                }
            });

        }

    }

    public static Long utcToLocal(Date utcDate) {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        return utcDate.getTime() - offsetInMillis;
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
                    List<NotificationData> filteredList = new ArrayList<>();
                    for (Object row : reportsList) {

                        if (row instanceof NotificationData) {
                            NotificationData obb = (NotificationData) row;

                            if (obb.getTitle().toLowerCase().contains(charString.toLowerCase()) || obb.getHeader().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add((NotificationData) row);
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
                contactListFiltered = (ArrayList<NotificationData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemViewType(int position) {

//        if (reportsList.get(position) instanceof NotificationData)
//            return VIEW_TYPE_TITLE;

        return super.getItemViewType(position);
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_desc, txt_notification_time, user_image_unread_count;
        ImageView img_user_notificaton;
        //        ImageView txt_notification_readed;
        RelativeLayout main_layer;

        public ViewHolder(View rowView) {
            super(rowView);

            // configure view holder
            main_layer = rowView.findViewById(R.id.main_layer);

            txt_name = rowView.findViewById(R.id.txt_name);
            txt_desc = rowView.findViewById(R.id.txt_desc);
            txt_notification_time = rowView.findViewById(R.id.txt_notification_time);
            user_image_unread_count = rowView.findViewById(R.id.user_image_unread_count);

            img_user_notificaton = rowView.findViewById(R.id.img_user_notificaton);
//            txt_notification_readed = rowView.findViewById(R.id.txt_notification_readed);

        }

    }


    public void setOnSelectNotificationListener(NotificationsAdapter.OnClickNotificationCell listene) {
        this.listener = listene;
    }

    //    Interface For Events Handing ...........
    public interface OnClickNotificationCell {
        void onSelectNotificationCell(NotificationData history);
    }


}

