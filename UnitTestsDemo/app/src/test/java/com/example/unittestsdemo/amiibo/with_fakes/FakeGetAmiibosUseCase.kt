package com.example.unittestsdemo.amiibo.with_fakes

import com.example.unittestsdemo.amiibo.GetAmiibosUseCase
import com.example.unittestsdemo.amiibo.Amiibo
import kotlinx.coroutines.flow.flow

class FakeGetAmiibosUseCase : GetAmiibosUseCase {
    private var amiiboList: List<Amiibo> = emptyList()
    private var shouldThrowError: Boolean = false

    fun setAmiiboList(amiibos: List<Amiibo>) {
        amiiboList = amiibos
    }

    fun setShouldThrowError(shouldThrow: Boolean) {
        shouldThrowError = shouldThrow
    }

    override fun invoke(name: String) = flow {
        if (shouldThrowError) {
            throw Exception("Error")
        } else {
            emit(amiiboList)
        }
    }
}