package com.mediainteraktif

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mediainteraktif.ui.materi.MateriFragment

class MateriActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi)

        val materifragment = MateriFragment()

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.materi_fragment, materifragment)
                .commit()
        }

        supportActionBar?.title = intent.getStringExtra(TITLEDOC)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_3dot_menu, menu)

        val item = menu?.findItem(R.id.logout)
        item?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.daftar_pustaka -> null
//            R.id.help -> null
//        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TITLEDOC = "extra_title"
    }
}