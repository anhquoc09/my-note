package com.haq.mynote.ui

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract fun onBackPressed(): Boolean
}