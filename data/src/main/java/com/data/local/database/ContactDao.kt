package com.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.data.entities.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM ContactEntity")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM ContactEntity WHERE id IN (:id) LIMIT 1")
    fun getContactById(id: String): Flow<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg contactEntity: ContactEntity)

    @Query("SELECT * FROM ContactEntity WHERE name LIKE '%' || :searchQuery || '%' OR phone LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun getContactsWithSearchPagingSource(
        searchQuery: String
    ): PagingSource<Int,ContactEntity>

    @Query("SELECT COUNT(*) FROM ContactEntity")
    fun isContactDatabaseContainsData(): Boolean

}