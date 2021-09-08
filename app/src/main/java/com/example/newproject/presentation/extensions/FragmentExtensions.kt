package com.example.newproject.presentation.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment

/**
 * Created by Nimish Nandwana on 01/03/2020.
 */
/**
 * Extension method to provide hide keyboard for [Fragment].
 */
fun Fragment.hideSoftKeyboard() {
    activity?.apply {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService<InputMethodManager>()
            inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showToast(messageResId: Int, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, messageResId, duration).show()
}

/**
 * Extension method used to display a [showToast] message to the user.
 */
fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    context?.showToast(message, duration)
}

/**
 * Extension method used to display a [showToast] message to the user.
 */
fun Fragment.showToast(messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    context?.showToast(messageResId, duration)
}
