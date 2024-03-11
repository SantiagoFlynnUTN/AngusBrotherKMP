package com.angus.common.domain.getway

import com.angus.common.domain.entity.DataWrapper
import com.angus.common.domain.entity.NewTaxiInfo
import com.angus.common.domain.entity.Taxi
import com.angus.common.domain.entity.TaxiFiltration

interface ITaxisGateway {

    suspend fun getPdfTaxiReport()
    suspend fun getTaxis(
        username: String?,
        taxiFiltration: TaxiFiltration,
        page: Int,
        limit: Int
    ): DataWrapper<Taxi>

    suspend fun createTaxi(taxi: NewTaxiInfo): Taxi

    suspend fun updateTaxi(taxi: NewTaxiInfo,taxiId:String): Taxi

    suspend fun deleteTaxi(taxiId: String): Taxi

    suspend fun getTaxiById(taxiId: String): Taxi

}