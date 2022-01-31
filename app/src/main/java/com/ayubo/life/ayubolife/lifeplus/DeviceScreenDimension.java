package com.ayubo.life.ayubolife.lifeplus;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Chirath Perera on 2021-10-05.
 */
public class DeviceScreenDimension {

    Integer displayWidth;
    Integer displayHeight;

    public DeviceScreenDimension(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayWidth = size.x;
        displayHeight = size.y;
    }

    public Integer getDisplayWidth() {
        return displayWidth;
    }

    public Integer getDisplayHeight() {
        return displayHeight;
    }
}
