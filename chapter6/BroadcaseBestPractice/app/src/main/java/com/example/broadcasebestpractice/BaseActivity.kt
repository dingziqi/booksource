package com.example.broadcasebestpractice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {

    lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        ActivityCollector.addActivity(this)
    }

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter("com.example.broadcastbestpractice.FORCE_OFFLINE")
        receiver = ForceOfflineReceiver()
        registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        super.onDestroy()

        ActivityCollector.removeActivity(this)
    }

    inner class ForceOfflineReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context, p1: Intent?) {
            AlertDialog.Builder(context).apply {
                setTitle("Warning")

                setMessage("You are forced to be offline.Please try to login again.")
                setCancelable(false)

                setPositiveButton("OK") {_, _ ->
                    ActivityCollector.finishAll()
                    val i = Intent(context, LoginActivity::class.java)
                    context.startActivity(i)
                }

                show()
            }
        }
    }
}