package com.example.financialcalculator

import androidx.compose.ui.test.*
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FinancialCalculatorUITest {

    @Test
    fun testTitleIsDisplayed() = runComposeUiTest {
        setContent { App() }
        // We look for the main part of the title that remains similar or use a more flexible matcher
        onNode(hasText("калькулятор", substring = true) or hasText("Calculator", substring = true))
            .assertIsDisplayed()
    }

    @Test
    fun testLanguageSwitchingRUtoEN() = runComposeUiTest {
        setContent { App() }
        onNodeWithText("EN").performClick()
        onNode(hasText("Calculator", substring = true)).assertIsDisplayed()
    }

    @Test
    fun testCalculateFlow() = runComposeUiTest {
        setContent { App() }
        
        // Use a matcher that works for both "Рассчитать" and "Calculate"
        onNode(hasText("Рассчитать", substring = true) or hasText("Calculate", substring = true))
            .performClick()
        
        // Verify results appear by checking for specific result labels to avoid ambiguity
        // with input labels (like "Начальная сумма") or history entries (which use "Amount:").
        onNode(hasText("Итоговая сумма", substring = true) or hasText("Total Amount", substring = true))
            .assertIsDisplayed()
            
        onNode(hasText("Общая прибыль", substring = true) or hasText("Total Profit", substring = true))
            .assertIsDisplayed()
    }

    @Test
    fun testHistoryUpdateInUI() = runComposeUiTest {
        setContent { App() }
        onNode(hasText("Рассчитать", substring = true) or hasText("Calculate", substring = true))
            .performClick()

        // History is at the bottom of the scrollable column, so we must scroll to it
        onNode(hasText("History", substring = true))
            .performScrollTo()
            .assertIsDisplayed()
    }
}
