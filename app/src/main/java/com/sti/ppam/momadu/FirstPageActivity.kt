package com.sti.ppam.momadu

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler
import android.os.Looper

class FirstPageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.firstpage)
    }

    override fun onStart() {
        super.onStart()

        Looper.myLooper()?.let { Handler(it) }?.postDelayed(kotlinx.coroutines.Runnable {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish();
        }, 3000)
    }

}