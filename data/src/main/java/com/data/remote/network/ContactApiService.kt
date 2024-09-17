package com.data.remote.network

import com.data.entities.ContactDto
import retrofit2.http.GET

interface ContactApiService {

    @GET("generated-01.json")
    suspend fun getContactsFromSourceOne(): NetworkResult<List<ContactDto>>

    @GET("generated-02.json")
    suspend fun getContactsFromSourceTwo(): NetworkResult<List<ContactDto>>

    @GET("generated-03.json")
    suspend fun getContactsFromSourceThree(): NetworkResult<List<ContactDto>>

}