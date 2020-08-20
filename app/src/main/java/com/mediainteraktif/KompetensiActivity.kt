package com.mediainteraktif

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class KompetensiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kompetensi)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}