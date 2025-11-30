package com.example.simplescientificcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.simplescientificcalculator.ui.calculator.CalculatorScreen
import com.example.simplescientificcalculator.ui.theme.SimpleScientificCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the Simple Scientific Calculator application.
 * This activity serves as the entry point for the UI, hosting the Jetpack Compose content.
 * Annotated with `@AndroidEntryPoint` to enable Hilt for injecting dependencies into this activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleScientificCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}