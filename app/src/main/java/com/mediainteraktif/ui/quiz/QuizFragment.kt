package com.mediainteraktif.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mediainteraktif.R

@Suppress("unused")
class QuizFragment : Fragment() {

    companion object {
        private lateinit var mFirestore: FirebaseFirestore
        private lateinit var db: CollectionReference

        private lateinit var btnJawabA: LinearLayout
        private lateinit var btnJawabB: LinearLayout
        private lateinit var btnJawabC: LinearLayout
        private lateinit var btnJawabD: LinearLayout
        private lateinit var btnJawabE: LinearLayout
        private lateinit var trySeeLayout: LinearLayout
        private lateinit var btnSubmit: Button
        private lateinit var btnTry: Button
        private lateinit var btnSee: Button
        private lateinit var txtSoal: TextView
        private lateinit var txtA: TextView
        private lateinit var txtB: TextView
        private lateinit var txtC: TextView
        private lateinit var txtD: TextView
        private lateinit var txtE: TextView
        private lateinit var txtJawabA: TextView
        private lateinit var txtJawabB: TextView
        private lateinit var txtJawabC: TextView
        private lateinit var txtJawabD: TextView
        private lateinit var txtJawabE: TextView

        private var blue = 1
        private var green = 1
        private var red = 1
        private var documentNumber = 1
        private var docPath = ""
        private var realAnswer = "F"
        private var userAnswer = "-"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_quiz, container, false)

        mFirestore = FirebaseFirestore.getInstance()
        db = mFirestore.collection("Quiz")

        btnJawabA = root.findViewById(R.id.quiz_layout_jawaban_a)
        btnJawabB = root.findViewById(R.id.quiz_layout_jawaban_b)
        btnJawabC = root.findViewById(R.id.quiz_layout_jawaban_c)
        btnJawabD = root.findViewById(R.id.quiz_layout_jawaban_d)
        btnJawabE = root.findViewById(R.id.quiz_layout_jawaban_e)
        btnSubmit = root.findViewById(R.id.quiz_btn_submit)

        txtSoal = root.findViewById(R.id.quiz_txt_soal)
        txtJawabA = root.findViewById(R.id.quiz_txt_jawab_a)
        txtJawabB = root.findViewById(R.id.quiz_txt_jawab_b)
        txtJawabC = root.findViewById(R.id.quiz_txt_jawab_c)
        txtJawabD = root.findViewById(R.id.quiz_txt_jawab_d)
        txtJawabE = root.findViewById(R.id.quiz_txt_jawab_e)
        txtA = root.findViewById(R.id.quiz_txt_a)
        txtB = root.findViewById(R.id.quiz_txt_b)
        txtC = root.findViewById(R.id.quiz_txt_c)
        txtD = root.findViewById(R.id.quiz_txt_d)
        txtE = root.findViewById(R.id.quiz_txt_e)

        trySeeLayout = root.findViewById(R.id.quiz_layout_trysee)
        btnTry = root.findViewById(R.id.quiz_btn_tryagain)
        btnSee = root.findViewById(R.id.quiz_btn_hints)

        green = ContextCompat.getColor(requireContext(), R.color.green)
        blue = ContextCompat.getColor(requireContext(), R.color.blue)
        red = ContextCompat.getColor(requireContext(), R.color.red)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        docPath = "Quiz$documentNumber"

        isClickable(true)
        getQuestionAndSelection()

