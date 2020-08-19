package com.mediainteraktif.ui.materi

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
        maxNo = activity?.intent?.getLongExtra(SIZE_DOCUMENT, 0L)

        Log.d("DOCUMENT", "noDocument get from Intent : $noDocument")
        Log.d("DOCUMENT", "maxNo get from Intent : $maxNo")

        collection = "Materi" + noDocument.toString()
        mFirestore = FirebaseFirestore.getInstance()
        db = mFirestore.collection(collection)

        getDatabaseData(docNumber)
        check(btnNext, btnPrev)

        Log.d("MateriFragment", "------------- END MateriFragment -------------")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext.setOnClickListener {
            docNumber += 1
            check(btnNext, btnPrev)
            getDatabaseData(docNumber)
        }

        btnPrev.setOnClickListener {
            docNumber -= 1
            check(btnNext, btnPrev)
            getDatabaseData(docNumber)
        }
    }

    private fun getDatabaseData(
        docNumber: Int
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
    }

    //TODO fix btnNext and btnPrev Bug

    private fun check(
        btnNext: FloatingActionButton,
        btnPrev: FloatingActionButton
    ) {
        when {
            maxNo == 1L -> {
                btnPrev.visibility = View.GONE
                btnNext.visibility = View.GONE
            }

            docNumber.toLong() <= 1L -> {
                btnNext.visibility = View.VISIBLE
                btnPrev.visibility = View.GONE
            }

            docNumber.toLong() >= maxNo!! -> {
                btnNext.visibility = View.GONE
                btnPrev.visibility = View.VISIBLE
            }

            else -> {
                btnNext.visibility = View.VISIBLE
                btnPrev.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val ID_DOCUMENT = "extra_no"
        const val SIZE_DOCUMENT = "extra_size"

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
        private var maxNo: Long? = 0
    }
}