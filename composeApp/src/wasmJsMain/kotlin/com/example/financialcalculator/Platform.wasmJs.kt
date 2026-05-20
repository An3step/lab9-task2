package com.example.financialcalculator

actual val currentPlatform: PlatformType = PlatformType.WEB

actual fun Double.format(digits: Int): String =
    formatDouble(this, digits)

@JsFun("(value, digits) => value.toFixed(digits)")
private external fun formatDouble(value: Double, digits: Int): String
