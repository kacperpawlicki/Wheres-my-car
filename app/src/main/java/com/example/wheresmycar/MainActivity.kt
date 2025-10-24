package com.example.wheresmycar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.wheresmycar.navigation.NavigationRoot
import com.example.wheresmycar.ui.theme.WheresMyCarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WheresMyCarTheme {
                NavigationRoot(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}