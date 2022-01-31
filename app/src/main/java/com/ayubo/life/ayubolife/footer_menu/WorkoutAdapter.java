package com.ayubo.life.ayubolife.footer_menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.body.WorkoutProgram;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class WorkoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_WORKOUT_SUMMERY = 0;
    public static final int TYPE_WORKOUT_TYPES = 1;
    public static final int TYPE_WORKOUT_ITEMS = 2;
    //instances
    private Context context;
    private List<WorkoutProgram> workoutPageDataList;
    PrefManager pref = null;
    private String errorMsg;
    // private com.ayubo.life.ayubolife.notification.adapter.NotificationsAdapter.OnClickNotificationCell listener;


    private OnSendStepsListener listener;
    private OnImageClickListener listenerImage;

    public interface OnImageClickListener {
        void onClickProcessAction(String action, String meta);
    }

    public interface OnSendStepsListener {
        void onClicksendSteps();
    }


    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listenerImage = listener;
    }

    public void setOnVideoSessionListener(OnSendStepsListener listener) {
        this.listener = listener;
    }


    public WorkoutAdapter(Context context, List<WorkoutProgram> reportsListt) {
        this.context = context;
        workoutPageDataList = new ArrayList<>();
        workoutPageDataList = reportsListt;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_WORKOUT_SUMMERY) {
            view = LayoutInflater.from(context).inflate(R.layout.workout_summery_list_raw_layout, parent, false);
            return new WorkoutViewHolder(view);
        }
        if (viewType == TYPE_WORKOUT_TYPES) {
            view = LayoutInflater.from(context).inflate(R.layout.workout_intro_raw_layout, parent, false);
            return new WorkoutTypesViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.workout_image_list_raw_layout, parent, false);
            return new ItemViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof WorkoutViewHolder) {

            WorkoutViewHolder workoutHolder = (WorkoutViewHolder) holder;

            workoutHolder.progressBar_Step_Circle.setProgress(0);
            workoutHolder.progressBar_Step_Circle.setMax(10000);

            pref = new PrefManager(context);
            String googlefitenabled = pref.isGoogleFitEnabled();
            String total_steps = pref.getUserData().get("steps");
            String total_energy = pref.getUserData().get("energy");
            String total_calories = pref.getUserData().get("calories");
            String total_distance = pref.getUserData().get("distance");
            String total_duration = pref.getUserData().get("duration");
            String deviceName = pref.getDeviceData().get("stepdevice");
            String deviceIcon = pref.getDeviceData().get("icon");

            int steps = Integer.parseInt(total_steps);
            String steps_with_comma = NumberFormat.getIntegerInstance().format(steps);
            workoutHolder.txt_total_steps.setText(steps_with_comma);

            double dSteps = (double) Integer.parseInt(total_steps);
            double d78 = (double) 78;
            double dLax = (double) 100000;
            double dDist = (dSteps * d78) / dLax;

            DecimalFormat twoDForm = new DecimalFormat("#.##");
            String numberAsString = twoDForm.format(dDist);

            workoutHolder.txt_mets.setText(total_energy);
            workoutHolder.txt_cals.setText(total_calories);
            workoutHolder.txt_dus.setText(total_duration);
            workoutHolder.txt_dis.setText(numberAsString);

            workoutHolder.progressBar_Step_Circle.setProgress(Integer.parseInt(total_steps));

            workoutHolder.layout_sysnc_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onClicksendSteps();
                }
            });

            workoutHolder.img_coonectgfit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onClicksendSteps();
                }
            });


            if (deviceName != null) {
                System.out.println("======deviceName====4444=====" + deviceName);
                if (deviceName.equals("activity_AYUBO")) {

                    Bitmap bitmap55 = BitmapFactory.decodeResource(context.getResources(), R.drawable.gfit_ic);
                    workoutHolder.img_coonectgfit_icon.setImageBitmap(bitmap55);

                    if (googlefitenabled.equals("false")) {
                        workoutHolder.img_coonectgfit.setText("Connect");

                    } else if (googlefitenabled.equals("true")) {
                        workoutHolder.img_coonectgfit.setText("Sync now");
                    }

                } else {
                    // System.out.println("======deviceIcon========="+deviceIcon);
                    workoutHolder.img_coonectgfit.setText(deviceName);

                    RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).transforms(new CircleTransform(context));
                    Glide.with(context).load(deviceIcon)
                            .transition(withCrossFade())
                            .thumbnail(0.5f)
                            .apply(requestOptions)
                            .into(workoutHolder.img_coonectgfit_icon);

                }

            }


            final int pos = holder.getAdapterPosition();

//            workoutHolder.main_layer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (listener != null)
//                        listener.onSelectNotificationCell((NotificationData) reportsList.get(pos));
//                }
//            });

        }
        if (holder instanceof WorkoutTypesViewHolder) {

        }
        if (holder instanceof ItemViewHolder) {

            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            final WorkoutProgram obj = workoutPageDataList.get(position);

            String imgUrl = ApiClient.BASE_URL + obj.getImage();
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter();
            Glide.with(context).load(imgUrl)
                    .apply(requestOptions)
                    .into(itemHolder.bg_image);
            itemHolder.bg_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listenerImage != null)
                        listenerImage.onClickProcessAction(obj.getAction(), obj.getMeta());
                }
            });


        }
    }


    @Override
    public int getItemCount() {
        int finalCount;
        finalCount = workoutPageDataList == null ? 0 : workoutPageDataList.size();
        return finalCount;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_WORKOUT_SUMMERY;
        }
        if (position == 1) {
            return TYPE_WORKOUT_TYPES;
        } else {
            return TYPE_WORKOUT_ITEMS;
        }

    }


    protected class WorkoutViewHolder extends RecyclerView.ViewHolder {

        Button img_coonectgfit;
        ImageView img_coonectgfit_icon;
        LinearLayout layout_sysnc_button;
        TextView txt_total_steps;
        TextView txt_mets;
        TextView txt_cals;
        TextView txt_dus;
        TextView txt_dis;
        ProgressBar progressBar_Step_Circle;

        public WorkoutViewHolder(View viewHolder) {
            super(viewHolder);

            img_coonectgfit = (Button) viewHolder.findViewById(R.id.img_coonectgfit);
            img_coonectgfit_icon = (ImageView) viewHolder.findViewById(R.id.img_coonectgfit_icon);
            layout_sysnc_button = (LinearLayout) viewHolder.findViewById(R.id.layout_sysnc_button);
            txt_total_steps = (TextView) viewHolder.findViewById(R.id.txt_total_steps);
            txt_mets = (TextView) viewHolder.findViewById(R.id.txt_mets);
            txt_cals = (TextView) viewHolder.findViewById(R.id.txt_cals);
            txt_dus = (TextView) viewHolder.findViewById(R.id.txt_dus);
            txt_dis = (TextView) viewHolder.findViewById(R.id.txt_dis);
            progressBar_Step_Circle = (ProgressBar) viewHolder.findViewById(R.id.progressBar_Step_Circle);

        }

    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView bg_image;

        public ItemViewHolder(View rowView) {
            super(rowView);

            bg_image = rowView.findViewById(R.id.workout_main_bg_banner);
        }
    }

    protected class WorkoutTypesViewHolder extends RecyclerView.ViewHolder {

        ImageView bg_image;

        public WorkoutTypesViewHolder(View rowView) {
            super(rowView);

            bg_image = rowView.findViewById(R.id.main_layer);
        }
    }


}

