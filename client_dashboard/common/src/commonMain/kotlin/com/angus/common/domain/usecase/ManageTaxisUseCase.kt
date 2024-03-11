package com.angus.common.domain.usecase

import com.angus.common.domain.entity.DataWrapper
import com.angus.common.domain.entity.NewTaxiInfo
import com.angus.common.domain.entity.Taxi
import com.angus.common.domain.entity.TaxiFiltration
import com.angus.common.domain.getway.IRemoteGateway
import com.angus.common.domain.getway.ITaxisGateway


interface IManageTaxisUseCase {

    suspend fun createTaxi(addTaxi: NewTaxiInfo): Taxi

    suspend fun createTaxiReport()

    suspend fun updateTaxi(addTaxi: NewTaxiInfo,taxiId:String): Taxi

    suspend fun deleteTaxi(taxiId: String): Taxi

}

class ManageTaxisUseCase(
    private val taxiGateway: ITaxisGateway,
) : IManageTaxisUseCase {

    override suspend fun createTaxi(addTaxi: NewTaxiInfo): Taxi {
        return taxiGateway.createTaxi(addTaxi)
    }

    override suspend fun createTaxiReport() {
        return taxiGateway.getPdfTaxiReport()
    }

    override suspend fun deleteTaxi(taxiId: String): Taxi {
        return taxiGateway.deleteTaxi(taxiId)
    }

    override suspend fun updateTaxi(addTaxi: NewTaxiInfo,taxiId:String): Taxi {
        return taxiGateway.updateTaxi(addTaxi,taxiId)
    }

}