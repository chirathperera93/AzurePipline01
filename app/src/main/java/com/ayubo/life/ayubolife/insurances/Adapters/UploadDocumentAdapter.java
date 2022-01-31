package com.ayubo.life.ayubolife.insurances.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.insurances.GenericItems.UploadDocumentItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UploadDocumentAdapter extends RecyclerView.Adapter<UploadDocumentAdapter.ExampleViewHolder> {

    private ArrayList<UploadDocumentItem> uploadDocumentList;
    private OnItemClickListener mListener;
    private Context ctx;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout mainRelativeForImage;
        public RelativeLayout mainRelativeForText;
        public ImageView mImageView;
        public ImageView mDeleteImage;
        public ImageView mDeleteText;
        public TextView mTextArea;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mainRelativeForImage = itemView.findViewById(R.id.mainRelativeForImage);
            mainRelativeForText = itemView.findViewById(R.id.mainRelativeForText);
            mTextArea = itemView.findViewById(R.id.textViewForTextArea);
            mImageView = itemView.findViewById(R.id.upload_document_item_image_view);
            mDeleteImage = itemView.findViewById(R.id.upload_document_item_delete_image);
            mDeleteText = itemView.findViewById(R.id.upload_text_delete_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            mDeleteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public UploadDocumentAdapter(ArrayList<UploadDocumentItem> mUploadDocumentList, Context context) {
        uploadDocumentList = mUploadDocumentList;
        ctx = context;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_document_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        UploadDocumentItem currentItem = uploadDocumentList.get(position);


        String[] stringArr = currentItem.getImgUrl().split("/");

        String[] fileType = stringArr[stringArr.length - 1].split("\\.");


        if (currentItem.getMediaType().equals("application/pdf")) {
            holder.mImageView.setBackgroundResource(R.drawable.ic_pdf);
        } else if (currentItem.getMediaType().equals("text/plain")) {
            holder.mainRelativeForImage.setVisibility(View.GONE);
            holder.mainRelativeForText.setVisibility(View.VISIBLE);
            holder.mTextArea.setText(currentItem.getMediaNote());
            System.out.println(currentItem);


        } else {
            Glide.with(ctx).load(currentItem.getImgUrl()).into(holder.mImageView);
        }

//        if (fileType[fileType.length - 1].equals("pdf")) {
//            holder.mImageView.setBackgroundResource(R.drawable.ic_pdf);
//        } else {
//            Glide.with(ctx).load(currentItem.getImgUrl()).into(holder.mImageView);
//        }


//        holder.mImageView.setImageBitmap(currentItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return uploadDocumentList.size();
    }

}
