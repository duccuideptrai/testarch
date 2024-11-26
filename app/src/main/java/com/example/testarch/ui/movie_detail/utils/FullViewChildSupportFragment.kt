package com.example.testarch.ui.movie_detail.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.testarch.R

abstract class FullViewChildSupportFragment: Fragment() {
    fun move2ChildFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.main_layout, fragment)
        transaction.addToBackStack(fragment::class.simpleName)
        transaction.commit()
    }

    fun move2Fragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_layout, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun move2FragmentByDialog(fragmentDialog: DialogFragment, addToBackStack: Boolean = false) {
        val transaction = childFragmentManager.beginTransaction()
        fragmentDialog.show(transaction, null)
    }

    inline fun <reified T: Any> findSupportParentFragment(): Fragment {
        var currentParentFragment = parentFragment

        while (currentParentFragment!= null) {
            if (currentParentFragment is T) {
                return currentParentFragment
            }
            currentParentFragment = currentParentFragment.parentFragment
        }
        throw IllegalStateException("No parent fragment found for interface")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_main, container, false)
        view.findViewById<ViewGroup>(R.id.main_layout).addView(applyMainView())
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Remove all fragments from the childFragmentManager,
                // but exclude the first added child fragment.
                // This child fragment will be deleted with its parent.
                if (childFragmentManager.backStackEntryCount > 1) {
                    childFragmentManager.popBackStack()
                    return
                }
                if (parentFragment != null) {
                    // Delete parent fragment
                    parentFragmentManager.popBackStack()
                } else {
                    val fragmentManager = requireActivity().supportFragmentManager
                    if (fragmentManager.backStackEntryCount > 0) {
                        fragmentManager.popBackStack()
                    } else {
                        requireActivity().finish()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
    }

    abstract fun applyMainView(): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyFragment", "onCreate")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("MyFragment", "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("MyFragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MyFragment", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MyFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MyFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MyFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MyFragment", "onDetach")
    }
}
