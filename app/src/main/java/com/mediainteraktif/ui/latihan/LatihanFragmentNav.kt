package com.mediainteraktif.ui.latihan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mediainteraktif.LatihanActivity
import com.mediainteraktif.R
import com.mediainteraktif.adapter.LatihanAdapter

class LatihanFragmentNav : Fragment(), LatihanAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LatihanAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_latihan_nav, container, false)
        adapter = LatihanAdapter()

        recyclerView = root.findViewById(R.id.latihan_rv)
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)

        return root
    }

    override fun onItemClick(pos: Int) {
        val i = Intent(activity, LatihanActivity::class.java).also {
            it.putExtra(LatihanFragment.LATIHAN_DOC, pos+1)
        }
        startActivity(i)
    }
}