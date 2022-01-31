package com.ayubo.life.ayubolife.lifeplus;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeZone {

    public Long utcToLocal(Date utcDate) {
        java.util.TimeZone tz = java.util.TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        return utcDate.getTime() + offsetInMillis;
    }
}
