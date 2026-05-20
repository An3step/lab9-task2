package com.example.financialcalculator

enum class PlatformType {
    ANDROID, IOS, WINDOWS, WEB
}

expect val currentPlatform: PlatformType

expect fun Double.format(digits: Int): String
