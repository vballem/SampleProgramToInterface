package com.example.unittestsdemo.amiibo

sealed class AmiiboIntent {
    data class FetchAmiibos(
        val name: String,
    ) : AmiiboIntent()
}
