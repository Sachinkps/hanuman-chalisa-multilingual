package com.sachinkps.hanumanChalisa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sachinkps.hanumanChalisa.ui.screens.HomeScreen
import com.sachinkps.hanumanChalisa.ui.theme.HanumanChalisaTheme
import com.sachinkps.hanumanChalisa.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HanumanChalisaTheme {
                val viewModel: MainViewModel = viewModel()
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}
