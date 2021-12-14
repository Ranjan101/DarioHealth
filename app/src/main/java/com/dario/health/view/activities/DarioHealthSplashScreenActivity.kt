package com.dario.health.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import com.dario.health.R

class DarioHealthSplashScreenActivity : AppCompatActivity() {
    private var mHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) //will hide the title
        supportActionBar!!.hide()
        setContentView(R.layout.dario_health_splash_screen_activity)
        mHandler = Handler(Looper.getMainLooper());
        mHandler?.postDelayed(mRunnable, 3000);

    }


    var mRunnable = Runnable {
        val intent = Intent(this, DarioHealthMovieListMainActivity::class.java)
        startActivity(intent)
        finish()

    }


    @Override
    override fun onBackPressed() {
        mHandler?.removeCallbacks(mRunnable)
        super.onBackPressed();
    }

    override fun onDestroy() {
        mHandler?.removeCallbacks(mRunnable)
        super.onDestroy()
    }

}