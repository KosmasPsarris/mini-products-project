package com.kosmas.tecprin.ui.main

import android.os.Bundle
import com.kosmas.tecprin.R
import com.kosmas.tecprin.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
