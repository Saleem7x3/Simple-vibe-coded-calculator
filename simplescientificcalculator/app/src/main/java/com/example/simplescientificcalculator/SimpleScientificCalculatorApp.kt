package com.example.simplescientificcalculator

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for the SimpleScientificCalculator app.
 * Annotated with `@HiltAndroidApp` to enable Hilt for dependency injection.
 */
@HiltAndroidApp
class SimpleScientificCalculatorApp : Application()