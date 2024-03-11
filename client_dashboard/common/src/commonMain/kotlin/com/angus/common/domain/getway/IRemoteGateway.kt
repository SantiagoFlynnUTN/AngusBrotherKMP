package com.angus.common.domain.getway

import com.angus.common.domain.entity.RevenueShare
import com.angus.common.domain.entity.TotalRevenueShare
import com.angus.common.domain.util.RevenueShareDate


interface IRemoteGateway {

    suspend fun getCurrentLocation(): String

    suspend fun getRevenueShare(revenueShareDate: RevenueShareDate): TotalRevenueShare
    suspend fun getDashboardRevenueShare(): RevenueShare

}