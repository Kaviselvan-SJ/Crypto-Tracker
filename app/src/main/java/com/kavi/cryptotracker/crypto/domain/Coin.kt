package com.kavi.cryptotracker.crypto.domain

data class Coin(
    val id: String,
    val name: String,
    val rank: Int,
    val symbol: String,
    val marketCapUsd: Double,
    val priceUsd: Double,
    val changePercent24Hr: Double? = 0.0
)
