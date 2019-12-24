package com.haq.mynote

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

fun appContext(): Context = MyNoteApp.instance.applicationContext

fun getString(@StringRes res: Int): String = appContext().getString(res)

fun showToast(message: String?, duration: Int) {
    Toast.makeText(appContext(), message, duration).show()
}

fun showShortToast(fmt: String, vararg arguments: Any) {
    showToast(String.format(fmt, arguments), Toast.LENGTH_SHORT)
}