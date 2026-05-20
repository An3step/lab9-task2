package com.example.financialcalculator

import kotlin.math.pow

data class CalculationResult(
    val totalAmount: Double,
    val totalProfit: Double,
    val growthData: List<Pair<Int, Double>>
)

enum class CapitalizationFrequency(val months: Int) {
    MONTHLY(1),
    QUARTERLY(3),
    YEARLY(12)
}

object CalculatorLogic {
    fun calculate(
        initialAmount: Double,
        annualRate: Double,
        years: Int,
        frequency: CapitalizationFrequency
    ): CalculationResult {
        val n = 12 / frequency.months // times per year
        val r = annualRate / 100.0
        val t = years.toDouble()
        
        val totalAmount = initialAmount * (1 + r / n).pow(n * t)
        val totalProfit = totalAmount - initialAmount
        
        val growthData = mutableListOf<Pair<Int, Double>>()
        for (year in 0..years) {
            val amountAtYear = initialAmount * (1 + r / n).pow(n * year.toDouble())
            growthData.add(year to amountAtYear)
        }
        
        return CalculationResult(totalAmount, totalProfit, growthData)
    }
    
    fun validateInput(amount: String, rate: String, years: String): String? {
        val a = amount.toDoubleOrNull() ?: return "Invalid initial amount"
        val r = rate.toDoubleOrNull() ?: return "Invalid annual rate"
        val y = years.toIntOrNull() ?: return "Invalid period"
        
        if (a <= 0) return "Amount must be positive"
        if (r < 0) return "Rate cannot be negative"
        if (y <= 0) return "Period must be at least 1 year"
        
        return null
    }
}
