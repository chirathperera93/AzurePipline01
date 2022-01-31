package com.ayubo.life.ayubolife.map_challenges;

import android.view.View;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by appdev on 1/22/2018.
 */

interface MarkerAdapter {
    View getInfoContents(Marker mark);

    View getInfoWindow(Marker marker);
}
