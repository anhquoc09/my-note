package com.haq.mynote

import android.support.annotation.DimenRes
import android.widget.Toast
import java.text.Normalizer

fun appContext() = MyNoteApp.instance.applicationContext

fun dip(value: Int): Int = (value * appContext().resources.displayMetrics.density).toInt()
fun dip(value: Float): Int = (value * appContext().resources.displayMetrics.density).toInt()
fun sp(value: Int): Int = (value * appContext().resources.displayMetrics.scaledDensity).toInt()
fun sp(value: Float): Int = (value * appContext().resources.displayMetrics.scaledDensity).toInt()
fun px2dip(px: Int): Float = px.toFloat() / appContext().resources.displayMetrics.density
fun px2sp(px: Int): Float = px.toFloat() / appContext().resources.displayMetrics.scaledDensity
fun dimen(@DimenRes resource: Int): Int = appContext().resources.getDimensionPixelSize(resource)

fun showToast(message: String?, duration: Int) {
    Toast.makeText(appContext(), message, duration).show()
}

fun showShortToast(fmt: String, vararg arguments: Any) {
    showToast(String.format(fmt, arguments), Toast.LENGTH_SHORT)
}

fun String.stripAccents(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        .replace("Đ".toRegex(), "D")
        .replace("đ".toRegex(), "d")

fun String.normalizeUnicode() = Normalizer.normalize(this, Normalizer.Form.NFC)
