package com.data.entities

import com.domain.entities.Temperament
import com.squareup.moshi.Json

data class ContactDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "height") val height: Float,
    @Json(name = "biography") val biography: String,
    @Json(name = "temperament") val temperament: String,
    @Json(name = "educationPeriod") val educationPeriod: EducationPeriod
)

data class EducationPeriod(
    @Json(name = "start") val start: String,
    @Json(name = "end") val end: String
)

fun ContactDto.toContactEntity(): ContactEntity = ContactEntity(
    id = id,
    name = name,
    phone = phone.filter { it.isDigit() },
    height = height,
    biography = biography,
    temperament = when (temperament) {
        "melancholic" -> Temperament.Melancholic
        "phlegmatic" -> Temperament.Phlegmatic
        "sanguine" -> Temperament.Sanguine
        "choleric" -> Temperament.Choleric
        else -> {
            Temperament.Melancholic
        }
    },
    educationPeriod = educationPeriod.start.take(10) + " - " + educationPeriod.end.take(10)
)