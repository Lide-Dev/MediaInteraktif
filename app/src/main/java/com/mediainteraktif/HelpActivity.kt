package com.mediainteraktif

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        try {
            Glide.with(this)
                .load(R.drawable.group_1)
                .into(help_img_1)

            Glide.with(this)
                .load(R.drawable.group_2)
                .into(help_img_2)

            Glide.with(this)
                .load(R.drawable.group_3)
                .into(help_img_3)

            Glide.with(this)
                .load(R.drawable.group_4)
                .into(help_img_4)

            Glide.with(this)
                .load(R.drawable.group_5)
                .into(help_img_5)

            Glide.with(this)
                .load(R.drawable.group_6)
                .into(help_img_6)

            Glide.with(this)
                .load(R.drawable.group_7)
                .into(help_img_7)

            Glide.with(this)
                .load(R.drawable.group_8)
                .into(help_img_8)

            Glide.with(this)
                .load(R.drawable.group_9)
                .into(help_img_9)
        } catch (e: Exception) {
            Toast.makeText(this, "$e", Toast.LENGTH_LONG).show()
        }
    }
}