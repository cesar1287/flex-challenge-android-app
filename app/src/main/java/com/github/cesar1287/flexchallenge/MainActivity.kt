package com.github.cesar1287.flexchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.cesar1287.flexchallenge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            timerView.setOnClickListener {
                timerView.start(5L)
            }
        }
    }
}