package com.domain.entities

data class Contact(
    val id: String,
    val name: String,
    val phone: String,
    val height: Float,
    val biography: String,
    val temperament: Temperament,
    val educationPeriod: String
)

enum class Temperament {
    Melancholic,
    Phlegmatic,
    Sanguine,
    Choleric
}

@JvmInline
value class NameOrPhoneQuery(val string: String)

val NoQuery = NameOrPhoneQuery("")

