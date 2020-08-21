package com.mediainteraktif.adapter

import android.annotation.SuppressLint
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.mediainteraktif.R
import kotlinx.android.synthetic.main.latihan_rv_item.view.*

class LatihanAdapter : RecyclerView.Adapter<LatihanAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.latihan_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setTitle(position + 1)
        holder.clicking()
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.latihan_rv_item_title)

        @SuppressLint("SetTextI18n")
        fun setTitle(number: Int) {
            tvTitle.text = "Latihan $number"
        }

        fun clicking() {
            itemView.setOnClickListener {
                val pos: Int = adapterPosition
                if (pos != RecyclerView.NO_POSITION)
                    listener.onItemClick(pos)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

