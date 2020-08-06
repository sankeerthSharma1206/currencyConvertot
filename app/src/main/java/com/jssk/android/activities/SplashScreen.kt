package com.jssk.android.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.jssk.android.R
import com.jssk.android.utils.PrefManager

class SplashScreen : AppCompatActivity() {
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startNavigation()
            finish()
        }, 1500)
    }

    private fun startNavigation() {
        if(PrefManager.getUserDTO() == null)
            startActivity(Intent(context, LoginActivity::class.java))
        else
            startActivity(Intent(context, MainActivity::class.java))
    }
}