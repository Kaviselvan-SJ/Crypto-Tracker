package com.kavi.cryptotracker.crypto.data.networking

import com.kavi.cryptotracker.core.data.networking.constructUrl
import com.kavi.cryptotracker.core.data.networking.safeCall
import com.kavi.cryptotracker.core.domain.util.NetworkError
import com.kavi.cryptotracker.crypto.data.networking.dto.CoinsResponseDto
import com.kavi.cryptotracker.crypto.domain.Coin
import com.kavi.cryptotracker.core.domain.util.Result
import com.kavi.cryptotracker.crypto.domain.CoinDataSource
import com.kavi.cryptotracker.core.domain.util.map
import com.kavi.cryptotracker.crypto.data.mappers.toCoin
import com.kavi.cryptotracker.crypto.data.mappers.toCoinPrice
import com.kavi.cryptotracker.crypto.data.networking.dto.CoinHistoryDto
import com.kavi.cryptotracker.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

class RemoteCoinDataSource(
    private val httpClient: HttpClient
): CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinsResponseDto> {
            httpClient.get(
                urlString = constructUrl("/assets")
            )
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        val startMillis = start
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()
        val endMillis = end
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()

        return safeCall<CoinHistoryDto> {
            httpClient.get(
                urlString = constructUrl("/assets/$coinId/history")
            ) {
                parameter("interval", "h6")
                parameter("start", startMillis)
                parameter("end", endMillis)
            }
        }.map { response ->
            response.data.map { it.toCoinPrice() }
        }
    }
}