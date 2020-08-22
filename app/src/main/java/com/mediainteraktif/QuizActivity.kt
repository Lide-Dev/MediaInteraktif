package com.mediainteraktif

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mediainteraktif.ui.quiz.QuizFragmentNav

class QuizActivity : AppCompatActivity() {
    private lateinit var quizFragmentNav: QuizFragmentNav

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        quizFragmentNav = QuizFragmentNav()

        if (savedInstanceState != null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.quiz_fragment_content, quizFragmentNav)
                .commit()
        }
    }
}