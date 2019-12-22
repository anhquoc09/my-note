package com.haq.mynote.ui

import android.support.v4.app.Fragment
import com.haq.mynote.KeyboardUtils

abstract class BaseFragment : Fragment() {
    abstract fun onBackPressed(): Boolean
}