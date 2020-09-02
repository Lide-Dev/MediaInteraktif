package com.mediainteraktif

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private var mAuth = FirebaseAuth.getInstance()
    private var mUser = mAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val btnStart = findViewById<LinearLayout>(R.id.splash_btn_start)

        btnStart.setOnClickListener {
            btnStart.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_click_button))
            btnStart.visibility = View.INVISIBLE

            if (btnStart.visibility == View.INVISIBLE) {
                if (mUser != null) {
                    intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}