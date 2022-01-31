package com.ayubo.life.ayubolife.satalite_menu;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.insurances.GenericItems.UploadDocumentItem;

import java.util.ArrayList;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.UploadImageViewHolder> {

    private ArrayList<Bitmap> uploadDocumentList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class UploadImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public ImageView mDeleteImage;

        public UploadImageViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.upload_document_item_image_view);
            mDeleteImage = itemView.findViewById(R.id.upload_document_item_delete_image);


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
        }
    }

    public UploadImageAdapter(ArrayList<Bitmap> mUploadDocumentList) {
        uploadDocumentList = mUploadDocumentList;
    }

    @NonNull
    @Override
    public UploadImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_document_item, parent, false);
        UploadImageViewHolder evh = new UploadImageViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull UploadImageViewHolder holder, int position) {
        holder.mImageView.setImageBitmap(uploadDocumentList.get(position));
    }

    @Override
    public int getItemCount() {
        return uploadDocumentList.size();
    }


}
