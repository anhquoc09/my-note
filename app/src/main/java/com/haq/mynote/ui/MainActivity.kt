package com.haq.mynote.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.haq.mynote.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host)
        navHostFragment?.childFragmentManager?.fragments?.forEach {
            if (it.isVisible && it.userVisibleHint && (it as? BaseFragment)?.onBackPressed() == true) {
                return
            }
        }

        super.onBackPressed()
    }
}
