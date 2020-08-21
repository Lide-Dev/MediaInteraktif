package com.mediainteraktif.ui.latihan

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.service.autofill.TextValueSanitizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mediainteraktif.R
import org.w3c.dom.Text
import java.io.File
import java.net.URI

class LatihanFragment : Fragment() {
    private lateinit var mStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var db: DocumentReference

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var imgAnswer: ImageView
    private lateinit var imgAdd: ImageView

    private var noDocument: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_latihan, container, false)
        noDocument = activity?.intent?.getIntExtra(LATIHAN_DOC, 0)!!

        mStorage = FirebaseStorage.getInstance()
        storageReference = mStorage.reference

        mFirestore = FirebaseFirestore.getInstance()
        db = mFirestore.collection("Latihan")
            .document("Latihan$noDocument")

        tvTitle = root.findViewById(R.id.latihan_txt_number)
        tvContent = root.findViewById(R.id.latihan_txt_content)
        imgAdd = root.findViewById(R.id.latihan_add_xlx)
        imgAnswer = root.findViewById(R.id.latihan_answer_xlx)

        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitle.text = "Latihan Soal $noDocument"
        getQuestion()

        imgAdd.setOnClickListener(onClickListener("add"))
        imgAnswer.setOnClickListener(onClickListener("ans"))
    }

    fun onClickListener(type: String) = View.OnClickListener {
//        when (type) {
//            "ans" -> {
//
//                val pathRef = storageReference.child("Latihan/Answer/LT ${noDocument}.xlsx")
//                val filePath = File("content:///data/user/0/com.mediainteraktif/cache/LT%2015810054286600217657xlsx")
//                Log.d("filepath", "$filePath")
//                val localFile = File.createTempFile("LT $noDocument", ".xlsx", filePath)
//
//                val i = Intent(Intent.ACTION_VIEW)
//                i.setDataAndType(Uri.fromFile(localFile), "application/vnd.ms-excel")
//                startActivity(i)
//
//                pathRef.getFile(localFile)
//                    .addOnCompleteListener { task ->
//                        Lo
//
//                        val i = Intent(Intent.ACTION_VIEW)
//                        i.setDataAndType(Uri.fromFile(localFile), "application/vnd.ms-excel")
//                        startActivity(i)
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.w("Storage", "FAILED TO DOWNLOAD", exception)
//                    }
//            }
//            "add" -> {
//            }
//        }
    }

    fun getQuestion() {
        db.get()
            .addOnSuccessListener { task ->
                tvContent.text = task["contentLatihan"].toString()
                    .replace("_n", "\n")
            }
            .addOnFailureListener { exception ->
                Log.w("LATIHAN", "FAILED TO GET DOCUMENT", exception)
            }
    }

    companion object {
        const val LATIHAN_DOC = "latihan_extra"
    }
}