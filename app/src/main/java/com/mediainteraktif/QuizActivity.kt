package com.mediainteraktif

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mediainteraktif.ui.quiz.QuizFragment

class QuizActivity : AppCompatActivity() {
    private lateinit var quizFragment: QuizFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        quizFragment = QuizFragment()

        if (savedInstanceState != null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.quiz_fragment_content, quizFragment)
                .commit()
        }
    }
}