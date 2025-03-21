package com.kavi.cryptotracker.crypto.presentation.coin_list

import com.kavi.cryptotracker.core.domain.util.NetworkError


sealed interface CoinListEvent {
    data class Error(val error: NetworkError): CoinListEvent
}