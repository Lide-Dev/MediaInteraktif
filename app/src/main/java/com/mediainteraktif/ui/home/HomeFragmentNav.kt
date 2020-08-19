package com.mediainteraktif.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
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
import com.mediainteraktif.R
import com.mediainteraktif.adapter.HomeAdapter
import com.mediainteraktif.model.HomeModel
import com.mediainteraktif.ui.materi.MateriFragment
import com.mediainteraktif.ui.materi.MateriFragment.Companion.ID_DOCUMENT
import com.mediainteraktif.ui.materi.MateriFragment.Companion.SIZE_DOCUMENT

class HomeFragmentNav : Fragment(), HomeAdapter.OnItemClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var db: CollectionReference
    private lateinit var adapter: HomeAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home_nav, container, false)

        mAuth = FirebaseAuth.getInstance()
        val mUser: FirebaseUser? = mAuth.currentUser
        mFirestore = FirebaseFirestore.getInstance()

        val txtWelcome: TextView = root.findViewById(R.id.welcome)

        txtWelcome.text = "Selamat Datang ${mUser?.displayName}"

        getData(root)

        return root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        adapter.stopListening()
    }

    fun getData(view: View) {
        val collectionReference = mFirestore.collection("listMateri")
        val query = collectionReference.orderBy("no_materi", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions
            .Builder<HomeModel>()
            .setQuery(query, HomeModel::class.java)
            .build()

        val rvMateri: RecyclerView = view.findViewById(R.id.home_rv_materi)
        rvMateri.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        adapter = HomeAdapter(options)
        adapter.setOnItemClickListener(this)
        rvMateri.adapter = adapter
    }

    override fun onItemClick(documentSnapshot: DocumentSnapshot, pos: Int) {
        var maxNo = 0L

        db = mFirestore.collection("Materi${pos+1}")
        db.orderBy("no", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                maxNo = querySnapshot.documents[0]["no"] as Long
                Log.d("DOCUMENT", "Success get document size $maxNo")

                val i = Intent(activity, MateriActivity::class.java).also {
                    it.putExtra(ID_DOCUMENT, pos+1)
                    it.putExtra(SIZE_DOCUMENT, maxNo)
                }
                startActivity(i)
            }
            .addOnFailureListener { exception ->
                Log.w("DOCUMENT", "Failed to get document size", exception)
            }

        Log.d("DOCUMENT", "maxNo after get document size : $maxNo")
    }

}