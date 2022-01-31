package com.ayubo.life.ayubolife.goals_extention;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.pojo.timeline.Post;
import com.ayubo.life.ayubolife.timeline.pagination.PaginationAdapterCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardViewActivityNew extends AppCompatActivity {
    RecyclerView rv; LinearLayoutManager linearLayoutManager;

    PaginationAdapter adapter;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    Context contextt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_view_new);

        contextt.getApplicationContext();

        rv = (RecyclerView) findViewById(R.id.main_recycler);
        rv.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(DashboardViewActivityNew.this, LinearLayoutManager.VERTICAL, false);
        adapter = new PaginationAdapter(contextt, "");
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

    }


    class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        boolean shouldShow_TitlePanel = true;
        boolean shouldShow_ContentPanel = true;
        boolean shouldShow_UserInteractionPanel = true;

        boolean shouldShow_MediaPanel = false;
        boolean shouldShow_ProgramPanel = false;
        boolean shouldShow_OverlayImage = false;
        boolean shouldShow_LinkPreviewPanel = false;

        // View Types
        private static final int DATES = 0;
        private static final int CHART = 1;
        private static final int GOALS = 2;

        private List<Post> movieResults;
        private Context context;

        private boolean isLoadingAdded = false;

        String userid_ExistingUser, cellType;
        private PaginationAdapterCallback mCallback;


        public PaginationAdapter(Context context, String userid_ExistingUser) {
            this.context = context;
            this.mCallback = (PaginationAdapterCallback) context;
            movieResults = new ArrayList<>();
            this.userid_ExistingUser = userid_ExistingUser;

        }

        public List<Post> getMovies() {
            return movieResults;
        }

        public void setMovies(List<Post> movieResults) {
            this.movieResults = movieResults;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());


            switch (viewType) {
                case DATES:
                    View viewItem = inflater.inflate(R.layout.dashboard_cell_dates, parent, false);
                    viewHolder = new PaginationAdapter.PostCell(viewItem);
                    break;
                case CHART:
                    View viewLoading = inflater.inflate(R.layout.dashboard_cell_charts, parent, false);
                    viewHolder = new PaginationAdapter.LoadingVH(viewLoading);
                    break;
                case GOALS:
                    View viewHero = inflater.inflate(R.layout.dashboard_cell_goals, parent, false);
                    viewHolder = new PaginationAdapter.ServiceMenuView(viewHero);
                    break;
            }
            return viewHolder;
        }


        public void remove(int position) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holde, final int position) {


            switch (getItemViewType(position)) {

                case DATES:
                    final Post obj = movieResults.get(position); // PostCell
                    final PaginationAdapter.PostCell holder = (PaginationAdapter.PostCell) holde;

                    if (obj.getPostId() == null) {
                        holder.title_panel.setVisibility(View.GONE);
                        holder.content_panel.setVisibility(View.GONE);
                        holder.media_panel.setVisibility(View.GONE);
                        holder.user_link_panel.setVisibility(View.GONE);
                        holder.user_reaction_panel.setVisibility(View.GONE);
                    } else {

                        cellType = obj.getType();
                        if (cellType == null) {
                            cellType = "0";
                        }

                        //======   SETUP CELL STATUS ============================================
                        setupCellStatus(obj);
                        //======   SETUP CELL VIEW ============================================


                        String postedUserId = null;

                        holder.img_btn_close_intop.setTag(obj);

                    }


                    break;



            }
        }

        @Override
        public int getItemCount() {

            int finalCount;

            finalCount = movieResults == null ? 0 : movieResults.size();

            return finalCount;

        }

        void openZoomImage(Integer pos, Post pobj) {

        }

        String getImageUrlFromType(Post obj) {
            String imageURL = null;
            obj.getType();
            if (obj.getType().equals("IMAGE_POST")) {
                imageURL = obj.getPostImageUrl();
            }
            if (obj.getType().equals("VIDEO_POST")) {
                imageURL = obj.getVideoThumbnail();
            }
            if (obj.getType().equals("GIF_POST")) {
                imageURL = obj.getGifThumbnail();
            }
            if (obj.getType().equals("LINK_POST")) {
                imageURL = obj.getLinkInfo().getThumbnailUrl();
            }
            if (obj.getType().equals("SYSTEM_POST")) {
                imageURL = obj.getPostImageUrl();
            }
            if (obj.getType().equals("PROGRAM_POST")) {
                imageURL = obj.getPostImageUrl();
            }
            if (obj.getType().equals("IMAGE_POST")) {
                imageURL = obj.getPostImageUrl();
            }
            //  System.out.println("==============================================="+obj.getType());
            //   System.out.println("===u=="+imageURL);
            return imageURL;
        }

        String getLikesStringFromCount(Integer liked_count) {
            String likeCounts = null;
            if (liked_count > 1) {
                likeCounts = Integer.toString(liked_count) + " Likes";
            } else if (liked_count == 1) {
                likeCounts = Integer.toString(liked_count) + " Like";
            } else if (liked_count == 0) {
                likeCounts = Integer.toString(liked_count) + " Like";
            }

            return likeCounts;
        }

        String getTimeStringFromInteger(Integer timeInInteger) {

            long number = Long.valueOf(timeInInteger);
            number = number * 1000;
            // Date date=new Date(number);
            Calendar cal = Calendar.getInstance();
            Calendar todayCal = Calendar.getInstance();
            Date d = new Date(number);
            cal.setTime(d);

            int date = cal.get(Calendar.DATE);
            Date todayDate = new Date();

            todayCal.setTime(todayDate);
            int cameDate = cal.get(Calendar.DATE);
            int currDate = todayCal.get(Calendar.DATE);
            SimpleDateFormat formatter = null;
            if (cameDate == currDate) {
                formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss'X' z");
            } else {
                formatter = new SimpleDateFormat("E, dd MMM yyyy'X'HH:mm:ss z");
            }
            String dateFromDB_forADay = formatter.format(cal.getTime());
            String[] parts = dateFromDB_forADay.split("X");
            String part1 = parts[0];

            return part1;
        }

        private void setupCellStatus(Post postObj) {
            cellType = postObj.getType();

            if (postObj != null) {

                if (cellType.equals("NORMAL_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = false;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("IMAGE_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("VIDEO_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = true;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("GIF_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = true;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("LINK_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = true;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("SYSTEM_POST")) {
                    shouldShow_MediaPanel = postObj.hasImage();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_ContentPanel = postObj.hasBody();

                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;

                    if (postObj.isInteractionsEnabled()) {
                        shouldShow_UserInteractionPanel = true;   //E  Optional
                    } else {
                        shouldShow_UserInteractionPanel = false;  //E  Optional
                    }

                } else if (cellType.equals("PROGRAM_POST")) {
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = true;
                    shouldShow_TitlePanel = false;
                    shouldShow_ContentPanel = false;

                    shouldShow_UserInteractionPanel = false;   //E  Optional
                }
            }
        }




        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        @Override
        public int getItemViewType(int position) {
            // if (isPositionHeader(position)) {
            if (isPositionHeader(position)) {
                return DATES;
            } else {
                return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
            }
            //  return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */

        /**
         * @param result
         * @return [releasedate] | [2letterlangcode]
         */
        private String formatYearLabel(Post result) {
//        return result.getReleaseDate().substring(0, 4)  // we want the year only
//                + " | "
//                + result.getOriginalLanguage().toUpperCase();

            return "00000000";
        }




        /*
            Helpers - Pagination
       _________________________________________________________________________________________________
        */
        public void addToTop(Post r) {
            movieResults.add(1, r);
            //  movieResults.add(r);
            notifyItemInserted(1);
        }

        public void add(Post r) {
            movieResults.add(r);
            notifyItemInserted(movieResults.size() - 1);
        }

        public void addAll(List<Post> moveResults) {
            for (Post result : moveResults) {
                add(result);
            }
        }

        public void remove(Post r) {
            int position = movieResults.indexOf(r);
            if (position > -1) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            isLoadingAdded = false;
            while (getItemCount() > 0) {
                remove(getItem(0));
            }
        }

        public boolean isEmpty() {
            return getItemCount() == 0;
        }


        public void addLoadingFooter() {
            isLoadingAdded = true;
            add(new Post());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieResults.size() - 1;
            Post result = getItem(position);

            if (result != null) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public Post getItem(int position) {
            return movieResults.get(position);
        }

        /**
         * Displays Pagination retry footer view along with appropriate errorMsg
         *
         * @param show
         * @param errorMsg to display if page load fails
         */



   /*
   View Holders
   _________________________________________________________________________________________________
    */

        /**
         * Header ViewHolder
         */
        protected class ServiceMenuView extends RecyclerView.ViewHolder {

            TextView txt_desc1;
            TextView txt_desc2;
            TextView btn_action;
            TextView txt_whats_on_mind;
            ImageView profilePicture;
         //   LinearLayout btn_action_lay;
            ImageView main_services_image_icon;
            LinearLayout layout_main_menu_horizontal, layout_services_menu_horizontal;

            public ServiceMenuView(View itemView) {
                super(itemView);
                layout_main_menu_horizontal = (LinearLayout) itemView.findViewById(R.id.layout_main_menu_horizontal);
                layout_services_menu_horizontal = (LinearLayout) itemView.findViewById(R.id.layout_main_menu_horizontal_footer);

                txt_desc1 = (TextView) itemView.findViewById(R.id.txt_desc1);
                txt_desc2 = (TextView) itemView.findViewById(R.id.txt_desc2);
                btn_action = (TextView) itemView.findViewById(R.id.btn_action);
              //  btn_action_lay = (LinearLayout) itemView.findViewById(R.id.btn_action_lay);
                main_services_image_icon = (ImageView) itemView.findViewById(R.id.remote_image_icon);

                txt_whats_on_mind = (TextView) itemView.findViewById(R.id.lbl_whatsonyourmind);
                profilePicture = (ImageView) itemView.findViewById(R.id.img_timeline_post_click);

            }
        }

        /**
         * Main list's content ViewHolder
         */
        protected class PostCell extends RecyclerView.ViewHolder {
            private TextView mMovieTitle;
            private TextView mMovieDesc;
            private TextView mYear; // displays "year | language"
            private ImageView mPosterImg;
            private ProgressBar mProgress;

            LinearLayout img_btn_close_bglayout, user_reaction_panel_liked_user_list_layout, user_panel, title_panel, content_panel, user_link_panel, user_reaction_panel;
            ImageView user_reaction_panel_like_clicked_image, user_reaction_panel_liked_user_list_icon, img_user_picture, txt_panel_b_background_image, txt_panel_b_play_button;
            TextView txt_user_name, txt_time_ago, user_reaction_panel_like_text;
            ImageButton img_btn_close_intop, link_panel_timeline_readme;
            ConstraintLayout media_panel;
            Button txt_panel_b_view_button;
            TextView program_panel_text_heading, link_panel_text_url, link_panel_text_desc;
            TextView program_panel_text_normal, title_panel_heading_text, text_panel_text_message;
            TextView user_reaction_panel_liked_user_list, user_reaction_panel_like_count, user_reaction_panel_comment_count, user_reaction_panel_comment_clicked;
            TextView user_reaction_panel_comment_second_text;
            LinearLayout main_cell_view;

            public PostCell(View convertView) {
                super(convertView);
                main_cell_view = (LinearLayout) convertView.findViewById(R.id.main_cell_view);
                //  TYPE TIMELINE
                user_panel = (LinearLayout) convertView.findViewById(R.id.user_panel);
                title_panel = (LinearLayout) convertView.findViewById(R.id.title_panel);
                content_panel = (LinearLayout) convertView.findViewById(R.id.content_panel);
                media_panel = (ConstraintLayout) convertView.findViewById(R.id.media_panel);
                user_link_panel = (LinearLayout) convertView.findViewById(R.id.user_link_panel);
                user_reaction_panel = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel);
                user_reaction_panel_liked_user_list_layout = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel_liked_user_list_layout);

                // panel G
                txt_panel_b_view_button = (Button) convertView.findViewById(R.id.txt_panel_b_view_button);
                program_panel_text_heading = (TextView) convertView.findViewById(R.id.program_panel_text_heading);
                program_panel_text_normal = (TextView) convertView.findViewById(R.id.txt_panel_b_text_day);

                // button Play
                txt_panel_b_play_button = (ImageView) convertView.findViewById(R.id.txt_panel_b_play_button);

                user_reaction_panel_like_text = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_text);
                user_reaction_panel_like_clicked_image = (ImageView) convertView.findViewById(R.id.user_reaction_panel_like_clicked_image);
                user_reaction_panel_comment_second_text = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_second_text);

                txt_panel_b_background_image = (ImageView) convertView.findViewById(R.id.imageView2);

                // user_panel
                img_user_picture = (ImageView) convertView.findViewById(R.id.img_user_picture);
                txt_user_name = (TextView) convertView.findViewById(R.id.txt_user_name);
                txt_time_ago = (TextView) convertView.findViewById(R.id.txt_time_ago);
                img_btn_close_intop = (ImageButton) convertView.findViewById(R.id.img_btn_close_intop);
                img_btn_close_bglayout = (LinearLayout) convertView.findViewById(R.id.img_btn_close_bglayout);

                link_panel_timeline_readme = (ImageButton) convertView.findViewById(R.id.link_panel_timeline_readme);

                link_panel_text_url = (TextView) convertView.findViewById(R.id.link_panel_text_url);
                link_panel_text_desc = (TextView) convertView.findViewById(R.id.link_panel_text_desc);

                // viewHolder.user_reaction_panel_like_button = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_clicked);
                user_reaction_panel_like_count = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_count);
                user_reaction_panel_comment_count = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_count);
                user_reaction_panel_comment_clicked = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_clicked);
                user_reaction_panel_liked_user_list = (TextView) convertView.findViewById(R.id.user_reaction_panel_liked_user_list);
                user_reaction_panel_liked_user_list_icon = (ImageView) convertView.findViewById(R.id.user_reaction_panel_liked_user_list_icon);
                // title_panel
                title_panel_heading_text = (TextView) convertView.findViewById(R.id.title_panel_heading_text);

                // text_panel
                text_panel_text_message = (TextView) convertView.findViewById(R.id.text_panel_text_message);

            }
        }


        protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ProgressBar loadmore_progress;
            private ImageButton mRetryBtn;
            private TextView mErrorTxt;
            private LinearLayout mErrorLayout;

            public LoadingVH(View itemView) {
                super(itemView);

                loadmore_progress = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
                mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
                mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
                mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

                mRetryBtn.setOnClickListener(this);
                mErrorLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.loadmore_retry:
                    case R.id.loadmore_errorlayout:


                        mCallback.retryPageLoad();

                        break;
                }
            }
        }

    }

}
