package com.ayubo.life.ayubolife.map_challenges.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.map_challenges.model.NewLeaderboard;
import com.ayubo.life.ayubolife.reports.utility.Utility_Report;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_BASIC_LEADERBOARD = R.layout.component_raw_leaderboard;
    private static final int VIEW_TYPE_ITEM = R.layout.component_raw_leaderboard;
    private static final int VIEW_TYPE_VIDEO = R.layout.component_raw_leaderboard;

    //instances
    private List<NewLeaderboard> items;
    private Context context;
    private OnCancelAppointmentListener listener;

    public LeaderBoardAdapter(Context context, List<NewLeaderboard> dataList) {
        this.context = context;
        this.items = dataList;
    }

    public void setOnAppointmentCancelListener(OnCancelAppointmentListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        if (viewType == VIEW_TYPE_BASIC_LEADERBOARD)
            return new LeaderboardViewHolder(view);
        else if (viewType == VIEW_TYPE_VIDEO)
            return new LeaderboardViewHolder(view);
        else
            return new TitleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeaderboardViewHolder) {

            LeaderboardViewHolder leaderboardViewHolder = (LeaderboardViewHolder) holder;
            NewLeaderboard leaderboard = (NewLeaderboard) items.get(position);

            String name = Utility_Report.makeFirstLetterCapital(leaderboard.getName());


            leaderboardViewHolder.txt_leaderboard_item_row_name.setText(name);

            String rank = leaderboard.getPosition();
            if (rank.length() == 1) {
                rank = "0" + rank;
            }

            leaderboardViewHolder.txt_leaderboard_item_row_rank.setText(rank);

            leaderboardViewHolder.txt_leaderboard_item_row_steps.setText(leaderboard.getValue());
            if (leaderboard.getCompleted()) {
                leaderboardViewHolder.txt_leaderboard_item_row_status.setText("Completed");
            }


            final int pos = leaderboardViewHolder.getAdapterPosition();
            RequestOptions requestOptions1 = new RequestOptions()
                    .transform(new CircleTransform(context));
            Glide.with(context).load(leaderboard.getImage())
                    .apply(requestOptions1)
                    .into(leaderboardViewHolder.img_leaderboard_user_pic);


        } else if (holder instanceof TitleViewHolder) {
            // TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

            //   titleViewHolder.txtTitle.setText((String) items.get(position));
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
        if (items.get(position) instanceof NewLeaderboard) {

            return VIEW_TYPE_ITEM;
        }


        return super.getItemViewType(position);
    }

    class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        ImageView img_leaderboard_user_pic;
        TextView txt_leaderboard_item_row_status, txt_leaderboard_item_row_name, txt_leaderboard_item_row_steps, txt_leaderboard_item_row_rank;

        LeaderboardViewHolder(View itemView) {
            super(itemView);
            img_leaderboard_user_pic = itemView.findViewById(R.id.img_leaderboard_user_pic);
            txt_leaderboard_item_row_name = itemView.findViewById(R.id.txt_leaderboard_item_row_name);
            txt_leaderboard_item_row_rank = itemView.findViewById(R.id.txt_leaderboard_item_row_rank);
            txt_leaderboard_item_row_steps = itemView.findViewById(R.id.txt_leaderboard_item_row_steps);
            txt_leaderboard_item_row_status = itemView.findViewById(R.id.txt_leaderboard_item_row_status);


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
        void onCancel(NewLeaderboard history);
    }
}

