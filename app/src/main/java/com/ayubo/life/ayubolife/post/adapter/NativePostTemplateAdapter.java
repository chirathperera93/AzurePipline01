package com.ayubo.life.ayubolife.post.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.model.NativeButtonObj;
import com.ayubo.life.ayubolife.post.model.Template;
import com.ayubo.life.ayubolife.post.util.AppHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class NativePostTemplateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_HEADING = 1;
    private static final int VIEW_SUB_HEADING = 2;
    private static final int VIEW_TITLE = 3;
    private static final int VIEW_DESCRIPTION = 4;
    private static final int VIEW_BODY = 5;
    private static final int VIEW_FULL_IMAGE = 6;
    private static final int VIEW_NUMBERED = 7;
    private static final int VIEW_BULLET = 8;
    private static final int VIEW_VIDEO = 9;
    private static final int VIEW_IMAGE = 10;
    private static final int VIEW_GIF = 11;
    private static final int VIEW_PRICE = 12;
    private static final int VIEW_TERMS = 13;
    private static final int VIEW_BUTTON = 14;
    private static final int VIEW_TABLE = 15;
    private static final int VIEW_AUTHOR = 16;
    private static final int VIEW_TERMS_CONDITION = 17;
    private static final int VIEW_LISTVIEW_ITEM = 18;

    //instances
    private Context context;
    private List<Template> templates;
    private RequestManager requestManager;

    //primary data
    private int width;

    public NativePostTemplateAdapter(Context context, List<Template> templates, int w) {
        this.context = context;
        this.templates = templates;
        this.requestManager = Glide.with(context);
        width = w;
    }

    public interface OnItemClickListener {
        void onGoalClick(String meta);
    }

    OnActionButtonInterface onactionButtonListener = null;
    OnProcessActionInterface onProcessActionListener = null;

    //  private NativePostTemplateAdapter.OnItemClickListener backClickListner;

    public void OnACBBack(OnActionButtonInterface listener) {
        this.onactionButtonListener = listener;
    }

    public interface OnActionButtonInterface {
        void onActionButton(NativeButtonObj meta);
    }

    public void OnProcessAction(OnProcessActionInterface listener) {
        this.onProcessActionListener = listener;
    }

    public interface OnProcessActionInterface {
        void onProcessActionButton(String action, String meta);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case VIEW_HEADING:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_heading_row, null);
                viewHolder = new TextViewHolder(v);
                break;
            case VIEW_SUB_HEADING:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_subheading_new, null);
                viewHolder = new TextViewHolder(v);
                break;
            case VIEW_TITLE:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_title_row, null);
                viewHolder = new TextViewHolder(v);
                break;
            case VIEW_DESCRIPTION:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_description_row, null);
                viewHolder = new TextViewHolder(v);
                break;
            case VIEW_BODY:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_normal_text_row, null);
                viewHolder = new TextViewHolder(v);
                break;
            case VIEW_BULLET:
            case VIEW_NUMBERED:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_bullet_text_row, null);
                viewHolder = new TextViewHolder(v);
                break;
            case VIEW_PRICE:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_price_row, null);
                viewHolder = new TextViewHolder(v);
                break;
            case VIEW_TERMS:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_terms_row, null);
                viewHolder = new TextViewHolder(v);
                break;
            case VIEW_TERMS_CONDITION:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_termsncondition_row, null);
                viewHolder = new TermsConditionViewHolder(v);
                break;
            case VIEW_TABLE:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_table_row, null);
                viewHolder = new TableViewHolder(v);
                break;
            case VIEW_FULL_IMAGE:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_media_row, null);
                viewHolder = new ImageViewHolder(v);
                break;
            case VIEW_IMAGE:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_media_row, null);
                viewHolder = new ImageViewHolder(v);
                break;
            case VIEW_GIF:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_media_row, null);
                viewHolder = new ImageViewHolder(v);
                break;
            case VIEW_VIDEO:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_media_row, null);
                viewHolder = new ImageViewHolder(v);
                break;
            case VIEW_BUTTON:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_button_row, null);
                viewHolder = new ButtonViewHolder(v);
                break;
            case VIEW_AUTHOR:
                v = LayoutInflater.from(context).inflate(R.layout.authors_post_row_nativepost, null);
                viewHolder = new AuthorsHolder(v);
                break;
            case VIEW_LISTVIEW_ITEM:
                v = LayoutInflater.from(context).inflate(R.layout.layout_post_listitem, null);
                viewHolder = new ListItemHolder(v);
                break;
        }

        return viewHolder;

