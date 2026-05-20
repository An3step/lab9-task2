package com.example.task_9_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.financialcalculator.App
import com.example.task_9_2.ui.theme.Task_9_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task_9_2Theme {
                // Calling the shared UI from the multiplatform module
                App()
            }
        }
    }
}
