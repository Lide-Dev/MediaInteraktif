package com.mediainteraktif

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val btnStart = findViewById<LinearLayout>(R.id.splash_btn_start)

        btnStart.setOnClickListener {
            btnStart.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_click_button))
            btnStart.visibility = View.INVISIBLE
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }
}