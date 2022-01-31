package com.ayubo.life.ayubolife.insurances.GenericItems;

public class UploadDocumentItem {

//    private Bitmap mImageResource;

    private String mImgUrl;

    private String mMediaName;

    private String mMediaFolder;

    private String mMediaType;

    private String mMediaNote;


    public UploadDocumentItem(String mImgUrl, String mMediaName, String mMediaFolder, String mMediaType, String mMediaNote) {
//        this.mImageResource = mImageResource;
        this.mImgUrl = mImgUrl;
        this.mMediaName = mMediaName;
        this.mMediaFolder = mMediaFolder;
        this.mMediaType = mMediaType;
        this.mMediaNote = mMediaNote;
    }

//    public Bitmap getImageResource() {
//        return mImageResource;
//    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public String getMediaName() {
        return mMediaName;
    }

    public String getMediaFolder() {
        return mMediaFolder;
    }

    public String getMediaType() {
        return mMediaType;
    }

    public String getMediaNote() {
        return mMediaNote;
    }
}
