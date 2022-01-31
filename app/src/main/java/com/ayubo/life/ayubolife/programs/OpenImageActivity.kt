package com.ayubo.life.ayubolife.programs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.ayubo.life.ayubolife.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_appointment.*

class OpenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_image)

        val imagePath = intent.getStringExtra("image")

        imagePreview.scaleType = ImageView.ScaleType.FIT_CENTER
        Glide.with(this).load(imagePath).into(imagePreview)

    }
}
