package com.angus.common.data.gateway.remote

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import com.angus.common.data.gateway.remote.mapper.toDto
import com.angus.common.data.gateway.remote.mapper.toEntity
import com.angus.common.data.gateway.remote.model.PaginationResponse
import com.angus.common.data.gateway.remote.model.ServerResponse
import com.angus.common.data.gateway.remote.model.TaxiDto
import com.angus.common.domain.entity.DataWrapper
import com.angus.common.domain.entity.NewTaxiInfo
import com.angus.common.domain.entity.Taxi
import com.angus.common.domain.entity.TaxiFiltration
import com.angus.common.domain.getway.ITaxisGateway
import com.angus.common.domain.util.NotFoundException

class TaxisGateway(private val client: HttpClient) : BaseGateway(), ITaxisGateway {
    override suspend fun getPdfTaxiReport() {
        TODO("Not yet implemented")
    }

    override suspend fun getTaxis(
        username: String?,
        taxiFiltration: TaxiFiltration,
        page: Int,
        limit: Int
    ): DataWrapper<Taxi> {
        val result = tryToExecute<ServerResponse<PaginationResponse<TaxiDto>>>(client) {
           val taxiFiltrationDto= taxiFiltration.toDto()
            get(urlString = "/taxis/search") {
                parameter("page", page)
                parameter("limit", limit)
                parameter("status", taxiFiltrationDto.status)
                parameter("color", taxiFiltrationDto.color)
                parameter("seats", taxiFiltrationDto.seats)
                parameter("query", username)
            }
        }.value
        return paginateData(result?.items?.toEntity() ?: emptyList(), limit, result?.total ?: 0)
    }

    override suspend fun createTaxi(taxi: NewTaxiInfo): Taxi {
        val result = tryToExecute<ServerResponse<TaxiDto>>(client) {
            post(urlString = "/taxi") {
                setBody(taxi.toDto())
            }
        }.value
        return result?.toEntity() ?: throw UnknownError()
    }

    override suspend fun updateTaxi(taxi: NewTaxiInfo,taxiId:String): Taxi {
        val result = tryToExecute<ServerResponse<TaxiDto>>(client) {
            put(urlString = "/taxi/$taxiId") {
                contentType(ContentType.Application.Json)
                setBody(taxi.toDto())
            }
        }.value
        return result?.toEntity() ?: throw UnknownError()
    }

    override suspend fun deleteTaxi(taxiId: String): Taxi {
        return tryToExecute<ServerResponse<TaxiDto>>(client) {
            delete(urlString = "/taxi") { url { appendPathSegments(taxiId) } }
        }.value?.toEntity() ?: throw NotFoundException("Taxi not found")
    }

    override suspend fun getTaxiById(taxiId: String): Taxi {
        return tryToExecute<ServerResponse<TaxiDto>>(client) {
            get(urlString = "/taxi") { url { appendPathSegments(taxiId) } }
        }.value?.toEntity() ?: throw NotFoundException("Taxi not found")
    }

}