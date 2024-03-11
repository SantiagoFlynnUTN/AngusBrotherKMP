package com.angus.common.data.gateway.remote.mapper

import com.angus.common.data.gateway.remote.model.OrdersRevenueDto
import com.angus.common.data.gateway.remote.model.RevenueShareDto
import com.angus.common.data.gateway.remote.model.TripsRevenueDto
import com.angus.common.domain.entity.OrdersRevenue
import com.angus.common.domain.entity.RevenueShare
import com.angus.common.domain.entity.TripsRevenue

fun RevenueShareDto.toEntity(): RevenueShare {
    return RevenueShare(
            ordersRevenueShare =  ordersRevenueShare.toEntity(),
            tripsRevenueShare = tripsRevenueShare.toEntity()
    )
}

fun TripsRevenueDto.toEntity(): TripsRevenue {
    return TripsRevenue(
            acceptedTrips = acceptedTrips?: 0.0,
            pendingTrips = pendingTrips?: 0.0,
            rejectedTrips = rejectedTrips?: 0.0,
            canceledTrips = canceledTrips?: 0.0,
    )
}

fun OrdersRevenueDto.toEntity(): OrdersRevenue {
    return OrdersRevenue(
            completedOrders = completedOrders?: 0.0,
            canceledOrders = canceledOrders?: 0.0,
            inTheWayOrders = inTheWayOrders?: 0.0,
    )
}