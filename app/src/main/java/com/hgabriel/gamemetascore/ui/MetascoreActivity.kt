package com.hgabriel.gamemetascore.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.databinding.ActivityMetascoreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MetascoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMetascoreBinding>(this, R.layout.activity_metascore)
    }
}
