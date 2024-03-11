package com.angus.common.domain.entity

import com.angus.common.domain.util.TaxiStatus

data class TaxiFiltration(
    val carColor: CarColor?,
    val seats: Int?,
    val status: TaxiStatus?,
)