//        return new TextViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_post_heading_row, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof TextViewHolder) {
            TextViewHolder textHolder = (TextViewHolder) holder;

            if (templates.get(position) instanceof Template.TextTemplate) {
                Template.TextTemplate data = (Template.TextTemplate) templates.get(position);
                textHolder.textView.setText(data.getText());

            } else if (templates.get(position) instanceof Template.PriceTemplate) {
                Template.PriceTemplate data = (Template.PriceTemplate) templates.get(position);
                textHolder.textView.setText(String.format("%s %s", data.getCurrency(), data.getAmount()));
            } else if (templates.get(position) instanceof Template.TermTemplate) {
                final Template.TermTemplate data = (Template.TermTemplate) templates.get(position);
                textHolder.textView.setText(R.string.terms_conditions);

                textHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getLink())));
                    }
                });
            } else if (templates.get(position) instanceof Template.AuthorTemplate) {

                AuthorsHolder authorViewHolder = (AuthorsHolder) holder;
                //  final Template.AuthorData authorTemplat = (Template.AuthorData) templates.get(position);
                Template.AuthorTemplate authData = (Template.AuthorTemplate) templates.get(position);
                DisplayMetrics displayMetrics = new DisplayMetrics();

                RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);

                authorViewHolder.txt_author_name.setText(authData.getList().getMain_label());
                authorViewHolder.txt_author_title.setText(authData.getList().getSub_label());
                Glide.with(context).load(authData.getList().getImage())
                        .apply(requestOptionsSamll)
                        .into(authorViewHolder.img_author_image);
            } else {
                Template.ListTemplate data = (Template.ListTemplate) templates.get(position);
                SpannableStringBuilder result = new SpannableStringBuilder();

                int pos = 0;
                for (String line : data.getList()) {
                    if (!result.toString().equals(""))
                        result.append("\n");
                    if (getItemViewType(position) == VIEW_NUMBERED)
                        result.append(String.valueOf(++pos)).append(". ");
                    result.append(line);
                }

                if (getItemViewType(position) == VIEW_BULLET) {
                    String[] points = result.toString().split("\n");
                    for (String item : points) {
                        int start = result.toString().indexOf(item);
                        result.setSpan(new BulletSpan(50, context.getResources().getColor(R.color.black)), start, start + item.length(), 0);
                    }
                }
                textHolder.textView.setText(result);
            }
        }

        if (templates.get(position) instanceof Template.ListItemData) {

            ListItemHolder textHolder = (ListItemHolder) holder;

            Template.ListItemData data = (Template.ListItemData) templates.get(position);


            int buttonWidth = width - 5;
            textHolder.card_main.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textHolder.txt_heading.setText(data.getHeader());
            textHolder.txt_description.setText(data.getSub_heading());
            textHolder.sub_heading.setText(data.getStatus());
            Glide.with(context).load(data.getImage())
                    .into(textHolder.img_image);

//            int strokeColor = Color.parseColor("#53db7f");
//            int fillColor = Color.parseColor("#53db7f");
//          //  textHolder.sub_heading.setTextColor(Color.parseColor("#ffffff"));
//            int strokeWidth = 2;
//            GradientDrawable gD = new GradientDrawable();
//            gD.setColor(fillColor);
//            gD.setShape(GradientDrawable.OVAL);
//            gD.setStroke(strokeWidth, strokeColor);
//            textHolder.sub_heading.setBackground(gD);


            textHolder.card_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onProcessActionListener.onProcessActionButton("", "'");
                }
            });
        } else if (templates.get(position) instanceof Template.TermTemplate) {
            TermsViewHolder textHolder = (TermsViewHolder) holder;
            final Template.TermTemplate data = (Template.TermTemplate) templates.get(position);
            textHolder.textView.setText(R.string.terms_conditions);

            textHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getLink())));
                }
            });
        } else if (templates.get(position) instanceof Template.TermAndConditionTemplate) {
            final TermsConditionViewHolder termsConditionViewHolder = (TermsConditionViewHolder) holder;
            final Template.TermAndConditionTemplate data = (Template.TermAndConditionTemplate) templates.get(position);
            termsConditionViewHolder.txt_terms_text.setText(R.string.terms_conditions);


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            termsConditionViewHolder.txt_terms_text.setLayoutParams(layoutParams);

            termsConditionViewHolder.layout_post_row_buttons_termsncon.removeAllViews();


            termsConditionViewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    termsConditionViewHolder.layout_post_row_buttons_termsncon.removeAllViews();


                    if (termsConditionViewHolder.radioButton.isChecked()) {
                        termsConditionViewHolder.layout_post_row_buttons_termsncon.removeAllViews();
                        for (Template.ButtonData btnData : data.getList()) {
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                            TextView tv = new TextView(context);
                            tv.setLayoutParams(new LinearLayout.LayoutParams(10, 10));

                            Button btn = new Button(context);
                            btn.setText(btnData.getLabel());
                            btn.setEnabled(true);
                            int buttonWidth = width - 80;
                            btn.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rounded_coners_with_black_bg_nopadding));
                            } else {
                                btn.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_coners_with_black_bg_nopadding));
                            }
                            // btn.setBackgroundColor(termsConditionViewHolder.radioButton.getContext().getResources().getColor(R.color.theme_color));

                            btn.setEnabled(true);
                            btn.setTag(btnData);
                            btn.setLayoutParams(param);

                            btn.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Template.ButtonData btnData = (Template.ButtonData) view.getTag();
                                    NativeButtonObj obj = new NativeButtonObj(btnData.getLabel(), btnData.getAction(), btnData.getType(), btnData.getStatus(), btnData.getMeta());
                                    onactionButtonListener.onActionButton(obj);
                                }
                            });
                            termsConditionViewHolder.layout_post_row_buttons_termsncon.addView(btn);
                            termsConditionViewHolder.layout_post_row_buttons_termsncon.addView(tv);
                        }
                    } else {
                        termsConditionViewHolder.layout_post_row_buttons_termsncon.removeAllViews();
                        for (Template.ButtonData btnData : data.getList()) {
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                            //  LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tv.getLayoutParams();
                            //  param.setMargins(0, 0, 0, 20);
                            TextView tv = new TextView(context);
                            tv.setLayoutParams(new LinearLayout.LayoutParams(10, 10));

                            Button btn = new Button(context);
                            btn.setText(btnData.getLabel());
                            int buttonWidth = width - 80;
                            btn.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            // btn.setBackgroundColor(termsConditionViewHolder.radioButton.getContext().getResources().getColor(R.color.stephistory_text_color));
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_balckwhite_button_bg_inactive));
                            } else {
                                btn.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_balckwhite_button_bg_inactive));
                            }
                            btn.setEnabled(true);
                            btn.setTag(btnData);
                            btn.setLayoutParams(param);
                            btn.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                            termsConditionViewHolder.layout_post_row_buttons_termsncon.addView(btn);
                            termsConditionViewHolder.layout_post_row_buttons_termsncon.addView(tv);
                        }
                    }

                }
            });

            termsConditionViewHolder.txt_terms_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getTerms_meta())));
                }
            });


            for (Template.ButtonData btnData : data.getList()) {

                TextView tv = new TextView(context);
                tv.setLayoutParams(new LinearLayout.LayoutParams(10, 10));


                Button btn = new Button(context);
                btn.setText(btnData.getLabel());
                btn.setEnabled(false);
                btn.setTag(btnData);

                int buttonWidth = width - 80;
                btn.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,
                        LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL));

                final int sdk = android.os.Build.VERSION.SDK_INT;

                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_balckwhite_button_bg));
                } else {
                    btn.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_balckwhite_button_bg));
                }


                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Template.ButtonData btnData = (Template.ButtonData) view.getTag();
                        NativeButtonObj obj = new NativeButtonObj(btnData.getLabel(), btnData.getAction(), btnData.getType(), btnData.getStatus(), btnData.getMeta());
                        onactionButtonListener.onActionButton(obj);
                    }
                });

                if (termsConditionViewHolder.radioButton.isChecked()) {
                    final int sd = android.os.Build.VERSION.SDK_INT;
                    if (sd < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_balckwhite_button_bg));
                    } else {
                        btn.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_balckwhite_button_bg));
                    }

                }

                termsConditionViewHolder.layout_post_row_buttons_termsncon.addView(btn);
                termsConditionViewHolder.layout_post_row_buttons_termsncon.addView(tv);
            }
        } else if (holder instanceof TableViewHolder) {
            TableViewHolder tableHolder = (TableViewHolder) holder;

            Template.TableTemplate tableData = (Template.TableTemplate) templates.get(position);

            int www = 0;

            if (tableData.getHead().size() == 3) {
                www = width / 3;
            } else if (tableData.getHead().size() == 2) {
                www = width / 2;
            } else {
                www = width;
            }

            tableHolder.tableView.removeAllViews();

            TableRow tableHeadRow = new TableRow(context);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(www);
            tableHeadRow.setLayoutParams(lp);


            for (String head : tableData.getHead()) {
                View v = LayoutInflater.from(context).inflate(R.layout.component_post_table_head_item, null);
                TextView txtView = v.findViewById(R.id.txt_post_table_head_item);
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(www, LinearLayout.LayoutParams.MATCH_PARENT);
                txtView.setLayoutParams(ll);


                txtView.setText(head);
                tableHeadRow.addView(v);
            }
            tableHolder.tableView.addView(tableHeadRow, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.
                    WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            for (List<String> row : tableData.getBody()) {
                TableRow tableRow = new TableRow(context);

                tableRow.setBackgroundResource(R.drawable.row_borders);

                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(www, LinearLayout.LayoutParams.MATCH_PARENT);
                tableRow.setLayoutParams(lp1);

                Integer count = 0;
                for (String item : row) {
                    count = count + 1;
                    View v = LayoutInflater.from(context).inflate(R.layout.component_post_table_data_item, null);

                    TextView txtView = v.findViewById(R.id.txt_post_table_data_item);

                    LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(www, LinearLayout.LayoutParams.MATCH_PARENT);


                    txtView.setLayoutParams(ll);


                    if (count == (row.size())) {
                        txtView.setEms(20);
                    }


                    txtView.setText(item);
                    tableRow.addView(v);
                }
                tableHolder.tableView.addView(tableRow, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.
                        WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        } else if (holder instanceof AuthorsHolder) {

            AuthorsHolder authorViewHolder = (AuthorsHolder) holder;
            Template.AuthorTemplate authData = (Template.AuthorTemplate) templates.get(position);

            RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);

            authorViewHolder.txt_author_name.setText(authData.getList().getMain_label());
            authorViewHolder.txt_author_title.setText(authData.getList().getSub_label());
            Glide.with(context).load(authData.getList().getImage())
                    .apply(requestOptionsSamll)
                    .into(authorViewHolder.img_author_image);


        } else if (holder instanceof ImageViewHolder) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            final Template.MediaTemplate mediaTemplate = (Template.MediaTemplate) templates.get(position);
            imageViewHolder.webView.setVisibility(View.GONE);
            final WebView webView = imageViewHolder.webView;


            DisplayMetrics displayMetrics = new DisplayMetrics();
            DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(context);
            int w = deviceScreenDimension.getDisplayWidth();
            int h = (width / 4) * 3;


            imageViewHolder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            RequestOptions requestOptionsSamll = new RequestOptions().override(w, h).diskCacheStrategy(DiskCacheStrategy.NONE);


            String imgLink = mediaTemplate.getLink();
            if (!mediaTemplate.getType().equals("video-full")) {

                //  RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(context).load(imgLink)
                        .apply(requestOptionsSamll)
                        .into(imageViewHolder.imageView);

                // requestManager.load(imgLink).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageViewHolder.imageView);
                imageViewHolder.imagePlay.setVisibility(View.GONE);
                imageViewHolder.imageView.setOnClickListener(null);

            } else {
                // RequestOptions requestOptionsSamll = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(context).load(mediaTemplate.getThumb())
                        .apply(requestOptionsSamll)
                        .into(imageViewHolder.imageView);

                imageViewHolder.imagePlay.setVisibility(View.VISIBLE);

                imageViewHolder.imagePlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        webView.getSettings().setLoadsImagesAutomatically(true);
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                        webView.loadUrl(mediaTemplate.getLink());
                        webView.setVisibility(View.VISIBLE);
                    }
                });

                imageViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        webView.getSettings().setLoadsImagesAutomatically(true);
