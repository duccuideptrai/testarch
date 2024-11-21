package com.example.testarch.ui.movie_detail.utils

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.testarch.R

fun Fragment.move2Fragment(fragment: Fragment, addToBackStack: Boolean = false) {
    val transaction = requireActivity().supportFragmentManager.beginTransaction()
    transaction.replace(R.id.main_layout, fragment)
    if (addToBackStack) {
        transaction.addToBackStack(null)
    }
    transaction.commit()
}

fun Fragment.move2FragmentByDialog(fragmentDialog: DialogFragment, addToBackStack: Boolean = false) {
    val transaction = childFragmentManager.beginTransaction()
    fragmentDialog.show(transaction, null)
}