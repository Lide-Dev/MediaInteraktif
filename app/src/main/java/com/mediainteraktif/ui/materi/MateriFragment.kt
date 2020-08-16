package com.mediainteraktif.ui.materi

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mediainteraktif.MateriActivity
import com.mediainteraktif.R

class MateriFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_materi, container, false)

        //initializing
        tvSubtitle = root.findViewById(R.id.materi_txt_subtitle)
        tvContent = root.findViewById(R.id.materi_txt_content)
        btnNext = root.findViewById(R.id.materi_btn_next)
        btnPrev = root.findViewById(R.id.materi_btn_prev)
        materiActivity = MateriActivity()

        btnNext.visibility = View.GONE
        btnPrev.visibility = View.GONE

        noDocument = activity?.intent?.getIntExtra(ID_DOCUMENT, 0)
        collection = "Materi" + noDocument.toString()
        mFirestore = FirebaseFirestore.getInstance()
        db = mFirestore.collection(collection)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.orderBy("no", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {querySnapshot ->
                maxNo = querySnapshot.documents[0]["no"] as Long
            }

        getDatabaseData(docNumber, btnNext, btnPrev)

        Log.d(TAG, "size document : $maxNo")

        btnNext.setOnClickListener {
            docNumber += 1
            getDatabaseData(docNumber, btnNext, btnPrev)
        }

        btnPrev.setOnClickListener {
            docNumber -= 1
            getDatabaseData(docNumber, btnNext, btnPrev)
        }
    }

    private fun getDatabaseData(
        docNumber: Int,
        btnNext: FloatingActionButton,
        btnPrev: FloatingActionButton
    ) {
        db.whereEqualTo("no", docNumber)
            .get()
            .addOnSuccessListener { task ->
                tvContent.text = task.documents[0]["contentMateri"].toString()
                    .replace("_n", "\n")
                tvSubtitle.text = task.documents[0]["subtitleMateri"].toString()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(activity, "ERROR NO DOCUMENT! $exception", Toast.LENGTH_SHORT)
                    .show()
            }

        check(btnNext, btnPrev)
    }

    //TODO fix btnNext and btnPrev Bug

    private fun check(
        btnNext: FloatingActionButton,
        btnPrev: FloatingActionButton
    ) {
        Log.d(TAG, "current document number $docNumber")
        Log.d(TAG, "get document size from database $maxNo")

        when {
            docNumber.toLong() <= 1L -> {
                btnNext.visibility = View.VISIBLE
                btnPrev.visibility = View.GONE
            }

            docNumber.toLong() >= maxNo -> {
                btnNext.visibility = View.GONE
                btnPrev.visibility = View.VISIBLE
            }

            maxNo == 1L -> {
                btnPrev.visibility = View.GONE
                btnNext.visibility = View.GONE
            }

            else -> {
                btnNext.visibility = View.VISIBLE
                btnPrev.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val ID_DOCUMENT = "extra_document"

        private lateinit var tvSubtitle: TextView
        private lateinit var tvContent: TextView
        private lateinit var btnNext: FloatingActionButton
        private lateinit var btnPrev: FloatingActionButton
        private lateinit var mFirestore: FirebaseFirestore
        private lateinit var materiActivity: MateriActivity
        private lateinit var db: CollectionReference

        private var docNumber = 1
        private var noDocument: Int? = 0
        private var collection = ""
        private var maxNo: Long = 0L
    }
}