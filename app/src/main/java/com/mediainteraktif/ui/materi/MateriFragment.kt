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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mediainteraktif.MateriActivity
import com.mediainteraktif.R
import kotlin.math.log
import kotlin.math.max

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
    private var maxNo: Long = 0

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

        //database
        noDocument = activity?.intent?.getIntExtra(ID_DOCUMENT, 0)
        collection = "Materi" + noDocument.toString()
        mFirestore = FirebaseFirestore.getInstance()
        val db = mFirestore.collection(collection)

        db.orderBy("no", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                maxNo = querySnapshot.documents.size.toLong()
            }

        getDatabaseData(docNumber, btnNext, btnPrev)

        btnNext.setOnClickListener {
            docNumber = docNumber + 1
            getDatabaseData(docNumber, btnNext, btnPrev)
        }

        btnPrev.setOnClickListener {
            docNumber = docNumber - 1
            getDatabaseData(docNumber, btnNext, btnPrev)
        }

        return root
    }

//    fun numberCheck(docNumber: Int, btnNext: FloatingActionButton, btnPrev: FloatingActionButton) {
////    }

    fun getDatabaseData(
        docNumber: Int,
        btnNext: FloatingActionButton,
        btnPrev: FloatingActionButton
    ) {
        val db = mFirestore.collection(collection)

        db.whereEqualTo("no", docNumber)
            .get()
            .addOnSuccessListener { task ->
                tvContent.text = task.documents[0]["contentMateri"].toString()
                    .replace("_n", "\n")
                tvSubtitle.text = task.documents[0]["subtitleMateri"].toString()

                if (maxNo == 1L) {
                    btnNext.visibility = View.GONE
                    btnPrev.visibility = View.GONE
                } else if (task.documents[0]["no"] as Long >= maxNo) {
                    Log.d(TAG, "BtnNext.Gone")
                    Log.d(TAG, "no" + task.documents[0]["no"] as Long)
                    btnNext.visibility = View.GONE
                    btnPrev.visibility = View.VISIBLE
                }
                else if (task.documents[0]["no"] as Long <= 1) {
                    Log.d(TAG, "BtnPrev.Gone")
                    btnPrev.visibility = View.GONE
                    btnNext.visibility = View.VISIBLE
                }
                else {
                    btnNext.visibility = View.VISIBLE
                    btnPrev.visibility = View.VISIBLE
                }
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