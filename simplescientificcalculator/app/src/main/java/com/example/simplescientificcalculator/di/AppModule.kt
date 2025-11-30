package com.example.simplescientificcalculator.di

import com.example.simplescientificcalculator.data.repository.CalculatorRepository
import com.example.simplescientificcalculator.data.repository.CalculatorRepositoryImpl
import com.example.simplescientificcalculator.domain.usecase.CalculateExpressionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing application-wide dependencies.
 * This module is installed in the `SingletonComponent`, meaning all provided dependencies
 * will have a singleton lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton instance of `CalculatorRepository`.
     * In a real-world scenario, this might involve database or network dependencies.
     */
    @Provides
    @Singleton
    fun provideCalculatorRepository(): CalculatorRepository {
        return CalculatorRepositoryImpl()
    }

    /**
     * Provides a singleton instance of `CalculateExpressionUseCase`.
     * It depends on `CalculatorRepository` for its operations.
     */
    @Provides
    @Singleton
    fun provideCalculateExpressionUseCase(repository: CalculatorRepository): CalculateExpressionUseCase {
        return CalculateExpressionUseCase(repository)
    }
}