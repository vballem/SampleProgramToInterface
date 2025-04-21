package com.example.unittestsdemo.amiibo

sealed class AmiiboState {
    data object Loading : AmiiboState()

    data class Success(
        val data: List<Amiibo>,
    ) : AmiiboState()

    data object Error : AmiiboState()
}
