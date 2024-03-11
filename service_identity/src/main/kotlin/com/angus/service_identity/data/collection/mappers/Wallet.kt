package com.angus.service_identity.data.collection.mappers

import com.angus.service_identity.data.collection.WalletCollection
import com.angus.service_identity.domain.entity.Wallet


fun WalletCollection.toEntity() = Wallet(
    id = id.toString(),
    userId = userId.toString(),
    walletBalance = walletBalance,
    currency = currency
)

