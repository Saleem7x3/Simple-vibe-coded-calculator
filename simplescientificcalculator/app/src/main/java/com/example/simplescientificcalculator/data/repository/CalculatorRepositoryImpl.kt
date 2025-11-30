package com.example.simplescientificcalculator.data.repository

import kotlin.math.*

/**
 * Implementation of the `CalculatorRepository` interface.
 * This class contains the core logic for parsing and evaluating mathematical expressions.
 * It uses a recursive descent parser approach to handle operator precedence, parentheses,
 * numbers, and scientific functions.
 */
class CalculatorRepositoryImpl : CalculatorRepository {

    override fun processExpression(expression: String): Result<Double> {
        return try {
            val evaluator = ExpressionEvaluator(expression)
            Result.success(evaluator.evaluate())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Internal class responsible for parsing and evaluating a mathematical expression string.
     * Implements a recursive descent parser.
     */
    private class ExpressionEvaluator(private var expression: String) {
        private var pos: Int = -1
        private var char: Char = ' '

        init {
            expression = expression.replace(" ", "") // Remove all spaces
            nextChar()
        }

        /** Moves to the next character in the expression string. */
        private fun nextChar() {
            pos++
            char = if (pos < expression.length) expression[pos] else (-1).toChar() // -1 indicates end of string
        }

        /**
         * Attempts to consume a specific character.
         * @param charToEat The character to consume.
         * @return `true` if the character was consumed, `false` otherwise.
         */
        private fun eat(charToEat: Char): Boolean {
            while (char == ' ') nextChar() // Skip whitespace (though already removed)
            if (char == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        /**
         * Evaluates the entire expression.
         * @return The calculated double value.
         * @throws RuntimeException if the expression is invalid or contains unexpected characters.
         */
        fun evaluate(): Double {
            val result = parseExpression()
            if (pos < expression.length) throw RuntimeException("Unexpected character: '" + char + "' at position " + pos)
            return result
        }

        /**
         * Parses an expression (handles addition and subtraction).
         * Grammar: `expression = term | expression '+' term | expression '-' term`
         */
        private fun parseExpression(): Double {
            var x = parseTerm()
            while (true) {
                if (eat('+')) x += parseTerm() // addition
                else if (eat('-')) x -= parseTerm() // subtraction
                else return x
            }
        }

        /**
         * Parses a term (handles multiplication and division).
         * Grammar: `term = factor | term '*' factor | term '/' factor`
         */
        private fun parseTerm(): Double {
            var x = parseFactor()
            while (true) {
                if (eat('*')) x *= parseFactor() // multiplication
                else if (eat('/')) {
                    val divisor = parseFactor()
                    if (divisor == 0.0) throw ArithmeticException("Division by zero")
                    x /= divisor // division
                }
                else return x
            }
        }

        /**
         * Parses a factor (handles numbers, parentheses, functions, unary plus/minus, and power).
         * Grammar: `factor = power | factor '^' power`
         * `power = number | '(' expression ')' | functionName '(' expression ')' | '-' factor | '+' factor`
         * `functionName = "sin" | "cos" | "tan" | "sqrt" | "log" | "ln"`
         */
        private fun parseFactor(): Double {
            if (eat('+')) return parseFactor() // unary plus
            if (eat('-')) return -parseFactor() // unary minus

            var x: Double
            val startPos = pos

            if (eat('(')) { // parentheses
                x = parseExpression()
                if (!eat(')')) throw RuntimeException("Missing ')' at position " + pos)
            } else if (char.isDigit() || char == '.') { // numbers
                while (char.isDigit() || char == '.') nextChar()
                val numStr = expression.substring(startPos, pos)
                x = numStr.toDoubleOrNull() ?: throw RuntimeException("Invalid number format: $numStr")
            } else if (char.isLetter()) { // functions
                while (char.isLetter()) nextChar()
                val func = expression.substring(startPos, pos)
                x = parseFactor() // argument of function
                when (func) {
                    "sqrt" -> {
                        if (x < 0) throw IllegalArgumentException("Cannot take square root of a negative number")
                        x = sqrt(x)
                    }
                    "sin" -> x = sin(Math.toRadians(x)) // Assume degrees for simplicity
                    "cos" -> x = cos(Math.toRadians(x))
                    "tan" -> x = tan(Math.toRadians(x))
                    "log" -> {
                        if (x <= 0) throw IllegalArgumentException("Logarithm of non-positive number")
                        x = log10(x) // Base 10
                    }
                    "ln" -> {
                        if (x <= 0) throw IllegalArgumentException("Natural logarithm of non-positive number")
                        x = ln(x) // Natural log
                    }
                    else -> throw RuntimeException("Unknown function: $func at position $startPos")
                }
            } else {
                throw RuntimeException("Unexpected character: '" + char + "' at position " + pos)
            }

            // Handle power operator (^) after parsing the base factor
            if (eat('^')) {
                x = x.pow(parseFactor())
            }

            return x
        }
    }
}