package com.angus.service_identity.endpoints.model.mapper

import com.angus.service_identity.data.collection.mappers.toEntity
import com.angus.service_identity.domain.entity.Address
import com.angus.service_identity.endpoints.model.AddressDto

fun Address.toDto(): AddressDto {
    return AddressDto(
        id = id,
        location = location?.toDto(),
        address = address
    )
}

fun List<Address>.toDto(): List<AddressDto> {
    return map { it.toDto() }
}

fun AddressDto.toEntity() = Address(
    id = id ?: "",
    location = location?.toEntity(),
    address = address
)

fun List<AddressDto>.toEntity() = map { it.toEntity() }

