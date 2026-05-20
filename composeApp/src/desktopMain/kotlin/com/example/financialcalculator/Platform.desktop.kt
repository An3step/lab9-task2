package com.example.financialcalculator

actual val currentPlatform: PlatformType = PlatformType.WINDOWS

actual fun Double.format(digits: Int): String = "%.${digits}f".format(this)
