package com.example.wheresmycar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.wheresmycar.navigation.NavigationRoot
import com.example.wheresmycar.ui.theme.WheresMyCarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WheresMyCarTheme {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(
                        Color.Transparent.toArgb(),
                        Color.White.toArgb()
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        Color.White.toArgb(),
                        Color.White.toArgb()
                    )
                )

                NavigationRoot(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}