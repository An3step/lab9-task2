package com.example.financialcalculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IntegrationTest {

    @Test
    fun testFullCalculationFlow() {
        val amountStr = "5000"
        val rateStr = "10"
        val yearsStr = "3"
        val frequency = CapitalizationFrequency.QUARTERLY
        
        val validationError = CalculatorLogic.validateInput(amountStr, rateStr, yearsStr)
        assertNull(validationError)
        
        val result = CalculatorLogic.calculate(
            amountStr.toDouble(),
            rateStr.toDouble(),
            yearsStr.toInt(),
            frequency
        )
        
        assertEquals(6724.44, result.totalAmount, 0.01)
        assertEquals(3, result.growthData.last().first)
        assertEquals(4, result.growthData.size) // 0, 1, 2, 3
    }

    @Test
    fun testLanguageStringsConsistency() {
        val languages = listOf(Language.RU, Language.EN, Language.BE)
        languages.forEach { lang ->
            val strings = getStrings(lang)
            assertTrue(strings.title.isNotEmpty())
            assertTrue(strings.calculate.isNotEmpty())
        }
    }

    @Test
    fun testHistoryUpdateSimulation() {
        val history = mutableListOf<CalculationResult>()
        
        val res1 = CalculatorLogic.calculate(1000.0, 5.0, 1, CapitalizationFrequency.YEARLY)
        history.add(res1)
        
        val res2 = CalculatorLogic.calculate(2000.0, 10.0, 2, CapitalizationFrequency.MONTHLY)
        history.add(res2)
        
        assertEquals(2, history.size)
        assertEquals(1050.0, history[0].totalAmount, 0.01)
        assertTrue(history[1].totalAmount > 2000.0)
    }
    
    @Test
    fun testBoundaryConditions() {
        // High values
        val res = CalculatorLogic.calculate(1000000.0, 20.0, 50, CapitalizationFrequency.MONTHLY)
        assertTrue(res.totalAmount > 1000000.0)
        
        // Minimum valid values
        val resMin = CalculatorLogic.calculate(1.0, 0.1, 1, CapitalizationFrequency.YEARLY)
        assertEquals(1.001, resMin.totalAmount, 0.001)
    }

    @Test
    fun testValidationFailureFlow() {
        val error = CalculatorLogic.validateInput("abc", "5", "5")
        assertEquals("Invalid initial amount", error)
    }

    @Test
    fun testCapitalizationComparison() {
        val initial = 1000.0
        val rate = 10.0
        val years = 1
        
        val monthly = CalculatorLogic.calculate(initial, rate, years, CapitalizationFrequency.MONTHLY)
        val yearly = CalculatorLogic.calculate(initial, rate, years, CapitalizationFrequency.YEARLY)
        
        assertTrue(monthly.totalAmount > yearly.totalAmount, "Monthly capitalization should yield more than yearly")
    }
}
