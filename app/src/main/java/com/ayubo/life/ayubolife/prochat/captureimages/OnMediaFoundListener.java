package com.ayubo.life.ayubolife.prochat.captureimages;

import java.io.File;

/**
 * Created by Admin on 5/20/2016.
 */
public interface OnMediaFoundListener {
    void onPicFileFound(File mFile);
    void onVideoFileFound(File mFile);
    void onOtherFileFound(File mFile);
}
