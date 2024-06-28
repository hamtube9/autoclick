package com.autoclick.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.autoclick.R
import com.autoclick.constant.SharedPref


class SplashActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        SharedPref.init(this)

        checkDrawOverOtherApp()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_DRAW_OVER_OTHER_APP -> {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(
                        this,
                        "Need Permission REQUEST_DRAW_OVER_OTHER_APP",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                } else {
                    checkAccessibilityService()
                }
            }

            REQUEST_ACCESSIBILITY_SERVICE -> {
                if (!isAccessibilityEnable()) {
                    Toast.makeText(
                        this,
                        "Need Permission REQUEST_ACCESSIBILITY_SERVICE",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    navigateToMain()
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkDrawOverOtherApp() {
        if (Settings.canDrawOverlays(this)) {
            checkAccessibilityService()
            return
        }
        val intent =
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivityForResult(intent, REQUEST_DRAW_OVER_OTHER_APP)
    }

    private fun checkAccessibilityService() {
        if(isAccessibilityEnabled()){
            navigateToMain()
        }else{
            if (isAccessibilityEnable()) {
                navigateToMain()
            } else {
                openAccessibilityServiceSetting()
            }
        }

    }

    val LOGTAG = "NGOEC"

    private fun isAccessibilityEnabled(): Boolean {
        var accessibilityEnabled = 0
        val ACCESSIBILITY_SERVICE_NAME =
            "com.autoclick/com.autoclick.service.service.AppAccessibilityService"
        val accessibilityFound = false
        try {
            accessibilityEnabled =
                Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
            Log.d(LOGTAG, "ACCESSIBILITY: $accessibilityEnabled")
        } catch (e: SettingNotFoundException) {
            Log.d(LOGTAG, "Error finding setting, default accessibility to not found: " + e.message)
        }
        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            Log.d(LOGTAG, "***ACCESSIBILIY IS ENABLED***: ")
            val settingValue = Settings.Secure.getString(
                contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            Log.d(LOGTAG, "Setting: $settingValue")
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessabilityService = mStringColonSplitter.next()
                    Log.d(LOGTAG, "Setting: $accessabilityService")
                    if (accessabilityService.equals(
                            ACCESSIBILITY_SERVICE_NAME,
                            ignoreCase = true
                        )
                    ) {
                        Log.d(
                            LOGTAG,
                            "We've found the correct setting - accessibility is switched on!"
                        )
                        return true
                    }
                }
            }
            Log.d(LOGTAG, "***END***")
        } else {
            Log.d(LOGTAG, "***ACCESSIBILIY IS DISABLED***")
        }
        return accessibilityFound
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun openAccessibilityServiceSetting() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivityForResult(intent, REQUEST_ACCESSIBILITY_SERVICE)
    }

    private fun isAccessibilityEnable(): Boolean {
        val secure = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return secure.contains(getString(R.string.app_name))
    }

    companion object {
        const val REQUEST_DRAW_OVER_OTHER_APP = 99
        const val REQUEST_ACCESSIBILITY_SERVICE = 100
    }
}