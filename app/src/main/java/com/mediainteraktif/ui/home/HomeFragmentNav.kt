package com.mediainteraktif.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mediainteraktif.MateriActivity
import com.mediainteraktif.MateriActivity.Companion.TITLEDOC
import com.mediainteraktif.R
import com.mediainteraktif.adapter.HomeAdapter
import com.mediainteraktif.model.HomeModel
import com.mediainteraktif.ui.materi.MateriFragment.Companion.IDDOCUMENT
import com.mediainteraktif.ui.materi.MateriFragment.Companion.SIZEDOCUMENT

class HomeFragmentNav : Fragment(), HomeAdapter.OnItemClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var db: CollectionReference
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var rvMateri: RecyclerView
    private lateinit var mUser: FirebaseUser

    private lateinit var txtWelcome: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home_nav, container, false)
        txtWelcome = root.findViewById(R.id.welcome)
        rvMateri = root.findViewById(R.id.home_rv_materi)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mFirestore = FirebaseFirestore.getInstance()

        rvMateri.visibility = View.GONE

        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtWelcome.text = "Selamat Datang ${mUser.displayName}"
        rvMateri.visibility = View.VISIBLE

        getData()
    }

    override fun onStart() {
        super.onStart()
        homeAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        homeAdapter.stopListening()
    }

    fun getData() {
        val collectionReference = mFirestore.collection("listMateri")
        val query = collectionReference.orderBy("no_materi", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions
            .Builder<HomeModel>()
            .setQuery(query, HomeModel::class.java)
            .build()

        rvMateri.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            GridLayoutManager(activity, 2)

        homeAdapter = HomeAdapter(options)
        homeAdapter.setOnItemClickListener(this)
        rvMateri.adapter = homeAdapter
    }

    override fun onItemClick(documentSnapshot: DocumentSnapshot, pos: Int) {
        var maxNo = 0L
        val title = "Materi ${pos+1}"

        db = mFirestore.collection("Materi${pos+1}")
        db.orderBy("no", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                maxNo = querySnapshot.documents[0]["no"] as Long
                Log.d("DOCUMENT", "Success get document size $maxNo")

                val i = Intent(activity, MateriActivity::class.java).also {
                    it.putExtra(IDDOCUMENT, pos+1)
                    it.putExtra(SIZEDOCUMENT, maxNo)
                    it.putExtra(TITLEDOC, title)
                }
                startActivity(i)
            }
            .addOnFailureListener { exception ->
                Log.w("DOCUMENT", "Failed to get document size", exception)
            }

        Log.d("DOCUMENT", "maxNo after get document size : $maxNo")
    }

}