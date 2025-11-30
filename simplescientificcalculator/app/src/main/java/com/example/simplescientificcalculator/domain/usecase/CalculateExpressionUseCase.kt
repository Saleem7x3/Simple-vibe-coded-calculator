package com.example.simplescientificcalculator.domain.usecase

import com.example.simplescientificcalculator.data.repository.CalculatorRepository
import javax.inject.Inject

/**
 * Use case responsible for calculating the result of a mathematical expression.
 * This class orchestrates the business logic, delegating the actual processing
 * to the `CalculatorRepository` and handling the result.
 */
class CalculateExpressionUseCase @Inject constructor(
    private val repository: CalculatorRepository
) {

    /**
     * Executes the calculation for the given expression.
     * @param expression The mathematical expression string to evaluate.
     * @return A `Result` object containing either the calculated `Double` result on success,
     *         or a `Throwable` on failure (e.g., invalid expression, division by zero).
     */
    operator fun invoke(expression: String): Result<Double> {
        if (expression.isBlank()) {
            return Result.failure(IllegalArgumentException("Expression cannot be empty"))
        }
        return repository.processExpression(expression)
    }
}