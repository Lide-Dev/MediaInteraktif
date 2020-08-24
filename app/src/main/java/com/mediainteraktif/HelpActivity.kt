package com.mediainteraktif

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        btnNext = findViewById(R.id.help_btn_next)
        btnPrev = findViewById(R.id.help_btn_prev)
        imgHelp = findViewById(R.id.help_img)

        getPic()
        btnCheck()

        btnNext.setOnClickListener(onClickNext())
        btnPrev.setOnClickListener(onClickPrev())
    }

    private fun btnCheck() {
        if (helpPos >= 9)
            btnNext.visibility = View.GONE
        else if (helpPos == 1)
            btnPrev.visibility = View.GONE
        else {
            btnNext.visibility = View.VISIBLE
            btnPrev.visibility = View.VISIBLE
        }
    }

    private fun onClickNext() = View.OnClickListener {
        helpPos += 1
        getPic()
        btnCheck()
    }

    private fun onClickPrev() = View.OnClickListener {
        helpPos -= 1
        getPic()
        btnCheck()
    }

    private fun getPic() {
        Glide.with(this)
            .load(ContextCompat.getDrawable(this, changedPic()))
            .into(imgHelp)
    }

    private fun changedPic(): Int {
        var pic = 0
        when (helpPos) {
            1 -> pic = R.drawable.group_1
            2 -> pic = R.drawable.group_2
            3 -> pic = R.drawable.group_3
            4 -> pic = R.drawable.group_4
            5 -> pic = R.drawable.group_5
            6 -> pic = R.drawable.group_6
            7 -> pic = R.drawable.group_7
            8 -> pic = R.drawable.group_8
            9 -> pic = R.drawable.group_9
        }
        return pic
    }

    companion object {
        private lateinit var btnNext: FloatingActionButton
        private lateinit var btnPrev: FloatingActionButton
        private lateinit var imgHelp: ImageView

        private var helpPos = 1
    }
}