package com.mediainteraktif.ui.quiz

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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
        val root = inflater.inflate(R.layout.fragment_quiz_nav, container, false)

        mFirestore = FirebaseFirestore.getInstance()
        db = mFirestore.collection("Quiz")

        initializing(root)

        layoutDialogue.visibility = View.GONE
        layoutContent.visibility = View.GONE
        layoutStart.visibility = View.VISIBLE

        green = ContextCompat.getColor(requireContext(), R.color.green)
        blue = ContextCompat.getColor(requireContext(), R.color.blue)
        red = ContextCompat.getColor(requireContext(), R.color.red)

        if (Build.VERSION.SDK_INT < 24) {
            initLow()
        }

        return root
    }

    private fun initLow() {
        btnJawabA.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_rounded_10dp_little_blue_corner
        )
        btnJawabB.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_rounded_10dp_little_blue_corner
        )
        btnJawabC.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_rounded_10dp_little_blue_corner
        )
        btnJawabD.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_rounded_10dp_little_blue_corner
        )
        btnJawabE.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_rounded_10dp_little_blue_corner
        )
    }

    private fun initializing(view: View) {
        btnJawabA = view.findViewById(R.id.quiz_layout_jawaban_a)
        btnJawabB = view.findViewById(R.id.quiz_layout_jawaban_b)
        btnJawabC = view.findViewById(R.id.quiz_layout_jawaban_c)
        btnJawabD = view.findViewById(R.id.quiz_layout_jawaban_d)
        btnJawabE = view.findViewById(R.id.quiz_layout_jawaban_e)
        btnSubmit = view.findViewById(R.id.quiz_btn_submit)

        imgSoal = view.findViewById(R.id.quiz_img_soal)
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

        layoutResult = view.findViewById(R.id.quiz_layout_result)
        txtResult = view.findViewById(R.id.quiz_txt_result)
        btnReset = view.findViewById(R.id.quiz_btn_reset)
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
        btnStart.setOnClickListener(onClickStart())
        layoutDialogue.setOnClickListener(onClickDialogue())
        btnReset.setOnClickListener(onClickReset())
    }

    private fun onClickReset() = View.OnClickListener {
        documentNumber = 1
        docPath = "Quiz$documentNumber"

        setBtnBackgroundColor(blue, blue, blue, blue, blue)
        layoutResult.visibility = View.GONE
        layoutStart.visibility = View.VISIBLE

        isDone = false
    }

    private fun onClickDialogue() = View.OnClickListener {
        layoutDialogue.visibility = View.GONE
    }

    private fun onClickStart() = View.OnClickListener {
        if (isDone) {
            txtResult.text = finalResult.toString()
            Toast.makeText(activity, "anda sudah selesai mengerjakan quiz", Toast.LENGTH_SHORT)
                .show()
            layoutStart.visibility = View.GONE
            layoutContent.visibility = View.GONE
            layoutResult.visibility = View.VISIBLE
        } else {
            layoutStart.visibility = View.GONE
            layoutContent.visibility = View.VISIBLE
            getQuestionAndSelection()
        }
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
        userAnswer = ""
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
        when {
            userAnswer == realAnswer -> {
                if (documentNumber >= 25) {
                    layoutContent.visibility = View.GONE
                    txtResult.text = finalResult.toString()
                    layoutResult.visibility = View.VISIBLE
                    isDone = true
                } else {

                    documentNumber += 1
                    docPath = "Quiz$documentNumber"

                    getQuestionAndSelection()
                    setBtnBackgroundColor(blue, blue, blue, blue, blue)
                    showAnswerDialogue(true)

                    alrWrong = false
                }
            }

            userAnswer.isEmpty() -> {
                Toast.makeText(activity, "Tolong pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {
                btnSubmit.visibility = View.GONE
                trySeeLayout.visibility = View.VISIBLE
                btnSee.visibility = View.VISIBLE
                btnTry.visibility = View.VISIBLE

                if (!alrWrong) {
                    finalResult -= 4
                    alrWrong = true
                }

                isWrong()
                isAnsClickable(false)
                showAnswerDialogue(false)
            }
        }
    }

    private fun getQuestionAndSelection() {
        when {
            documentNumber == 15 -> {
                imgSoal.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(R.drawable.tabel15)
                    .into(imgSoal)
            }

            documentNumber == 16 -> {
                imgSoal.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(R.drawable.tabel16)
                    .into(imgSoal)
            }

            documentNumber != 15 || documentNumber != 16 -> {
                imgSoal.visibility = View.GONE
            }
        }

        userAnswer = ""
        layoutContent.visibility = View.GONE

        db.document(docPath)
            .get()
            .addOnSuccessListener { task ->
                txtSoal.text = task["questionQuiz"].toString().replace("_n", "\n")
                txtJawabA.text = task["selectionA"].toString().replace("_n", "\n")
                txtJawabB.text = task["selectionB"].toString().replace("_n", "\n")
                txtJawabC.text = task["selectionC"].toString().replace("_n", "\n")
                txtJawabD.text = task["selectionD"].toString().replace("_n", "\n")
                txtJawabE.text = task["selectionE"].toString().replace("_n", "\n")
                realAnswer = task["answer"].toString()
            }
            .addOnCompleteListener {
                layoutContent.visibility = View.VISIBLE
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
        if (Build.VERSION.SDK_INT >= 24) {
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
        } else {
            setDrawableQuizSelection(a, b, c, d, e)
        }
    }

    private fun setDrawableQuizSelection(a: Int, b: Int, c: Int, d: Int, e: Int) {
        val greenRounded =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_rounded_10dp_green_corner)
        val blueRounded = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_rounded_10dp_little_blue_corner
        )
        val redRounded =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_rounded_10dp_red_corner)

        val greenFill =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_rounded_fill_green_10dp)
        val blueFill =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_rounded_fill_blue_10dp)
        val redFill =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_rounded_fill_red_10dp)

        when (a) {
            greenCode -> {
                btnJawabA.background = greenRounded
                txtA.background = greenFill
            }
            blueCode -> {
                btnJawabA.background = blueRounded
                txtA.background = blueFill
            }
            redCode -> {
                btnJawabA.background = redRounded
                txtA.background = redFill
            }
        }
        when (b) {
            greenCode -> {
                btnJawabB.background = greenRounded
                txtB.background = greenFill
            }
            blueCode -> {
                btnJawabB.background = blueRounded
                txtB.background = blueFill
            }
            redCode -> {
                btnJawabB.background = redRounded
                txtB.background = redFill
            }
        }
        when (c) {
            greenCode -> {
                btnJawabC.background = greenRounded
                txtC.background = greenFill
            }
            blueCode -> {
                btnJawabC.background = blueRounded
                txtC.background = blueFill
            }
            redCode -> {
                btnJawabC.background = redRounded
                txtC.background = redFill
            }
        }
        when (d) {
            greenCode -> {
                btnJawabD.background = greenRounded
                txtD.background = greenFill
            }
            blueCode -> {
                btnJawabD.background = blueRounded
                txtD.background = blueFill
            }
            redCode -> {
                btnJawabD.background = redRounded
                txtD.background = redFill
            }
        }
        when (e) {
            greenCode -> {
                btnJawabE.background = greenRounded
                txtE.background = greenFill
            }
            blueCode -> {
                btnJawabE.background = blueRounded
                txtE.background = blueFill
            }
            redCode -> {
                btnJawabE.background = redRounded
                txtE.background = redFill
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAnswerDialogue(ans: Boolean) {
        when (ans) {
            true -> {
                if (Build.VERSION.SDK_INT < 24) {
                    containerDialogue.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_rounded_fill_green_10dp
                    )
                } else {
                    containerDialogue.background.setTint(green)

                }
                imgDialogue.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_check_24)
                txtDialogue.text = "Benar"
                layoutDialogue.visibility = View.VISIBLE
            }

            false -> {
                if (Build.VERSION.SDK_INT < 24) {
                    containerDialogue.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_rounded_fill_red_10dp
                    )

                } else {
                    containerDialogue.background.setTint(red)
                }
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
        private lateinit var layoutContent: ScrollView
        private lateinit var layoutStart: ConstraintLayout
        private lateinit var btnSubmit: Button
        private lateinit var btnTry: Button
        private lateinit var btnSee: Button
        private lateinit var txtSoal: TextView
        private lateinit var imgSoal: ImageView
        private lateinit var btnReset: Button

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

        private lateinit var layoutResult: ConstraintLayout
        private lateinit var txtResult: TextView

        private var blue = 1
        private var green = 1
        private var red = 1

        val greenCode = -13119143
        val blueCode = -12372015
        val redCode = -1486768

        private var documentNumber = 1
        private var docPath = ""
        private var realAnswer = "F"
        private var userAnswer = ""
        private var finalResult = 100
        private var isDone = false
        private var alrWrong = false
    }
}