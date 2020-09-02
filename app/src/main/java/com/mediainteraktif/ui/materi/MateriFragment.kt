package com.mediainteraktif.ui.materi

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mediainteraktif.MateriActivity
import com.mediainteraktif.R
import java.io.File

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
        btnVideo = root.findViewById(R.id.materi_btn_video)
        imgMateri = root.findViewById(R.id.materi_img_content)
        materiActivity = MateriActivity()

        docNumber = 1

        btnNext.visibility = View.GONE
        btnPrev.visibility = View.GONE
        mStorageRef = FirebaseStorage.getInstance().reference

        noDocument = activity?.intent?.getIntExtra(IDDOCUMENT, 0)
        maxNo = activity?.intent?.getLongExtra(SIZEDOCUMENT, 0L)

        collection = "Materi" + noDocument.toString()
        mFirestore = FirebaseFirestore.getInstance()
        db = mFirestore.collection(collection)

        tvSubtitle.visibility = View.INVISIBLE
        tvContent.visibility = View.INVISIBLE
        btnVideo.visibility = View.GONE
        imgMateri.visibility = View.GONE
        videoId = ""

        getDatabaseData(docNumber)
        check(btnNext, btnPrev)

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

        btnVideo.setOnClickListener {
            if (videoId == "pdf") {
                getPdfFile()
            } else {
                openYoutube()
            }

        }
    }

    fun openYoutube() {
        val iApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        val iWeb =
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$videoId"))
        try {
            startActivity(iApp)
        } catch (e: ActivityNotFoundException) {
            startActivity(iWeb)
        }
    }

    fun getPdfFile() {
        val localFile = File.createTempFile("KODE_KLASIFIKASI", ".pdf")
        val localStorage = mStorageRef
            .child("/KodeKlasifikasi/KODE KLASIFIKASI BARANG.pdf")
        val filePath = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + ".provider",
            localFile
        )

        localStorage.getFile(localFile)
            .addOnSuccessListener {
                Toast.makeText(context, "Opening File", Toast.LENGTH_SHORT).show()
                val iApp = Intent(Intent.ACTION_VIEW)
                iApp.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                iApp.setDataAndType(filePath, "application/pdf")

                val iWeb = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://drive.google.com/file/d/" +
                                "1iB4rhLgHJ3NJoE1eWOgPOwNNJtuBhWQT/view?usp=sharing"
                    )
                )
                try {
                    startActivity(iApp)
                } catch (e: ActivityNotFoundException) {
                    startActivity(iWeb)
                }
            }
    }

    private fun getMateriImg(noDoc: Int): Int {
        return when (noDoc) {
            3 -> R.drawable.materi3
            4 -> R.drawable.materi4
            5 -> R.drawable.materi5
            6 -> R.drawable.materi6
            else -> 0
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDatabaseData(
        docNumber: Int
    ) {
        db.whereEqualTo("no", docNumber)
            .get()
            .addOnSuccessListener { task ->
                tvContent.text = task.documents[0]["contentMateri"].toString()
                    .replace("_n", "\n")
                tvSubtitle.text = task.documents[0]["subtitleMateri"].toString()
                videoId = task.documents[0]["videoUrl"].toString()
                materiImg = task.documents[0]["imageUrl"].toString()

                when {
                    videoId == "pdf" -> {
                        btnVideo.text = "Buka File"
                        btnVideo.visibility = View.VISIBLE
                    }

                    videoId != "null" -> {
                        btnVideo.text = "Buka Video"
                        btnVideo.visibility = View.VISIBLE
                    }

                    videoId == "null" -> {
                        btnVideo.visibility = View.GONE
                    }
                }

                if (materiImg == "yes") {
                    imgMateri.visibility = View.VISIBLE

                    Glide.with(requireContext())
                        .load(getMateriImg(noDocument!!))
                        .into(imgMateri)
                } else {
                    imgMateri.visibility = View.GONE
                }

            }
            .addOnCompleteListener {
                tvSubtitle.visibility = View.VISIBLE
                tvContent.visibility = View.VISIBLE
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
        const val IDDOCUMENT = "extra_no"
        const val SIZEDOCUMENT = "extra_size"

        private lateinit var tvSubtitle: TextView
        private lateinit var tvContent: TextView
        private lateinit var btnNext: FloatingActionButton
        private lateinit var btnPrev: FloatingActionButton
        private lateinit var mFirestore: FirebaseFirestore
        private lateinit var materiActivity: MateriActivity
        private lateinit var mStorageRef: StorageReference
        private lateinit var db: CollectionReference
        private lateinit var btnVideo: Button
        private lateinit var imgMateri: ImageView

        private var docNumber = 1
        private var noDocument: Int? = 0
        private var collection = ""
        private var maxNo: Long? = 0
        private var videoId = ""
        private var materiImg = ""
    }
}