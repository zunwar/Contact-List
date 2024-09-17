package com.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.domain.entities.Temperament

@Entity
data class ContactEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val phone: String,
    @ColumnInfo val height: Float,
    @ColumnInfo val biography: String,
    @ColumnInfo val temperament: Temperament,
    @ColumnInfo val educationPeriod: String
)