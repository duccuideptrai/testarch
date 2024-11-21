package com.example.testarch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testarch.ui.movie_detail.MovieDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_layout, MovieDetailFragment())
            commit()
        }
    }
}