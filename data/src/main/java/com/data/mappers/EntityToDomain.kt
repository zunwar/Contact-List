package com.data.mappers

import com.data.entities.ContactEntity
import com.domain.entities.Contact

internal fun ContactEntity.toDomain(): Contact = Contact(
    id = id,
    name = name,
    phone = phone.let {
        "+${it[0]} (${it.substring(1, 4)}) ${it.substring(4, 7)}-${it.substring(7, 11)} "
    },
    height = height,
    biography = biography,
    temperament = temperament,
    educationPeriod = educationPeriod
)



