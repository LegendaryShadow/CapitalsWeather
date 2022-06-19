package com.tkadela.capitalsweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * This activity only exists to contain the NavHostFragment.
 * All functionality is in the Fragments that are filled in to it.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}