package com.example.cs412androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SecondActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {

                val challenges = listOf(
                    "Device and Android version fragmentation",
                    "Limited battery life and power consumption",
                    "Network connectivity and reliability",
                    "Security and user privacy",
                    "Supporting different screen sizes and resolutions"
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Mobile Software Engineering Challenges",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    challenges.forEach { challenge ->
                        Text(
                            text = "• $challenge",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            finish()
                        }
                    ) {
                        Text("Main Activity")
                    }
                }
            }
        }
    }
}