package com.ayubo.life.ayubolife.channeling.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;


import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.model.VideoCallSession;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;

public class VideoSessionAdapter extends RecyclerView.Adapter<VideoSessionAdapter.VideoViewHolder> {

    //instances
    private Context context;
    private List<VideoCallSession> locations;
    private OnVideoSessionListener listener;

    public VideoSessionAdapter(Context context, List<VideoCallSession> locations) {
        this.context = context;
        this.locations = locations;
    }

    public void setOnVideoSessionListener(OnVideoSessionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_video_session_row, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoCallSession location = locations.get(position);
        if (location != null) {
            holder.txtDate.setText(TimeFormatter.millisecondsToString(location.getStart().getTime(), "EEEE, MMM dd, yyyy"));

            String duration = String.format(Locale.getDefault(), "%s - %s", TimeFormatter.
                    millisecondsToString(location.getStart().getTime(), "h:mm aa").toUpperCase(), TimeFormatter.
                    millisecondsToString(location.getEnd().getTime(), "h:mm aa").toUpperCase());
            holder.txtTime.setText(duration.toUpperCase());
        }

        final int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onVideoSessionClicked(locations.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(locations != null)
            return locations.size();
        else
            return 0;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime;

        VideoViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_video_session_date);
            txtTime = itemView.findViewById(R.id.txt_video_session_time);
        }
    }


    public interface OnVideoSessionListener {
        void onVideoSessionClicked(VideoCallSession location);
    }


}
