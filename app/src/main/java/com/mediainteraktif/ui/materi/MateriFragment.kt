package com.mediainteraktif.ui.materi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.mediainteraktif.MateriActivity
import com.mediainteraktif.R

class MateriFragment : Fragment() {
    private lateinit var tvSubtitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var btnNext: FloatingActionButton
    private lateinit var btnPrev: FloatingActionButton
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var materiActivity: MateriActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_materi, container, false)

        val tvSubtitle = root.findViewById<TextView>(R.id.materi_txt_subtitle)
        val tvContent = root.findViewById<TextView>(R.id.materi_txt_content)
        val btnNext = root.findViewById<FloatingActionButton>(R.id.materi_btn_next)
        val btnPrev = root.findViewById<FloatingActionButton>(R.id.materi_btn_prev)

        materiActivity = MateriActivity()

        val noDocument = activity?.intent?.getIntExtra(ID_DOCUMENT, 0)
        var no = 1

        val collection: String = "Materi" + noDocument.toString()
        mFirestore = FirebaseFirestore.getInstance()
        val db = mFirestore.collection(collection)

        numberCheck(no, btnNext, btnPrev)

        db.whereEqualTo("no", no)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    tvContent.text = "${document["contentMateri"]}"
                    tvSubtitle.text = "${document["subtitleMateri"]}"
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR", "Error getting documents: ", exception)
            }

        btnNext.setOnClickListener {
            no = no + 1
            db.whereEqualTo("no", no)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        tvContent.text = "${document["contentMateri"]}"
                        tvSubtitle.text = "${document["subtitleMateri"]}"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ERROR", "Error getting documents: ", exception)
                }
        }

        btnPrev.setOnClickListener {
            no = no - 1
            db.whereEqualTo("no", no)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        tvContent.text = "${document["contentMateri"]}"
                        tvSubtitle.text = "${document["subtitleMateri"]}"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ERROR", "Error getting documents: ", exception)
                }

        }

        return root
    }

    fun numberCheck(no: Int, btn1: FloatingActionButton, btn2: FloatingActionButton) {
        if(no > 2) {
            btn1.visibility = View.GONE
        } else if(no < 1) {
            btn2.visibility = View.GONE
        }
    }

    companion object {
        const val ID_DOCUMENT = "extra_document"
    }
}