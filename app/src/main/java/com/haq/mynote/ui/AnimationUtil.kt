package com.haq.mynote.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


object AnimationUtil {
    private const val DURATION = 500L

    fun expand(v: View, duration: Long = DURATION) {
        v.animate()
            .translationXBy(v.width.toFloat())
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    v.visibility = View.VISIBLE
                }
            })
    }

    fun collapse(v: View, duration: Long = DURATION) {
        v.animate()
            .translationXBy(-v.width.toFloat())
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.GONE
                }
            })
    }
}
