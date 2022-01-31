package com.ayubo.life.ayubolife.post.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Sabri on 10/19/2018.
 */

public class AppHandler {

    public static int getWidth(Context mContext) {
        return getScreenPoint(mContext).x;
    }

    public static int getHeight(Context mContext) {
        return getScreenPoint(mContext).y;
    }

    private static Point getScreenPoint(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display;
        Point size = new Point();
        if (wm != null) {
            display = wm.getDefaultDisplay();
            display.getSize(size);
        }
        return size;
    }
}
