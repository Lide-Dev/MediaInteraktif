package com.mediainteraktif.ui.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mediainteraktif.R

@Suppress("unused")
class QuizFragmentNav : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_quiz, container, false)

        mFirestore = FirebaseFirestore.getInstance()
        db = mFirestore.collection("Quiz")

        initializing(root)

        layoutDialogue.visibility = View.GONE
        layoutContent.visibility = View.GONE
        layoutStart.visibility = View.VISIBLE

        green = ContextCompat.getColor(requireContext(), R.color.green)
        blue = ContextCompat.getColor(requireContext(), R.color.blue)
        red = ContextCompat.getColor(requireContext(), R.color.red)

        return root
    }

    private fun initializing(view: View) {
        btnJawabA = view.findViewById(R.id.quiz_layout_jawaban_a)
        btnJawabB = view.findViewById(R.id.quiz_layout_jawaban_b)
        btnJawabC = view.findViewById(R.id.quiz_layout_jawaban_c)
        btnJawabD = view.findViewById(R.id.quiz_layout_jawaban_d)
        btnJawabE = view.findViewById(R.id.quiz_layout_jawaban_e)
        btnSubmit = view.findViewById(R.id.quiz_btn_submit)

        txtSoal = view.findViewById(R.id.quiz_txt_soal)
        txtJawabA = view.findViewById(R.id.quiz_txt_jawab_a)
        txtJawabB = view.findViewById(R.id.quiz_txt_jawab_b)
        txtJawabC = view.findViewById(R.id.quiz_txt_jawab_c)
        txtJawabD = view.findViewById(R.id.quiz_txt_jawab_d)
        txtJawabE = view.findViewById(R.id.quiz_txt_jawab_e)
        txtA = view.findViewById(R.id.quiz_txt_a)
        txtB = view.findViewById(R.id.quiz_txt_b)
        txtC = view.findViewById(R.id.quiz_txt_c)
        txtD = view.findViewById(R.id.quiz_txt_d)
        txtE = view.findViewById(R.id.quiz_txt_e)

        trySeeLayout = view.findViewById(R.id.quiz_layout_trysee)
        btnTry = view.findViewById(R.id.quiz_btn_tryagain)
        btnSee = view.findViewById(R.id.quiz_btn_hints)

        containerDialogue = view.findViewById(R.id.quiz_container_dialogue)
        layoutContent = view.findViewById(R.id.quiz_layout_content)
        layoutStart = view.findViewById(R.id.quiz_layout_start)
        btnStart = view.findViewById(R.id.quiz_btn_start)

        layoutDialogue = view.findViewById(R.id.quiz_layout_dialogue)
        imgDialogue = view.findViewById(R.id.quiz_img_dialogue)
        txtDialogue = view.findViewById(R.id.quiz_txt_dialogue)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        docPath = "Quiz$documentNumber"

        isAnsClickable(true)

        btnJawabA.setOnClickListener(onClickBtnSelection("a"))
        btnJawabB.setOnClickListener(onClickBtnSelection("b"))
        btnJawabC.setOnClickListener(onClickBtnSelection("c"))
        btnJawabD.setOnClickListener(onClickBtnSelection("d"))
        btnJawabE.setOnClickListener(onClickBtnSelection("e"))
        btnSubmit.setOnClickListener(onClickBtnSubmit())
        btnTry.setOnClickListener(onClickTryAgain())
        btnSee.setOnClickListener(onClickHints())
        btnStart.setOnClickListener(onCLickStart())
        layoutDialogue.setOnClickListener(onClickCloseDialogue())
    }

    private fun onClickCloseDialogue() = View.OnClickListener {
        layoutDialogue.visibility = View.GONE
    }

    private fun onCLickStart() = View.OnClickListener {
        layoutStart.visibility = View.GONE
        layoutContent.visibility = View.VISIBLE
        getQuestionAndSelection()
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

    private fun isWrong() {
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
        isAnsClickable(true)
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
            showAnswerDialogue(true)
        } else if (userAnswer == "-") {
            Toast.makeText(activity, "Tolong pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
        } else {
            btnSubmit.visibility = View.GONE
            trySeeLayout.visibility = View.VISIBLE
            btnSee.visibility = View.VISIBLE
            btnTry.visibility = View.VISIBLE
            isWrong()
            isAnsClickable(false)
            showAnswerDialogue(false)
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

    private fun isAnsClickable(ans: Boolean) {
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

    @SuppressLint("SetTextI18n")
    private fun showAnswerDialogue(ans: Boolean) {
        when (ans) {
            true -> {
                containerDialogue.background.setTint(green)
                imgDialogue.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_24)
                txtDialogue.text = "Benar"
                layoutDialogue.visibility = View.VISIBLE
            }

            false -> {
                containerDialogue.background.setTint(red)
                imgDialogue.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_24)
                txtDialogue.text = "Salah"
                layoutDialogue.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private lateinit var mFirestore: FirebaseFirestore
        private lateinit var db: CollectionReference

        private lateinit var btnJawabA: LinearLayout
        private lateinit var btnJawabB: LinearLayout
        private lateinit var btnJawabC: LinearLayout
        private lateinit var btnJawabD: LinearLayout
        private lateinit var btnJawabE: LinearLayout
        private lateinit var trySeeLayout: LinearLayout
        private lateinit var layoutContent: ConstraintLayout
        private lateinit var layoutStart: ConstraintLayout
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

        private lateinit var btnStart: TextView

        private lateinit var layoutDialogue: ConstraintLayout
        private lateinit var containerDialogue: LinearLayout
        private lateinit var txtDialogue: TextView
        private lateinit var imgDialogue: ImageView

        private var blue = 1
        private var green = 1
        private var red = 1
        private var documentNumber = 1
        private var docPath = ""
        private var realAnswer = "F"
        private var userAnswer = "-"
    }
}