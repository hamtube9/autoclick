package com.autoclick.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class AppAccessibilityService : AccessibilityService() {

    companion object {
        private var instance: AccessibilityService? = null

        private fun setService(service: AccessibilityService) {
            this.instance = service
        }
        val scheduler = Executors.newScheduledThreadPool(1)
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
    }

    override fun onInterrupt() {
    }

    override fun onServiceConnected() {
//        super.onServiceConnected()
        val info = serviceInfo
        info.packageNames = null
        setService(this)
        Log.e("AppAccessibilityService", ">>>>>>>Connected")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        return super.onUnbind(intent)
    }

    fun autoClick() {
        if (instance != null) {
            scheduler.scheduleAtFixedRate({
                spamClick()

            }, 0, 1, TimeUnit.SECONDS)
        } else {
            Toast.makeText(this, "NULL SERVICE", Toast.LENGTH_SHORT).show()
        }
    }


    fun  stopSpam(){
        if (instance != null) {
            scheduler.shutdown()
        } else {
            Toast.makeText(this, "NULL SERVICE", Toast.LENGTH_SHORT).show()
        }
    }

    private fun spamClick(){
        val root: AccessibilityNodeInfo = instance?.rootInActiveWindow ?: return
        root.let {

            val mention =
                root.findAccessibilityNodeInfosByViewId("com.ahamove.supplier:id/vgFooter")
            if (!(mention == null || mention.isEmpty())) {

                val btnAccept =
                    root.findAccessibilityNodeInfosByViewId(" com.ahamove.supplier:id/acceptBtn")
                if (!(btnAccept == null || btnAccept.isEmpty())) {
                    btnAccept[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }
                Thread.sleep(500)
            }else{
                val taskList =
                    root.findAccessibilityNodeInfosByViewId("com.ahamove.supplier:id/order_time_distance_fee")
                if (!(taskList == null || taskList.isEmpty())) {
                    taskList.first().parent.parent.parent.parent.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    Thread.sleep(500)

                }

                val cardBtnAccept =
                    root.findAccessibilityNodeInfosByViewId("com.ahamove.supplier:id/cardButton")

                if (!(cardBtnAccept == null || cardBtnAccept.isEmpty())) {
                    cardBtnAccept[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    Thread.sleep(500)
                }

                val btnConfirmAccept =
                    root.findAccessibilityNodeInfosByViewId("com.ahamove.supplier:id/md_buttonDefaultPositive")

                if (!(btnConfirmAccept == null || btnConfirmAccept.isEmpty())) {
                btnConfirmAccept[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }

            }
            autoClick()
        }
    }
}