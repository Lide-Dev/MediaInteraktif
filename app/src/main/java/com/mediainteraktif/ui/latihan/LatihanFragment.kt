package com.mediainteraktif.ui.latihan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mediainteraktif.R

class LatihanFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_latihan, container, false)
        return root
    }

    companion object {
        const val LATIHAN_DOC = "latihan_extra"
    }
}