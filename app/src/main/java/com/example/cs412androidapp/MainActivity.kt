package com.example.cs412androidapp

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private lateinit var broadcastReceiver: BroadcastReceiver
    private var receiverRegistered = false

    private var myService: MyService? = null
    private var isBound = false

    private var grade by mutableStateOf("")

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            val binder = service as MyService.LocalBinder

            myService = binder.getService()
            isBound = true

            grade = myService?.getMyGrade() ?: ""
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            myService = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission()

        broadcastReceiver = MyBroadcastReceiver()

        setContent {

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "CS412 Android App"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    onClick = {
                        startMyService()
                    }
                ) {
                    Text("Start Service")
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        onClick = {
                            bindMyService()
                        }
                    ) {
                        Text("Bind Service")
                    }

                    if (grade.isNotEmpty()) {
                        Text(
                            text = "   Grade: $grade"
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {
                        sendMyBroadcast()
                    }
                ) {
                    Text("Send Broadcast")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (!receiverRegistered) {
            val filter = IntentFilter(MY_ACTION)

            ContextCompat.registerReceiver(
                this,
                broadcastReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )

            receiverRegistered = true
        }
    }

    override fun onStop() {
        super.onStop()

        if (receiverRegistered) {
            unregisterReceiver(broadcastReceiver)
            receiverRegistered = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

    private fun startMyService() {

        val intent = Intent(
            this,
            MyService::class.java
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun bindMyService() {

        val intent = Intent(
            this,
            MyService::class.java
        )

        bindService(
            intent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun sendMyBroadcast() {

        val intent = Intent(MY_ACTION)

        intent.setPackage(packageName)

        sendBroadcast(intent)
    }

    private fun requestNotificationPermission() {

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    companion object {
        const val MY_ACTION =
            "com.example.cs412androidapp.MY_ACTION"
    }
}