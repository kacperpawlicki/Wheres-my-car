package com.example.wheresmycar.ui.main_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreenUi(
    modifier: Modifier
) {
    Scaffold(
        modifier = modifier
    ){ innerPadding ->
        Map(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}