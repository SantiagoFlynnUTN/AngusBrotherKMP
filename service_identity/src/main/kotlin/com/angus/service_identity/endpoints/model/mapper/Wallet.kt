package com.angus.service_identity.endpoints.model.mapper

import com.angus.service_identity.domain.entity.Wallet
import com.angus.service_identity.endpoints.model.WalletDto


fun Wallet.toDto() = WalletDto(id = id, userId = userId, walletBalance = walletBalance,currency=currency)