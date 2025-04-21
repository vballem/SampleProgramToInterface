package com.example.unittestsdemo.amiibo

import com.example.unittestsdemo.amiibo.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

fun interface AmiibosRepository {
    fun getAmiibos(name: String): Flow<List<Amiibo>>
}

class AmiibosRepositoryImpl @Inject constructor(
    private val dataSource: AmiibosNetworkDataSource,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : AmiibosRepository {

    override fun getAmiibos(name: String) =
        flow {
            emit(dataSource.getAmiibos(name))
        }.flowOn(dispatcher)

}
