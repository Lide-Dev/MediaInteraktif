package com.mediainteraktif

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mediainteraktif.ui.latihan.LatihanFragment

class LatihanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latihan)

        val latihanFragment = LatihanFragment()

        if (savedInstanceState != null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_latihan, latihanFragment)
                .commit()
        }
    }
}