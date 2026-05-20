package com.example.financialcalculator

enum class Language {
    RU, EN, BE
}

data class AppStrings(
    val title: String,
    val initialAmount: String,
    val annualRate: String,
    val periodYears: String,
    val capitalization: String,
    val calculate: String,
    val totalAmount: String,
    val totalProfit: String,
    val monthly: String,
    val quarterly: String,
    val yearly: String,
    val error: String
)

val stringsRu = AppStrings(
    title = "Финансовый калькулятор",
    initialAmount = "Начальная сумма",
    annualRate = "Годовая ставка (%)",
    periodYears = "Срок (лет)",
    capitalization = "Капитализация",
    calculate = "Рассчитать",
    totalAmount = "Итоговая сумма",
    totalProfit = "Общая прибыль",
    monthly = "Ежемесячно",
    quarterly = "Ежеквартально",
    yearly = "Ежегодно",
    error = "Ошибка"
)

val stringsEn = AppStrings(
    title = "Financial Calculator",
    initialAmount = "Initial Amount",
    annualRate = "Annual Rate (%)",
    periodYears = "Period (Years)",
    capitalization = "Capitalization",
    calculate = "Calculate",
    totalAmount = "Total Amount",
    totalProfit = "Total Profit",
    monthly = "Monthly",
    quarterly = "Quarterly",
    yearly = "Yearly",
    error = "Error"
)

val stringsBe = AppStrings(
    title = "Фінансавы калькулятар",
    initialAmount = "Пачатковая сума",
    annualRate = "Гадавая стаўка (%)",
    periodYears = "Тэрмін (гадоў)",
    capitalization = "Капіталізацыя",
    calculate = "Разлічыць",
    totalAmount = "Выніковая сума",
    totalProfit = "Агульны прыбытак",
    monthly = "Штомесяц",
    quarterly = "Штоквартальна",
    yearly = "Штогод",
    error = "Памылка"
)

fun getStrings(lang: Language) = when(lang) {
    Language.RU -> stringsRu
    Language.EN -> stringsEn
    Language.BE -> stringsBe
}
