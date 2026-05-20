package com.example.financialcalculator

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual val currentPlatform: PlatformType = PlatformType.IOS

actual fun Double.format(digits: Int): String {
    val formatter = NSNumberFormatter()
    formatter.minimumFractionDigits = digits.toULong()
    formatter.maximumFractionDigits = digits.toULong()
    formatter.numberStyle = NSNumberFormatterDecimalStyle
    return formatter.stringFromNumber(NSNumber(this)) ?: ""
}
