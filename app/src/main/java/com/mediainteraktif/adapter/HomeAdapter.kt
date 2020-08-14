package com.mediainteraktif.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.mediainteraktif.R
import com.mediainteraktif.model.HomeModel

class HomeAdapter internal constructor(options: FirestoreRecyclerOptions<HomeModel>) :
    FirestoreRecyclerAdapter<HomeModel, HomeAdapter.HomeViewHolder>(options) {

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.materi_rv_item,
            parent, false
        )
        val mHolder = HomeViewHolder(view)
        return mHolder
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int, model: HomeModel) {
        holder.setTitle(model.title_materi)
        holder.clicking()
    }

    inner class HomeViewHolder internal constructor(private val view: View) :
        RecyclerView.ViewHolder(view) {
        lateinit var tvTitle: TextView

        internal fun setTitle(title: String) {
            tvTitle = view.findViewById<TextView>(R.id.materi_rv_txt)
            tvTitle.text = title
        }


        internal fun clicking() {
            view.setOnClickListener {
                val pos: Int = adapterPosition
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(snapshots.getSnapshot(pos), pos)
                }

            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(documentSnapshot: DocumentSnapshot, pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}