//                        webView.getSettings().setJavaScriptEnabled(true);
//                        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//                        webView.loadUrl(mediaTemplate.getLink());
//                        webView.setVisibility(View.VISIBLE);

                    }
                });
            }

            int height = (int) (width * 1.0);
            RelativeLayout.LayoutParams layoutParams;
            if (!mediaTemplate.getType().equals("image-full")) {
                height = width * 2 / 3;
                layoutParams = new RelativeLayout.LayoutParams(width, height);
                layoutParams.setMargins(40, 0, 40, 20);
            } else {
                layoutParams = new RelativeLayout.LayoutParams(width, height);
                layoutParams.setMargins(0, 0, 0, 20);
            }
            imageViewHolder.imageView.setLayoutParams(layoutParams);
            //webView.setLayoutParams(layoutParams);

        } else if (holder instanceof ButtonViewHolder) {
            ButtonViewHolder btnViewHolder = (ButtonViewHolder) holder;
            Template.ButtonTemplate btnTemplate = (Template.ButtonTemplate) templates.get(position);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );

            btnViewHolder.parentView.removeAllViews();
            for (Template.ButtonData btnData : btnTemplate.getList()) {

                TextView tv = new TextView(context);
                tv.setLayoutParams(new LinearLayout.LayoutParams(10, 10));
                Button btn = new Button(context);
                btn.setText(btnData.getLabel());
                btn.setEnabled(btnData.getStatus().equals("enabled"));
                btn.setTag(btnData);
                btn.setLayoutParams(param);

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rounded_coners_with_black_bg_nopadding));
                } else {
                    btn.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_coners_with_black_bg_nopadding));
                }

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Template.ButtonData btnData = (Template.ButtonData) view.getTag();
                        NativeButtonObj obj = new NativeButtonObj(btnData.getLabel(), btnData.getAction(), btnData.getType(), btnData.getStatus(), btnData.getMeta());
                        onactionButtonListener.onActionButton(obj);
                    }
                });

                btnViewHolder.parentView.addView(btn);
                btnViewHolder.parentView.addView(tv);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (templates == null)
            return 0;
        else
            return templates.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (templates != null) {
            switch (templates.get(position).getType()) {
                case "heading":
                    return VIEW_HEADING;
                case "listed_tiles":
                    return VIEW_LISTVIEW_ITEM;
                case "terms":
                    return VIEW_TERMS;
                case "sub-heading":
                    return VIEW_SUB_HEADING;
                case "paragraph":
                    return VIEW_BODY;
                case "bulleted-list":
                    return VIEW_BULLET;
                case "numbered-list":
                    return VIEW_NUMBERED;
                case "price":
                    return VIEW_PRICE;
                case "image-sub":
                    return VIEW_IMAGE;
                case "video-sub":
                    return VIEW_VIDEO;
                case "image-full":
                    return VIEW_FULL_IMAGE;
                case "video-full":
                    return VIEW_VIDEO;
                case "gif":
                    return VIEW_GIF;
                case "table":
                    return VIEW_TABLE;
                case "termsnconditions":
                    return VIEW_TERMS_CONDITION;
                case "button":
                    return VIEW_BUTTON;
                case "authors":
                    return VIEW_AUTHOR;
            }
        }
        return super.getItemViewType(position);
    }


    class TermsConditionViewHolder extends RecyclerView.ViewHolder {

        TextView txt_terms_text;
        CheckBox radioButton;
        LinearLayout layout_post_row_buttons_termsncon;

        // Button terms_button1,terms_button2;
        TermsConditionViewHolder(View itemView) {
            super(itemView);
            txt_terms_text = itemView.findViewById(R.id.txt_terms_text);
            radioButton = itemView.findViewById(R.id.radio_post_row_radio);
//            terms_button1 = itemView.findViewById(R.id.terms_button1);
//            terms_button2 = itemView.findViewById(R.id.terms_button2);
            layout_post_row_buttons_termsncon = itemView.findViewById(R.id.layout_post_row_buttons_termsncon);

        }
    }

    class ListItemHolder extends RecyclerView.ViewHolder {
        LinearLayout card_main;
        TextView txt_heading, txt_description, sub_heading;
        ImageView img_image;

        ListItemHolder(View itemView) {
            super(itemView);
            card_main = itemView.findViewById(R.id.card_main);
            img_image = itemView.findViewById(R.id.img_image);
            txt_heading = itemView.findViewById(R.id.txt_heading);
            txt_description = itemView.findViewById(R.id.txt_description);
            sub_heading = itemView.findViewById(R.id.sub_heading);

        }
    }

    class AuthorsHolder extends RecyclerView.ViewHolder {

        TextView txt_author_name, txt_author_title;
        ImageView img_author_image;

        AuthorsHolder(View itemView) {
            super(itemView);
            img_author_image = itemView.findViewById(R.id.img_author_image);
            txt_author_name = itemView.findViewById(R.id.txt_author_name);
            txt_author_title = itemView.findViewById(R.id.txt_author_title);

        }
    }

    class TermsViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox radioButton;

        TermsViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_post_row_text);
            radioButton = itemView.findViewById(R.id.radio_post_row_radio);
        }
    }

    class TextViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox radioButton;

        TextViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_post_row_text);
            radioButton = itemView.findViewById(R.id.radio_post_row_radio);
        }
    }

    class TableViewHolder extends RecyclerView.ViewHolder {

        TableLayout tableView;

        TableViewHolder(View itemView) {
            super(itemView);
            tableView = itemView.findViewById(R.id.table_post_row_table);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, imagePlay;
        WebView webView;
        RelativeLayout main_image_view;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_post_row_image);
            imagePlay = itemView.findViewById(R.id.img_post_row_play);
            webView = itemView.findViewById(R.id.web_post_row_video);
            main_image_view = itemView.findViewById(R.id.main_image_view);
        }
    }

    class ButtonViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parentView;

        ButtonViewHolder(View itemView) {
            super(itemView);
            parentView = itemView.findViewById(R.id.layout_post_row_buttons);
        }
    }
}
