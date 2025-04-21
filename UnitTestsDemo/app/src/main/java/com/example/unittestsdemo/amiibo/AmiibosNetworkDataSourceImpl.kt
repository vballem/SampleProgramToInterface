package com.example.unittestsdemo.amiibo

import javax.inject.Inject
import javax.inject.Singleton

fun interface AmiibosNetworkDataSource {
    suspend fun getAmiibos(name: String): List<Amiibo>
}

@Singleton
class AmiibosNetworkDataSourceImpl
@Inject
constructor(
    private val amiiboService: AmiiboService,
) : AmiibosNetworkDataSource {

    override suspend fun getAmiibos(name: String): List<Amiibo> {
        val response = amiiboService.getAmiibos(name)
        return if (response.isSuccessful) {
            response.body()!!.amiibo ?: emptyList()
        } else {
            throw RuntimeException(response.errorBody()!!.bytes().toString())
        }
    }
}
