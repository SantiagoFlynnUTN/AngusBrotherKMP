package com.angus.service_taxi.api.dto.taxi

import org.bson.types.ObjectId
import com.angus.service_taxi.data.collection.TaxiCollection
import com.angus.service_taxi.domain.entity.Color
import com.angus.service_taxi.domain.entity.Taxi
import com.angus.service_taxi.domain.exceptions.CantBeNullException

fun TaxiDto.toEntity(): Taxi {
    return Taxi(
        id = if (id.isNullOrBlank()) ObjectId().toHexString() else ObjectId(id).toHexString(),
        plateNumber = plateNumber ?: "",
        color = color?.let { Color.getColorByColorNumber(it) } ?: Color.OTHER,
        type = type ?: "",
        driverId = driverId ?: "",
        driverUsername = driverUsername ?: "",
        driverImage = driverImage ?: "",
        rate = rate ?: 0.0,
        isAvailable = isAvailable ?: true,
        seats = seats ?: 4,
        tripsCount = tripsCount ?: 0
    )
}

fun Taxi.toDto(): TaxiDto {
    return TaxiDto(
        id = id,
        plateNumber = plateNumber,
        color = color.colorNumber,
        type = type,
        driverId = driverId,
        isAvailable = isAvailable,
        tripsCount = tripsCount,
        driverUsername = driverUsername,
        driverImage = driverImage,
        rate = rate,
        seats = seats
    )
}

fun List<Taxi>.toDto(): List<TaxiDto> = map(Taxi::toDto)


fun TaxiCollection.toEntity(): Taxi {
    return Taxi(
        id = id.toString(),
        plateNumber = plateNumber ?: throw CantBeNullException(),
        driverUsername = driverUsername ?: "",
        driverImage = driverImage ?: "",
        rate = rate ?: 0.0,
        color = color?.let { Color.getColorByColorNumber(it) } ?: throw CantBeNullException(),
        type = type ?: throw CantBeNullException(),
        driverId = driverId?.toString() ?: throw CantBeNullException(),
        isAvailable = isAvailable ?: true,
        seats = seats ?: 4,
        tripsCount = tripsCount ?: 0,
    )
}

fun List<TaxiCollection>.toEntity(): List<Taxi> = map(TaxiCollection::toEntity)

fun Taxi.toCollection(): TaxiCollection {
    return TaxiCollection(
        plateNumber = plateNumber,
        driverUsername = driverUsername,
        driverImage = driverImage,
        rate = rate,
        color = color.colorNumber,
        type = type,
        driverId = ObjectId(driverId),
        isAvailable = isAvailable,
        seats = seats,
        tripsCount = tripsCount,
        id = if (id.isNotBlank()) ObjectId(id) else ObjectId()
    )
}