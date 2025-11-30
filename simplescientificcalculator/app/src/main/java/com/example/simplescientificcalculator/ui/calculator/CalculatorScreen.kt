package com.example.simplescientificcalculator.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.simplescientificcalculator.ui.theme.SimpleScientificCalculatorTheme

/**
 * Composable function for the main calculator screen.
 * It displays the input expression, the result, and a grid of calculator buttons.
 * It interacts with the `CalculatorViewModel` to handle user input and update the UI state.
 */
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = hiltViewModel()
) {
    val expression = viewModel.expression.value
    val result = viewModel.result.value

    val buttonLayout = listOf(
        // Row 1: Scientific functions and Clear/Delete
        listOf("sin", "cos", "tan", "C", "DEL"),
        // Row 2: More scientific functions and basic operators
        listOf("sqrt", "log", "ln", "(", ")"),
        // Row 3: Numbers and multiplication/division
        listOf("7", "8", "9", "/", "^"),
        // Row 4: Numbers and addition/subtraction
        listOf("4", "5", "6", "*", "-"),
        // Row 5: Numbers, decimal, and equals
        listOf("1", "2", "3", "+", "="),
        listOf("0", ".") // Special row for 0 and .
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Display for the current expression
        Text(
            text = expression,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.End,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2
        )
        // Display for the result
        Text(
            text = result,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.End,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1
        )

        // Calculator button grid
        buttonLayout.forEachIndexed { rowIndex, rowButtons ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowButtons.forEach { buttonText ->
                    CalculatorButton(
                        text = buttonText,
                        modifier = Modifier
                            .weight(if (buttonText == "0") 2f else 1f) // Make '0' button wider
                            .aspectRatio(if (buttonText == "0") 2f else 1f),
                        onClick = { viewModel.onButtonClick(buttonText) },
                        buttonColor = getButtonColor(buttonText),
                        textColor = getTextColor(buttonText)
                    )
                }
            }
            if (rowIndex < buttonLayout.size - 1) {
                // Add spacing between rows, except after the last row
                RowSpacer(height = 8.dp)
            }
        }
    }
}

/**
 * Composable for a single calculator button.
 * @param text The text to display on the button.
 * @param modifier Modifier for the button.
 * @param onClick Lambda to be invoked when the button is clicked.
 * @param buttonColor The background color of the button.
 * @param textColor The color of the text on the button.
 */
@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Determines the background color for a calculator button based on its text.
 */
@Composable
private fun getButtonColor(buttonText: String): Color {
    return when (buttonText) {
        "C", "DEL" -> MaterialTheme.colorScheme.errorContainer
        "=", "+", "-", "*", "/", "^", "sin", "cos", "tan", "sqrt", "log", "ln", "(", ")" -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}

/**
 * Determines the text color for a calculator button based on its text.
 */
@Composable
private fun getTextColor(buttonText: String): Color {
    return when (buttonText) {
        "C", "DEL" -> MaterialTheme.colorScheme.onErrorContainer
        "=", "+", "-", "*", "/", "^", "sin", "cos", "tan", "sqrt", "log", "ln", "(", ")" -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

/**
 * Composable for adding vertical spacing between rows.
 */
@Composable
fun RowSpacer(height: Dp) {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.fillMaxWidth().padding(vertical = height / 2))
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    SimpleScientificCalculatorTheme {
        CalculatorScreen(viewModel = hiltViewModel()) // HiltViewModel will be mocked in preview
    }
}