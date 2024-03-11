package com.angus.service_identity.data.collection.mappers

import com.angus.service_identity.data.collection.AddressCollection
import com.angus.service_identity.domain.entity.Address

fun AddressCollection.toEntity() = Address(
    id = id.toString(),
    location = location?.toEntity(),
    address = address ?: ""
)


fun List<AddressCollection>.toEntity(): List<Address> {
    return this.map { it.toEntity() }
}
