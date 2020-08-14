package com.mediainteraktif.ui.materi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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

    private var docNumber = 1
    private var noDocument: Int? = 0
    private var collection = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_materi, container, false)

        tvSubtitle = root.findViewById<TextView>(R.id.materi_txt_subtitle)
        tvContent = root.findViewById<TextView>(R.id.materi_txt_content)
        btnNext = root.findViewById<FloatingActionButton>(R.id.materi_btn_next)
        btnPrev = root.findViewById<FloatingActionButton>(R.id.materi_btn_prev)
        materiActivity = MateriActivity()

        //database
        noDocument = activity?.intent?.getIntExtra(ID_DOCUMENT, 0)
        collection = "Materi" + noDocument.toString()
        mFirestore = FirebaseFirestore.getInstance()

        getDatabaseText(docNumber)
        numberCheck(docNumber, btnNext, btnPrev)

        btnNext.setOnClickListener {
            docNumber = docNumber + 1
            getDatabaseText(docNumber)
        }

        btnPrev.setOnClickListener {
            docNumber = docNumber - 1
            getDatabaseText(docNumber)
        }

        return root
    }

    fun numberCheck(docNumber: Int, btn1: FloatingActionButton, btn2: FloatingActionButton) {
        if (docNumber >= 4) {
            btn1.visibility = View.GONE
        } else if (docNumber <= 1) {
            btn2.visibility = View.GONE
        } else {
            btn1.visibility = View.VISIBLE
            btn2.visibility = View.VISIBLE
        }
    }

    fun getDatabaseText(docNumber: Int) {
        val db = mFirestore.collection(collection)
        db.whereEqualTo("no", docNumber)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    tvContent.text = """${document["contentMateri"]}"""
                        .replace("_n", "\n")
                    tvSubtitle.text = """${document["subtitleMateri"]}"""
                }
                numberCheck(docNumber, btnNext, btnPrev)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(activity, "ERROR NO DOCUMENT! " + exception, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    companion object {
        const val ID_DOCUMENT = "extra_document"
    }
}