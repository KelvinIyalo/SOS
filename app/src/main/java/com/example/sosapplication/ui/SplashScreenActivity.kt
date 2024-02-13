package com.example.sosapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.sosapplication.MainActivity
import com.example.sosapplication.R
import com.example.sosapplication.utils.SharedPreferencesHelper
import com.example.sosapplication.utils.Utility

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val intent: Intent
            // Start your main activity here
            if (SharedPreferencesHelper.isSharedPreferencesEmpty(this)) {
                intent = Intent(this, SetupActivity::class.java)
            } else {
                intent = Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 500)
    }
}