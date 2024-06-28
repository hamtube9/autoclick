package com.autoclick.activity

import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autoclick.constant.Constants
import com.autoclick.databinding.ActivityMainBinding
import com.autoclick.service.FloatingViewService


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var floatingServiceIntent : Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnStart.setOnClickListener {

            val openApp =
                packageManager.getLaunchIntentForPackage(Constants.ANOTHER_APP_PACKAGE_NAME)
            if (openApp != null) {
                openApp.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(openApp)
            }

            try {
                floatingServiceIntent = Intent(this,FloatingViewService::class.java)
                startService(floatingServiceIntent)
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Error Open App",e.message?:"Error Open App")
            }


        }
    }


    override fun onDestroy() {
        stopService(floatingServiceIntent)
        super.onDestroy()
    }
}
