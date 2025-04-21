package com.example.unittestsdemo.amiibo

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

fun interface GetAmiibosUseCase {
    operator fun invoke(name: String): Flow<List<Amiibo>>
}

class GetAmiibosUseCaseImpl
@Inject
constructor(
    private val repository: AmiibosRepository,
) : GetAmiibosUseCase {

    override operator fun invoke(name: String): Flow<List<Amiibo>> {
        return repository.getAmiibos(name)
    }

}
