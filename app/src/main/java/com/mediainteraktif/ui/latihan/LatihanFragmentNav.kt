package com.mediainteraktif.ui.latihan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mediainteraktif.R
import com.mediainteraktif.adapter.LatihanAdapter

class LatihanFragmentNav : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_latihan_nav, container, false)
        val adapter = LatihanAdapter()

        recyclerView = root.findViewById(R.id.latihan_rv)
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        return root
    }
}