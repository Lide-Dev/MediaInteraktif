package com.mediainteraktif.adapter

import android.annotation.SuppressLint
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mediainteraktif.R
import kotlinx.android.synthetic.main.latihan_rv_item.view.*

class LatihanAdapter : RecyclerView.Adapter<LatihanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.latihan_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setTitle(position + 1)
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.latihan_rv_item_title)

        @SuppressLint("SetTextI18n")
        fun setTitle(number: Int) {
            tvTitle.text = "Latihan $number"
        }
    }

}

