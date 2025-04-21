package com.example.unittestsdemo.amiibo

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

fun interface AmiiboService {
    @GET("api/amiibo/")
    suspend fun getAmiibos(
        @Query("name") name: String,
    ): Response<Amiibos>
}
