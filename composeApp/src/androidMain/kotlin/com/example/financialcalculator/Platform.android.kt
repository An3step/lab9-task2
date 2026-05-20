package com.example.financialcalculator

actual val currentPlatform: PlatformType = PlatformType.ANDROID

actual fun Double.format(digits: Int): String = "%.${digits}f".format(this)