        btnJawabA.setOnClickListener(onClickBtnSelection("a"))
        btnJawabB.setOnClickListener(onClickBtnSelection("b"))
        btnJawabC.setOnClickListener(onClickBtnSelection("c"))
        btnJawabD.setOnClickListener(onClickBtnSelection("d"))
        btnJawabE.setOnClickListener(onClickBtnSelection("e"))
        btnSubmit.setOnClickListener(onClickBtnSubmit())
        btnTry.setOnClickListener(onClickTryAgain())
        btnSee.setOnClickListener(onClickHints())
    }

    private fun onClickHints() = View.OnClickListener {
        when (realAnswer) {
            "a" -> {
                setBtnBackgroundColor(green, red, red, red, red)
            }
            "b" -> {
                setBtnBackgroundColor(red, green, red, red, red)
            }
            "c" -> {
                setBtnBackgroundColor(red, red, green, red, red)
            }
            "d" -> {
                setBtnBackgroundColor(red, red, red, green, red)
            }
            "e" -> {
                setBtnBackgroundColor(red, red, red, red, green)
            }
        }
        btnSee.visibility = View.INVISIBLE
    }

    private fun ifWrong() {
        when (userAnswer) {
            "a" -> {
                setBtnBackgroundColor(red, blue, blue, blue, blue)
            }
            "b" -> {
                setBtnBackgroundColor(blue, red, blue, blue, blue)
            }
            "c" -> {
                setBtnBackgroundColor(blue, blue, red, blue, blue)
            }
            "d" -> {
                setBtnBackgroundColor(blue, blue, blue, red, blue)
            }
            "e" -> {
                setBtnBackgroundColor(blue, blue, blue, blue, red)
            }
        }
    }

    private fun onClickTryAgain() = View.OnClickListener {
        setBtnBackgroundColor(blue, blue, blue, blue, blue)
        trySeeLayout.visibility = View.GONE
        btnSubmit.visibility = View.VISIBLE
        isClickable(true)
        userAnswer = "-"
    }

    private fun onClickBtnSelection(selection: String) =
        View.OnClickListener {
            userAnswer = selection

            when (selection) {
                "a" -> {
                    setBtnBackgroundColor(green, blue, blue, blue, blue)
                }
                "b" -> {
                    setBtnBackgroundColor(blue, green, blue, blue, blue)
                }
                "c" -> {
                    setBtnBackgroundColor(blue, blue, green, blue, blue)
                }
                "d" -> {
                    setBtnBackgroundColor(blue, blue, blue, green, blue)
                }
                "e" -> {
                    setBtnBackgroundColor(blue, blue, blue, blue, green)
                }
            }
        }

    private fun onClickBtnSubmit() = View.OnClickListener {
        if (userAnswer == realAnswer) {
            documentNumber += 1
            docPath = "Quiz$documentNumber"
            Log.d("Document", "document path: $docPath")
            getQuestionAndSelection()
            setBtnBackgroundColor(blue, blue, blue, blue, blue)
        } else if (userAnswer == "-") {
            Toast.makeText(activity, "Tolong pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
        } else {
            btnSubmit.visibility = View.GONE
            trySeeLayout.visibility = View.VISIBLE
            btnSee.visibility = View.VISIBLE
            btnTry.visibility = View.VISIBLE
            isClickable(false)
            ifWrong()
        }
    }

    private fun getQuestionAndSelection() {
        Log.d("Quiz", "document path : $docPath")

        db.document(docPath)
            .get()
            .addOnSuccessListener { task ->
                txtSoal.text = task["questionQuiz"].toString()
                txtJawabA.text = task["selectionA"].toString()
                txtJawabB.text = task["selectionB"].toString()
                txtJawabC.text = task["selectionC"].toString()
                txtJawabD.text = task["selectionD"].toString()
                txtJawabE.text = task["selectionE"].toString()
                realAnswer = task["answer"].toString()
            }
            .addOnFailureListener { exception ->
                Log.w("exception", "ERROR GETTING DOCUMENT: $exception", exception)
            }
    }

    private fun isClickable(ans: Boolean) {
        btnJawabA.isClickable = ans
        btnJawabB.isClickable = ans
        btnJawabC.isClickable = ans
        btnJawabD.isClickable = ans
        btnJawabE.isClickable = ans
    }

    private fun setBtnBackgroundColor(a: Int, b: Int, c: Int, d: Int, e: Int) {
        btnJawabA.background.setTint(a)
        txtA.background.setTint(a)
        btnJawabB.background.setTint(b)
        txtB.background.setTint(b)
        btnJawabC.background.setTint(c)
        txtC.background.setTint(c)
        btnJawabD.background.setTint(d)
        txtD.background.setTint(d)
        btnJawabE.background.setTint(e)
        txtE.background.setTint(e)
    }
}