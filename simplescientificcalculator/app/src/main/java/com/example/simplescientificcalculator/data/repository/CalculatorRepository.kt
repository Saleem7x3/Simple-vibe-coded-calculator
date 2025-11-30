package com.example.simplescientificcalculator.data.repository

/**
 * Interface for the Calculator data layer.
 * Defines the contract for operations related to calculator functionality.
 * In a more complex app, this might involve fetching calculation history from a database
 * or remote server. For this simple calculator, it primarily acts as an abstraction
 * layer for the business logic to interact with.
 */
interface CalculatorRepository {
    /**
     * Processes a mathematical expression string.
     * @param expression The mathematical expression to process.
     * @return A `Result` object containing either the calculated `Double` result on success,
     *         or a `Throwable` on failure (e.g., invalid expression, division by zero).
     */
    fun processExpression(expression: String): Result<Double>
}