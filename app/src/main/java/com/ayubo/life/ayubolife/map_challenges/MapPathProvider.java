package com.ayubo.life.ayubolife.map_challenges;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class MapPathProvider {

    String challenge_id;

    MapPathProvider(String ch_id)
    {
        challenge_id=ch_id;
    }
    public String loadJSONFromAssetNew(Context context) {
        String json = null;
        try {
            InputStream is = context.openFileInput(challenge_id + ".json");
            int size = is.available();
            if (size > 0) {
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.d("-----MAP--JSON--------",json.toString());
        return json;
    }
}
