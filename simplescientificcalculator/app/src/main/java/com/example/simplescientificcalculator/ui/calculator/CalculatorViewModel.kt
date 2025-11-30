package com.example.simplescientificcalculator.ui.calculator

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplescientificcalculator.domain.usecase.CalculateExpressionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the CalculatorScreen.
 * Manages the UI state (expression and result) and handles user interactions.
 * Uses `CalculateExpressionUseCase` to perform calculations.
 */
@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculateExpressionUseCase: CalculateExpressionUseCase
) : ViewModel() {

    // Mutable state for the current mathematical expression displayed on the calculator input.
    private val _expression = mutableStateOf("")
    val expression: State<String> = _expression

    // Mutable state for the calculated result displayed on the calculator output.
    private val _result = mutableStateOf("0")
    val result: State<String> = _result

    // Flag to indicate if the last operation was an equals, to clear the expression on next input.
    private var lastOperationWasEquals = false

    /**
     * Handles button clicks from the UI.
     * @param button The string representation of the button pressed (e.g., "1", "+", "C", "=").
     */
    fun onButtonClick(button: String) {
        when (button) {
            "C" -> clearAll()
            "DEL" -> deleteLastChar()
            "=" -> evaluateExpression()
            else -> appendToExpression(button)
        }
    }

    /**
     * Appends the given input to the current expression.
     * If the last operation was '=', clears the expression before appending.
     * Handles special cases for operators and functions to ensure valid input.
     */
    private fun appendToExpression(input: String) {
        if (lastOperationWasEquals) {
            _expression.value = ""
            _result.value = "0"
            lastOperationWasEquals = false
        }

        val currentExpression = _expression.value

        // Prevent multiple decimal points in a single number
        if (input == "." && (currentExpression.isEmpty() || currentExpression.last().isOperator() || currentExpression.endsWith("."))) {
            // If expression is empty or ends with operator, add "0." instead of "."
            _expression.value += "0."
            return
        } else if (input == "." && currentExpression.containsLastNumberDecimal()) {
            return // Already has a decimal in the current number
        }

        // Handle function inputs like sin, cos, etc.
        val functions = listOf("sin", "cos", "tan", "sqrt", "log", "ln")
        if (functions.contains(input)) {
            if (currentExpression.isNotEmpty() && currentExpression.last().isDigit()) {
                // If a digit precedes a function, assume multiplication
                _expression.value += "*"
            }
            _expression.value += "$input("
            return
        }

        // Handle operators: prevent multiple operators in a row, or operator at start
        if (input.isOperator()) {
            if (currentExpression.isEmpty()) {
                // Allow unary minus at the start
                if (input == "-") {
                    _expression.value += input
                }
                return
            } else if (currentExpression.last().isOperator()) {
                // Replace last operator if new one is pressed (e.g., 5+- -> 5-)
                _expression.value = currentExpression.dropLast(1) + input
                return
            }
        }

        _expression.value += input
    }

    /** Clears the entire expression and resets the result. */
    private fun clearAll() {
        _expression.value = ""
        _result.value = "0"
        lastOperationWasEquals = false
    }

    /** Deletes the last character from the expression. */
    private fun deleteLastChar() {
        if (lastOperationWasEquals) {
            clearAll()
            return
        }
        if (_expression.value.isNotEmpty()) {
            _expression.value = _expression.value.dropLast(1)
            if (_expression.value.isEmpty()) {
                _result.value = "0"
            }
        }
    }

    /** Evaluates the current expression using the `CalculateExpressionUseCase`. */
    private fun evaluateExpression() {
        if (_expression.value.isBlank()) {
            _result.value = "0"
            return
        }

        viewModelScope.launch {
            val currentExpression = _expression.value
            calculateExpressionUseCase(currentExpression)
                .onSuccess { result ->
                    _result.value = formatResult(result)
                    _expression.value = formatResult(result) // Set expression to result for chaining operations
                    lastOperationWasEquals = true
                }
                .onFailure { throwable ->
                    _result.value = "Error"
                    // Optionally, log the error or show a more specific message
                    println("Calculation error: ${throwable.message}")
                    lastOperationWasEquals = true // Still reset for next input
                }
        }
    }

    /** Formats a double result to a string, handling integer representation if no decimal part. */
    private fun formatResult(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            String.format("%.8f", value).trimEnd('0').trimEnd('.') // Limit decimals, remove trailing zeros
        }
    }

    /** Extension function to check if a character is an operator. */
    private fun Char.isOperator(): Boolean {
        return this == '+' || this == '-' || this == '*' || this == '/' || this == '^'
    }

    /** Extension function to check if the last number in the expression already contains a decimal point. */
    private fun String.containsLastNumberDecimal(): Boolean {
        if (this.isEmpty()) return false
        var i = this.length - 1
        while (i >= 0) {
            val char = this[i]
            if (char == '.') return true
            if (char.isOperator() || char == '(' || char == ')') return false
            i--
        }
        return false
    }
}