package com.example.testarch.ui.movie_detail.utils

import android.util.Log
import androidx.lifecycle.ViewModel

open class TraceableViewModel: ViewModel() {
    override fun onCleared() {
        super.onCleared()

        Log.d("MyFragment", "ViewModel onCleared")
    }
}