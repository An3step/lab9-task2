package com.example.financialcalculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CalculatorLogicTest {

    @Test
    fun testCalculationMonthly() {
        val result = CalculatorLogic.calculate(1000.0, 12.0, 1, CapitalizationFrequency.MONTHLY)
        assertEquals(1126.83, result.totalAmount, 0.01)
    }

    @Test
    fun testCalculationYearly() {
        val result = CalculatorLogic.calculate(1000.0, 10.0, 2, CapitalizationFrequency.YEARLY)
        assertEquals(1210.0, result.totalAmount, 0.01)
    }

    @Test
    fun testValidationValid() {
        assertNull(CalculatorLogic.validateInput("1000", "5", "5"))
    }

    @Test
    fun testValidationInvalidAmount() {
        assertNotNull(CalculatorLogic.validateInput("-100", "5", "5"))
    }

    @Test
    fun testValidationZeroYears() {
        assertNotNull(CalculatorLogic.validateInput("1000", "5", "0"))
    }

    @Test
    fun testValidationEmptyFields() {
        assertNotNull(CalculatorLogic.validateInput("", "", ""))
    }

    @Test
    fun testGrowthDataSize() {
        val result = CalculatorLogic.calculate(1000.0, 5.0, 10, CapitalizationFrequency.YEARLY)
        assertEquals(11, result.growthData.size) // 0 to 10 years
    }
}
