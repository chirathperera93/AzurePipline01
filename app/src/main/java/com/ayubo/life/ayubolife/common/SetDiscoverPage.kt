package com.ayubo.life.ayubolife.common

import android.content.Context
import android.content.Intent
import com.ayubo.life.ayubolife.lifeplus.LifePlusProgramActivity
import com.ayubo.life.ayubolife.lifeplus.NewDiscoverActivity
import com.flavors.changes.Constants

/**
 * Created by Chirath Perera on 2021-07-09.
 */
class SetDiscoverPage {


    fun getDiscoverIntent(baseContext: Context): Intent {
        val intent: Intent;
//        if (Constants.type == Constants.Type.LIFEPLUS || Constants.type == Constants.Type.AYUBO) {
        if (Constants.type == Constants.Type.LIFEPLUS) {
            intent = Intent(baseContext, NewDiscoverActivity::class.java)
        } else {
            intent = Intent(baseContext, LifePlusProgramActivity::class.java)
        }

        return intent;
    }


}